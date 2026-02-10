package com.zck.aicodemother.model.dto.versionCode;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 赵承康
 * @date 2026/2/10
 */
@Data
public  class RollbackRequest {
    @NotBlank
    @Parameter(description = "应用ID", required = true)
    private long appId;
    @NotBlank
    @Parameter(description = "目标版本ID", required = true)
    private long targetVersionId;
    @Parameter(description = "回滚提交信息", example = "修复bug，回滚到稳定版本")
    private String commitMessage;
    @NotBlank(message = "作者不能为空")
    @Parameter(description = "作者", required = true, example = "管理员")
    private long author;
}
