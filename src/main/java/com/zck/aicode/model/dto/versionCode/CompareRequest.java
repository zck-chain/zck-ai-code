package com.zck.aicode.model.dto.versionCode;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author 赵承康
 * @date 2026/2/10
 */
@Data
public class CompareRequest {
    @NotBlank(message = "应用id不能为空")
    @Parameter(description = "应用id", required = true)
    private long appId;
    @NotBlank(message = "源版本ID不能为空")
    @Parameter(description = "源版本ID", required = true)
    private long versionId1;
    @NotBlank(message = "目标版本ID不能为空")
    @Parameter(description = "目标版本ID", required = true)
    private long versionId2;
}
