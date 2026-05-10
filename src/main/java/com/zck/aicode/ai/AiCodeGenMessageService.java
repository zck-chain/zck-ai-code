package com.zck.aicode.ai;

import dev.langchain4j.service.SystemMessage;

/**
 * 优化客户提示词
 */
public interface AiCodeGenMessageService {

    /**
     * 获取代码生成提示
     * @param prompt
     * @return
     */
    @SystemMessage(fromResource = "prompt/codegen-message-system-prompt.txt")
    String optimiseMessage(String prompt);
}
