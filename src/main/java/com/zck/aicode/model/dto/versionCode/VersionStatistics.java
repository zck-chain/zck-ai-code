package com.zck.aicode.model.dto.versionCode;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 赵承康
 * @date 2026/2/10
 */
@Data
public class VersionStatistics {
    @Parameter(description = "应用ID")
    private Long appId;

    @Parameter(description = "总版本数")
    private Integer totalVersions;

    @Parameter(description = "第一个版本")
    private String firstVersion;

    @Parameter(description = "最新版本")
    private String latestVersion;

    @Parameter(description = "最后更新时间")
    private LocalDateTime lastUpdated;
}
