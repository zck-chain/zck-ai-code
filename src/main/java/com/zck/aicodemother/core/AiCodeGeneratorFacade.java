package com.zck.aicodemother.core;

import cn.hutool.json.JSONUtil;
import com.zck.aicodemother.ai.AiCodeGeneratorService;
import com.zck.aicodemother.ai.AiCodeGeneratorServiceFactory;
import com.zck.aicodemother.ai.model.HtmlCodeResult;
import com.zck.aicodemother.ai.model.MultiFileCodeResult;
import com.zck.aicodemother.ai.model.message.AiResponseMessage;
import com.zck.aicodemother.ai.model.message.ToolExecutedMessage;
import com.zck.aicodemother.ai.model.message.ToolRequestMessage;
import com.zck.aicodemother.constant.AppConstant;
import com.zck.aicodemother.core.builder.VueProjectBuilder;
import com.zck.aicodemother.core.parser.CodeParserExecutor;
import com.zck.aicodemother.core.saver.CodeFileSaverExecutor;
import com.zck.aicodemother.exception.BusinessException;
import com.zck.aicodemother.exception.ErrorCode;
import com.zck.aicodemother.model.enums.CodeGenTypeEnum;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.tool.ToolExecution;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * Ai 代码生成外观类,组合生成和保存功能
 * @author 赵承康
 * @date 2026/1/22
 */
@Service
@Slf4j
public class AiCodeGeneratorFacade {
    @Resource
    private AiCodeGeneratorServiceFactory aiCodeGeneratorServiceFactory;
    @Resource
    private VueProjectBuilder vueProjectBuilder;

    /**
     * 统一入口：根据类型生成并保存代码
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @return 保存的目录
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum,Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId);
        return switch (codeGenTypeEnum) {
            case HTML -> {
                HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.HTML,appId);
            }
            case MULTI_FILE -> {
                MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.MULTI_FILE,appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 统一入口：根据类型生成并保存代码（流式）
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum,Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId,codeGenTypeEnum);
        return switch (codeGenTypeEnum) {
            case HTML -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.HTML,appId);
            }
            case MULTI_FILE -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.MULTI_FILE,appId);
            }
            case VUE_PROJECT -> {
                TokenStream tokenStream = aiCodeGeneratorService.generateVueProjectCodeStream(appId, userMessage);
                yield  processTokenStream(tokenStream,appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 将 TokenStream 转换为 Flux<String>，并传递工具调用信息
     *
     * @param tokenStream TokenStream 对象
     * @return Flux<String> 流式响应
     */
    private Flux<String> processTokenStream(TokenStream tokenStream,long appId) {
        return Flux.create(sink -> {
            //1.收集AI响应消息
            tokenStream.onPartialResponse((String partialResponse) -> {
                        AiResponseMessage aiResponseMessage = new AiResponseMessage(partialResponse);
                        sink.next(JSONUtil.toJsonStr(aiResponseMessage));
                    })
                    //2.收集工具调用消息
                    .onPartialToolExecutionRequest((index, toolExecutionRequest) -> {
                        ToolRequestMessage toolRequestMessage = new ToolRequestMessage(toolExecutionRequest);
                        sink.next(JSONUtil.toJsonStr(toolRequestMessage));
                    })
                    //3.工具执行结果消息
                    .onToolExecuted((ToolExecution toolExecution) -> {
                        ToolExecutedMessage toolExecutedMessage = new ToolExecutedMessage(toolExecution);
                        sink.next(JSONUtil.toJsonStr(toolExecutedMessage));
                    })
                    .onCompleteResponse((ChatResponse response) -> {
                        String projectPath = AppConstant.CODE_OUTPUT_ROOT_DIR +File.separator+"vue_project_" + appId;
                        vueProjectBuilder.buildProjectAsync(projectPath);
                        sink.complete();
                    })
                    .onError((Throwable error) -> {
                        error.printStackTrace();
                        sink.error(error);
                    })
                    .start();
        });
    }



    private Flux<String> processCodeStream(Flux<String> codeStream,CodeGenTypeEnum codeGenTypeEnum,Long appId) {
        StringBuilder codeBuilder = new StringBuilder();
        return codeStream.doOnNext(chuck->{
            //实时收集代码片段
            codeBuilder.append(chuck);
        }).doOnComplete(()->{
            //流式返回完成后保存代码
            try {
                String completeCode = codeBuilder.toString();
                //使用执行器解析代码
                Object parsedResult = CodeParserExecutor.execute(completeCode, codeGenTypeEnum);
                //使用执行器保存代码
                File savedDir = CodeFileSaverExecutor.executeSaver(parsedResult, codeGenTypeEnum,appId);
                //保存代码到文件
                log.info("保存成功,路径:{}",savedDir.getAbsolutePath());
            }catch (Exception e){
                log.error("保存失败:{}",e.getMessage());
            }
        });
    }

}
