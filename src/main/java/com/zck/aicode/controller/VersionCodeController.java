package com.zck.aicode.controller;

import cn.hutool.core.util.StrUtil;
import com.zck.aicode.common.BaseResponse;
import com.zck.aicode.common.ResultUtils;
import com.zck.aicode.exception.ErrorCode;
import com.zck.aicode.exception.ThrowUtils;
import com.zck.aicode.model.dto.versionCode.*;
import com.zck.aicode.model.entity.VersionCode;
import com.zck.aicode.model.vo.DiffResult;
import com.zck.aicode.service.VersionCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 代码版本管理控制器
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/versions")
@Tag(name = "VersionCodeController", description = "提供代码版本创建、对比、回滚等功能")
public class VersionCodeController {

    @Resource
    private VersionCodeService versionCodeService;

    @PostMapping("/create")
    @Operation(summary = "创建新版本", description = "为指定应用创建新的代码版本")
    public BaseResponse<VersionCode> createVersion(
            @Valid @RequestBody CreateVersionRequest request) {
        ThrowUtils.throwIf(request==null || request.getAppId()<=0, ErrorCode.PARAMS_ERROR,"appId不能为空");
        ThrowUtils.throwIf( request.getCodeContent()==null, ErrorCode.PARAMS_ERROR,"codeContent不能为空");
        ThrowUtils.throwIf(request.getAuthor()<=0, ErrorCode.PARAMS_ERROR,"author不能为空");
        ThrowUtils.throwIf( request.getCommitMessage()==null, ErrorCode.PARAMS_ERROR,"commitMessage不能为空");
        String appId = String.valueOf(request.getAppId());


        VersionCode version = versionCodeService.createVersion(
                appId,
                request.getCodeContent(),
                request.getCommitMessage(),
                request.getAuthor()
        );

        return ResultUtils.success(version);
    }

    @GetMapping("/{appId}/compare/{versionId1}/{versionId2}")
    @Operation(summary = "对比两个版本", description = "对比指定两个版本之间的代码差异")
    public BaseResponse<DiffResult> compareVersions(@Valid @RequestBody CompareRequest request) {
        ThrowUtils.throwIf(request==null || request.getAppId()<=0, ErrorCode.PARAMS_ERROR,"appId不能为空");
        ThrowUtils.throwIf(request.getVersionId2()<=0, ErrorCode.PARAMS_ERROR,"versionId1不能为空");
        ThrowUtils.throwIf(request.getVersionId2()<=0, ErrorCode.PARAMS_ERROR,"versionId2不能为空");
        String v1 = String.valueOf(request.getVersionId1());
        String v2 = String.valueOf(request.getVersionId2());
        DiffResult diffResult = versionCodeService.compareVersions(v1, v2);
        return ResultUtils.success(diffResult);
    }

    @PostMapping("/{appId}/compare/latest")
    @Operation(summary = "与最新版本对比", description = "将提供的代码与指定应用的最新版本进行对比")
    public BaseResponse<DiffResult> compareWithLatest(@Valid @RequestBody CompareLatestRequest request) {
        ThrowUtils.throwIf( request==null || request.getAppId()<=0, ErrorCode.PARAMS_ERROR,"appId不能为空");
        String appId = String.valueOf(request.getAppId());
        DiffResult diffResult = versionCodeService.compareWithLatest(
                appId, request.getCodeContent());
        return ResultUtils.success(diffResult);
    }

    @GetMapping("/{appId}/list")
    @Operation(summary = "获取版本列表", description = "获取指定应用的所有版本列表，按创建时间倒序排列")
    public BaseResponse<List<VersionCode>> getVersionList(
            @Parameter(description = "应用ID", required = true)
            @PathVariable long appId) {
        ThrowUtils.throwIf(appId<=0, ErrorCode.PARAMS_ERROR,"appId不能为空");
        List<VersionCode> versions = versionCodeService.getVersionList(String.valueOf(appId));
        return ResultUtils.success(versions);
    }

    @GetMapping("/detail/{versionId}")
    @Operation(summary = "获取版本详情", description = "根据版本ID获取具体的版本信息")
    public BaseResponse<VersionCode> getVersionDetail(
            @Parameter(description = "版本ID", required = true)
            @PathVariable long versionId) {
        ThrowUtils.throwIf(versionId<=0, ErrorCode.PARAMS_ERROR,"versionId不能为空");
        VersionCode version = versionCodeService.getVersion(String.valueOf(versionId));
        return ResultUtils.success(version);
    }

