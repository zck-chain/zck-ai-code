package com.zck.aicodemother.core;

import com.zck.aicodemother.ai.AiCodeGeneratorService;
import com.zck.aicodemother.ai.model.HtmlCodeResult;
import com.zck.aicodemother.ai.model.MultiFileCodeResult;
import com.zck.aicodemother.exception.BusinessException;
import com.zck.aicodemother.exception.ErrorCode;
import com.zck.aicodemother.model.enums.CodeGenTypeEnum;
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
    private AiCodeGeneratorService aiCodeGeneratorService;


    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum){
        if (codeGenTypeEnum==null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"生成代码为空");
        }
        return switch (codeGenTypeEnum) {
            case  HTML -> generateAndSaveHtmlCodeSteam(userMessage);
            case  MULTI_FILE -> generateAndSaveMultiFileCodeStream(userMessage);
            default -> {
                String errorMessage="不支持的生成类型"+codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,errorMessage);
            }

        };
    }

    private Flux<String> generateAndSaveMultiFileCodeStream(String userMessage) {
        Flux<String> result = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
        StringBuilder codeBuilder = new StringBuilder();
        return result.doOnNext(chuck->{
            //实时收集代码片段
            codeBuilder.append(chuck);
        }).doOnComplete(()->{
            //流式返回完成后保存代码
            try {
                String completeMultiFileCode = codeBuilder.toString();
                MultiFileCodeResult multiFileCodeResult = CodeParser.parseMultiFileCode(completeMultiFileCode);
                //保存代码到文件
                File savedDir = CodeFileSaver.saveMultiFileCodeResult(multiFileCodeResult);
                log.info("保存成功,路径:{}",savedDir.getAbsolutePath());
            }catch (Exception e){
              log.error("保存失败:{}",e.getMessage());
            }
        });
    }

    private Flux<String> generateAndSaveHtmlCodeSteam(String userMessage) {
        Flux<String> result = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
        StringBuilder codeBuilder = new StringBuilder();
        return result.doOnNext(chuck->{
            //实时收集代码片段
            codeBuilder.append(chuck);
        }).doOnComplete(()->{
            //流式返回完成后保存代码
            try {
                String completeHtmlCode = codeBuilder.toString();
                HtmlCodeResult htmlCodeResult = CodeParser.parseHtmlCode(completeHtmlCode);
                //保存代码到文件
                File savedDir = CodeFileSaver.saveHtmlCodeResult(htmlCodeResult);
                log.info("保存成功,路径:{}",savedDir.getAbsolutePath());
            }catch (Exception e){
                log.error("保存失败:{}",e.getMessage());
            }
        });
    }

    /**
     * 统一入口: 根据类型生成保存代码
     * @param userMessage 用户输入信息
     * @param codeGenTypeEnum 枚举
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum){
        if (codeGenTypeEnum==null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"生成代码为空");
        }
        return switch (codeGenTypeEnum) {
            case  HTML -> generateAndSaveHtmlCode(userMessage);
            case  MULTI_FILE -> generateAndSaveMultiFileCode(userMessage);
            default -> {
                String errorMessage="不支持的生成类型"+codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,errorMessage);
            }

        };
    }

    /**
     * 生成HTML模式的代码并保存
     * @param userMessage
     * @return
     */
    private File generateAndSaveMultiFileCode(String userMessage) {
        MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode(userMessage);
        return CodeFileSaver.saveMultiFileCodeResult(result);
    }

    /**
     * 生成多文件模式的代码并保存
     * @param userMessage
     * @return
     */
    private File generateAndSaveHtmlCode(String userMessage) {
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(userMessage);
        return CodeFileSaver.saveHtmlCodeResult(result);
    }
}
