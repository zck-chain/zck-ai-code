package com.zck.aicodemother.ai;

import com.zck.aicodemother.model.enums.CodeGenTypeEnum;
import dev.langchain4j.service.SystemMessage;

/**
 * AI代码生成类型路由服务
 * @author 赵承康
 * @date 2026/3/10
 */
public interface AiCodeGenTypeRoutingService {
    /**
     * 根据用户需求选择代码生成类型
     * @param userPrompt
     * @return
     */
    @SystemMessage(fromResource = "prompt/codegen-routing-system-prompt.txt")
    CodeGenTypeEnum routeCodeGenType(String userPrompt);
}
