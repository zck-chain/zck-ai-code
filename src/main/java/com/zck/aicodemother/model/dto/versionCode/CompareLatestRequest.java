package com.zck.aicodemother.model.dto.versionCode;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author 赵承康
 * @date 2026/2/10
 */
@Data
public  class CompareLatestRequest {
    @NotBlank(message = "应用ID不能为空")
    @Parameter(description = "应用id", required = true)
    private long appId;

    @NotBlank(message = "代码内容不能为空")
    @Parameter(description = "要对比的代码内容", required = true, example = "public class Hello {\n    public void sayHello() {\n        System.out.println(\"Hello World!\");\n    }\n}")
    private String codeContent;
}
