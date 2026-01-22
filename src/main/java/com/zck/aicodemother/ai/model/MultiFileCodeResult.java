package com.zck.aicodemother.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * @author 赵承康
 * @date 2026/1/22
 */
@Data
public class MultiFileCodeResult {

    @Description("html代码")
    private String htmlCode;

    @Description("css代码")
    private String cssCode;

    @Description("js代码")
    private String jsCode;

    @Description("生成代码的描述")
    private String description;
}
