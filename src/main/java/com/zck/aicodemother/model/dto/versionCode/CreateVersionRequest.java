package com.zck.aicodemother.model.dto.versionCode;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author 赵承康
 * @date 2026/2/10
 */
@Data
public class CreateVersionRequest {
    @NotBlank(message = "应用ID不能为空")
    @Parameter(description = "应用ID", required = true, example = "app_001")
    private Long appId;

    @NotBlank(message = "代码内容不能为空")
    @Parameter(description = "代码内容", required = true, example = "public class Hello {\n    public void sayHello() {\n        System.out.println(\"Hello\");\n    }\n}")
    private String codeContent;

    @NotBlank(message = "提交信息不能为空")
    @Parameter(description = "提交信息", required = true, example = "初始化版本")
    private String commitMessage;

    @NotBlank(message = "作者不能为空")
    @Parameter(description = "作者", required = true, example = "张三")
    private Long author;
}
