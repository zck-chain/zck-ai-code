package com.zck.aicode.ai;

import dev.langchain4j.service.SystemMessage;

/**
 * 优化客户提示词
 */
public interface AiCodeGenPromptService {

    /**
     * 获取代码生成提示
     * @param prompt
     * @return
     */
    @SystemMessage(fromResource = "prompt/codegen-promptoptimization-system-prompt.txt")
    String getAiCodeGenPrompt(String prompt);
}