    @PostMapping("/{appId}/rollback/{targetVersionId}")
    @Operation(summary = "回滚到指定版本", description = "将应用代码回滚到指定的历史版本，并创建新的回滚版本")
    public BaseResponse<VersionCode> rollbackToVersion(
            @Valid @RequestBody RollbackRequest request) {
        ThrowUtils.throwIf(request==null || request.getAppId()<=0, ErrorCode.PARAMS_ERROR,"appId不能为空");
        ThrowUtils.throwIf(request.getTargetVersionId()<=0, ErrorCode.PARAMS_ERROR,"targetVersionId不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(request.getCommitMessage()), ErrorCode.PARAMS_ERROR,"commitMessage不能为空");
        ThrowUtils.throwIf(request.getAuthor()<=0 , ErrorCode.PARAMS_ERROR,"author不能为空");
        VersionCode version = versionCodeService.rollbackToVersion(
                String.valueOf(request.getAppId()), String.valueOf(request.getTargetVersionId()), request.getCommitMessage(), request.getAuthor());
        return ResultUtils.success(version);
    }

    @DeleteMapping("/{appId}")
    @Operation(summary = "删除应用所有版本", description = "删除指定应用的所有版本记录（谨慎操作）")
    public BaseResponse<String> deleteAppVersions(
            @Parameter(description = "应用ID", required = true)
            @PathVariable long appId) {
        ThrowUtils.throwIf(appId<=0, ErrorCode.PARAMS_ERROR,"appId不能为空");
        versionCodeService.deleteAppVersions(String.valueOf(appId));
        return ResultUtils.success("删除成功");
    }

    @GetMapping("/{appId}/latest")
    @Operation(summary = "获取最新版本", description = "获取指定应用的最新代码版本")
    public BaseResponse<VersionCode> getLatestVersion(
            @Parameter(description = "应用ID", required = true)
            @PathVariable long appId) {
        ThrowUtils.throwIf(appId<=0, ErrorCode.PARAMS_ERROR,"appId不能为空");
        List<VersionCode> versions = versionCodeService.getVersionList(String.valueOf(appId));
        return ResultUtils.success(versions.get(versions.size() - 1));
    }

    @GetMapping("/{appId}/statistics")
    @Operation(summary = "获取版本统计", description = "获取指定应用的版本统计信息")
    public BaseResponse<VersionStatistics> getVersionStatistics(
            @Parameter(description = "应用ID", required = true)
            @PathVariable long appId) {
        ThrowUtils.throwIf(appId<=0, ErrorCode.PARAMS_ERROR,"appId不能为空");

        List<VersionCode> versions = versionCodeService.getVersionList(String.valueOf(appId));
        VersionStatistics stats = new VersionStatistics();
        stats.setTotalVersions(versions.size());
        stats.setAppId(appId);

        if (!versions.isEmpty()) {
            stats.setLatestVersion(versions.get(versions.size() - 1).getVersion());
            stats.setFirstVersion(versions.get(0).getVersion());
            stats.setLastUpdated(versions.get(versions.size() - 1).getCreatorTime());
        }

        return ResultUtils.success(stats);
    }

    @GetMapping("/{appId}/search")
    @Operation(summary = "搜索版本", description = "根据关键字搜索指定应用的版本")
    public BaseResponse<List<VersionCode>> searchVersions(
            @Parameter(description = "应用ID", required = true)
            @PathVariable long appId,
            @Parameter(description = "搜索关键字")
            @RequestParam(required = false) String keyword) {
        ThrowUtils.throwIf(appId<=0, ErrorCode.PARAMS_ERROR,"appId不能为空");

        List<VersionCode> versions = versionCodeService.getVersionList(String.valueOf(appId));
        if (keyword != null && !keyword.trim().isEmpty()) {
            versions = versions.stream()
                    .filter(v -> v.getVersion().contains(keyword) ||
                            v.getCommitMessage().toLowerCase().contains(keyword.toLowerCase()) ||
                            v.getCodeContent().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
        }
        return ResultUtils.success(versions);
    }
    
}