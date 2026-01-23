package com.zck.aicodemother.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 赵承康
 * @date 2026/1/22
 */
@Configuration
public class AiCodeGeneratorServiceFactory {
    @Resource
    private ChatModel chatModel;

    @Resource
    private StreamingChatModel steamingChatModel;

    @Bean
    public AiCodeGeneratorService aiCodeGeneratorService(){
        return AiServices.builder(AiCodeGeneratorService.class)
                .chatModel(chatModel)
                .streamingChatModel(steamingChatModel)
                .build();
    }
}
