package com.zck.aicodemother.ai;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zck.aicodemother.ai.guardail.PromptSafetyInputGuardrail;
import com.zck.aicodemother.ai.tool.FileWriteTool;
import com.zck.aicodemother.ai.tool.ToolManager;
import com.zck.aicodemother.exception.BusinessException;
import com.zck.aicodemother.exception.ErrorCode;
import com.zck.aicodemother.model.enums.CodeGenTypeEnum;
import com.zck.aicodemother.service.ChatHistoryService;
import com.zck.aicodemother.utils.SpringContextUtil;
import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static com.zck.aicodemother.model.enums.CodeGenTypeEnum.*;

/**
 * @author 赵承康
 * @date 2026/1/22
 */
@Configuration
@Slf4j
public class AiCodeGeneratorServiceFactory {
    @Resource(name="openAiChatModel")
    private ChatModel chatModel;
    @Resource
    private RedisChatMemoryStore redisChatMemoryStore;
    @Resource
    private ChatHistoryService chatHistoryService;
    @Resource
    private ToolManager toolManager;
    /**
     * AI服务实例缓存
     * 缓存策略:
     * -最大缓存1000个实例
     * -写入后30分钟过期
     * -访问后10分钟过期
     */
    private final Cache<String,AiCodeGeneratorService> serviceCache= Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMinutes(30))
            .expireAfterAccess(Duration.ofMinutes(10))
            .removalListener((key,value,cause)->{
                log.debug("AI服务实例被移除,appId:{},原因:{}",key,cause);
            })
            .build();

    public AiCodeGeneratorService getAiCodeGeneratorService(long appId){
        return getAiCodeGeneratorService(appId, HTML);
    }

    /*
    * 根据appId获取服务（带缓存）
    * */
    public AiCodeGeneratorService getAiCodeGeneratorService(long appId,CodeGenTypeEnum codeGenTypeEnum){
        String cacheKey=buildCacheKey(appId,codeGenTypeEnum);
        return serviceCache.get(cacheKey,key->createAiCodeGeneratorService(appId,codeGenTypeEnum));
    }

    /**
     * 构建缓存键
     * @param appId
     * @param codeGenTypeEnum
     * @return
     */
    private String buildCacheKey(long appId, CodeGenTypeEnum codeGenTypeEnum) {
        return appId+"_"+codeGenTypeEnum.getValue();
    }

//    private AiCodeGeneratorService createAiCodeGeneratorService(long appId){
//        log.info("为appId:{}创建新的AI服务实例",appId);
//        //根据appId构建独立的对话记忆
//        MessageWindowChatMemory chatMemory = MessageWindowChatMemory
//                .builder()
//                .id(appId)
//                .chatMemoryStore(redisChatMemoryStore)
//                .maxMessages(20)
//                .build();
//        //从数据库加载历史对话到记忆中
//        chatHistoryService.loadChatHistoryToMemory(appId,chatMemory,20);
//        return AiServices.builder(AiCodeGeneratorService.class)
//                .chatModel(chatModel)
//                .streamingChatModel(openAiStreamingChatModel)
//                .chatMemory(chatMemory)
//                .build();
//    }

    /*
    * 创建新的AI服务实例
    * */
    private AiCodeGeneratorService createAiCodeGeneratorService(long appId, CodeGenTypeEnum codeGenType){
        //根据appId构建独立的对话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .id(appId)
                .chatMemoryStore(redisChatMemoryStore)
                .maxMessages(50)
                .build();
        //从数据库加载历史记录到记忆中
        chatHistoryService.loadChatHistoryToMemory(appId,chatMemory,20);
        // 根据代码生成类型选择不同的模型配置
        return switch (codeGenType) {
            case VUE_PROJECT -> {
                // 使用多例模式的 StreamingChatModel 解决并发问题
                StreamingChatModel reasoningStreamingChatModel = SpringContextUtil.getBean("reasoningStreamingChatModelPrototype", StreamingChatModel.class);
                yield AiServices.builder(AiCodeGeneratorService.class)
                        .streamingChatModel(reasoningStreamingChatModel)
                        .maxSequentialToolsInvocations(20)//最多连续调用20次工具
                        .chatMemoryProvider(memoryId -> chatMemory)
                        .tools(toolManager.getAllTools())
                        .hallucinatedToolNameStrategy(toolExecutionRequest -> ToolExecutionResultMessage.from(
                                toolExecutionRequest, "Error: there is no tool called " + toolExecutionRequest.name()
                        ))
                        .inputGuardrails(new PromptSafetyInputGuardrail())//添加输入护轨
                        .build();
            }
            case HTML, MULTI_FILE -> {
                // 使用多例模式的 StreamingChatModel 解决并发问题
                StreamingChatModel openAiStreamingChatModel = SpringContextUtil.getBean("streamingChatModelPrototype", StreamingChatModel.class);
                yield AiServices.builder(AiCodeGeneratorService.class)
                        .chatModel(chatModel)
                        .streamingChatModel(openAiStreamingChatModel)
                        .chatMemory(chatMemory)
                        .inputGuardrails(new PromptSafetyInputGuardrail())//添加输入护轨
                        .build();
            }
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR,
                    "不支持的代码生成类型: " + codeGenType.getValue());
        };

    }

}
