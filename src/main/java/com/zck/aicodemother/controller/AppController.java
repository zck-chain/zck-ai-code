package com.zck.aicodemother.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.paginate.Page;
import com.zck.aicodemother.annotation.AuthCheck;
import com.zck.aicodemother.common.BaseResponse;
import com.zck.aicodemother.common.DeleteRequest;
import com.zck.aicodemother.common.ResultUtils;
import com.zck.aicodemother.constant.AppConstant;
import com.zck.aicodemother.constant.UserConstant;
import com.zck.aicodemother.exception.BusinessException;
import com.zck.aicodemother.exception.ErrorCode;
import com.zck.aicodemother.exception.ThrowUtils;
import com.zck.aicodemother.model.dto.app.AppAddRequest;

import com.zck.aicodemother.model.dto.app.AppDeployRequest;
import com.zck.aicodemother.model.dto.app.AppQueryRequest;
import com.zck.aicodemother.model.dto.app.AppUpdateRequest;
import com.zck.aicodemother.model.entity.App;
import com.zck.aicodemother.model.entity.User;
import com.zck.aicodemother.service.AppService;
import com.zck.aicodemother.service.ProjectDownloadService;
import com.zck.aicodemother.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.io.File;
import java.util.Map;

/**
 * 应用 控制层。
 *
 * @author 赵承康
 */
@RestController
@RequestMapping("/app")
@Tag(name = "AppController", description = "提供应用创建、更新、删除、查询等功能")
public class AppController {

    @Resource
    private AppService appService;
    @Resource
    private UserService userService;
    @Resource
    private ProjectDownloadService projectDownloadService;

    // 【用户】创建应用（须填写 initPrompt）
    @PostMapping("/create")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    @Operation(summary = "创建应用", description = "用户创建新应用，须填写 initPrompt")
    public BaseResponse<Long> createApp(@RequestBody AppAddRequest addAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(addAddRequest == null, ErrorCode.PARAMS_ERROR);
        long result = appService.createApp(addAddRequest, request);
        return ResultUtils.success(result);
    }

    // 【用户】根据 id 修改自己的应用（目前只支持修改应用名称）
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    @Operation(summary = "更新应用", description = "用户修改自己的应用信息，目前只支持修改应用名称")
    public BaseResponse<Boolean> updateApp(@RequestBody AppUpdateRequest appUpdateRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(appUpdateRequest == null || appUpdateRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        boolean result = appService.updateApp(appUpdateRequest, request);
        return ResultUtils.success(result);
    }

    // 【用户】根据 id 删除自己的应用
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    @Operation(summary = "删除应用", description = "用户删除自己的应用")
    public BaseResponse<Boolean> deleteApp(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);
        boolean result = appService.deleteApp(deleteRequest.getId(), request);
        return ResultUtils.success(result);
    }

    // 【用户】根据 id 查看应用详情
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    @Operation(summary = "获取应用详情", description = "用户根据ID查看自己的应用详情")
    public BaseResponse<App> getAppById(@Parameter(description = "应用ID", required = true) long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        App app = appService.getAppById(id, request);
        return ResultUtils.success(app);
    }

    // 【用户】分页查询自己的应用列表（支持根据名称查询，每页最多 20 个）
    @PostMapping("/list/my/page")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    @Operation(summary = "查询我的应用列表", description = "用户分页查询自己的应用列表，支持根据名称查询，每页最多20个")
    public BaseResponse<Page<App>> listMyAppByPage(@RequestBody AppQueryRequest appQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(appQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Page<App> appPage = appService.listMyAppByPage(appQueryRequest, request);
        return ResultUtils.success(appPage);
    }

    // 【用户】分页查询精选的应用列表（支持根据名称查询，每页最多 20 个）
    @PostMapping("/list/featured/page")
    @Operation(summary = "查询精选应用列表", description = "分页查询精选的应用列表，支持根据名称查询，每页最多20个")
    public BaseResponse<Page<App>> listFeaturedAppByPage(@RequestBody AppQueryRequest appQueryRequest) {
        ThrowUtils.throwIf(appQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Page<App> appPage = appService.listFeaturedAppByPage(appQueryRequest);
        return ResultUtils.success(appPage);
    }

    // 【管理员】根据 id 删除任意应用
    @PostMapping("/admin/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "管理员删除应用", description = "管理员根据ID删除任意应用")
    public BaseResponse<Boolean> adminDeleteApp(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);
        boolean result = appService.adminDeleteApp(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    // 【管理员】根据 id 更新任意应用（支持更新应用名称、应用封面、优先级）
    @PostMapping("/admin/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "管理员更新应用", description = "管理员更新任意应用信息，支持更新应用名称、应用封面、优先级")
    public BaseResponse<Boolean> adminUpdateApp(@RequestBody AppUpdateRequest appUpdateRequest) {
        ThrowUtils.throwIf(appUpdateRequest == null || appUpdateRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        boolean result = appService.adminUpdateApp(appUpdateRequest);
        return ResultUtils.success(result);
    }

    // 【管理员】分页查询应用列表（支持根据除时间外的任何字段查询，每页数量不限）
    @PostMapping("/admin/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "管理员查询应用列表", description = "管理员分页查询所有应用列表，支持根据除时间外的任何字段查询，每页数量不限")
    public BaseResponse<Page<App>> adminListAppByPage(@RequestBody AppQueryRequest appQueryRequest) {
        ThrowUtils.throwIf(appQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Page<App> appPage = appService.adminListAppByPage(appQueryRequest);
        return ResultUtils.success(appPage);
    }

    // 【管理员】根据 id 查看应用详情
    @GetMapping("/admin/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "管理员获取应用详情", description = "管理员根据ID查看任意应用详情")
    public BaseResponse<App> adminGetAppById(@Parameter(description = "应用ID", required = true) long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        App app = appService.adminGetAppById(id);
        return ResultUtils.success(app);
    }

    @GetMapping(value="/chat/gen/code",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "生成代码", description = "通过对话方式生成代码，流式返回结果")
    public Flux<ServerSentEvent<String>> chatToGenCode(@Parameter(description = "生成代码的描述", required = true) @RequestParam String message,  @Parameter(description = "应用ID", required = true) @RequestParam Long appId, HttpServletRequest request) {
        //参数校验
        ThrowUtils.throwIf(appId==null || appId<0, ErrorCode.PARAMS_ERROR,"应用ID无效");
        ThrowUtils.throwIf(StrUtil.isBlank(message), ErrorCode.PARAMS_ERROR,"请输入生成代码的描述");
        //获取当前用户
        User loginUser = userService.getLoginUser(request);
        //调用服务生成代码（流式）
        Flux<String> contentFlux = appService.chatToGenCode(appId, message, loginUser);
        return contentFlux.map(chunk->{
            //将内容包装JSON对象
            Map<String, String> wrapper = Map.of("d", chunk);
            String jsonData = JSONUtil.toJsonStr(wrapper);
            return ServerSentEvent.<String>builder()
                    .data(jsonData)
                    .build();
        })
                .concatWith(Mono.just(
                        //发送结束事件
                        ServerSentEvent.<String>builder()
                                .event("done")
                                .data("")
                                .build()
                ));
    }


    /**
     * 应用部署
     *
     * @param appDeployRequest 部署请求
     * @param request          请求
     * @return 部署 URL
     */
    @PostMapping("/deploy")
    @Operation(summary = "部署应用", description = "部署应用并返回部署 URL")
    public BaseResponse<String> deployApp(@RequestBody AppDeployRequest appDeployRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(appDeployRequest == null, ErrorCode.PARAMS_ERROR);
        Long appId = appDeployRequest.getAppId();
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用 ID 不能为空");
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        // 调用服务部署应用
        String deployUrl = appService.deployApp(appId, loginUser);
        return ResultUtils.success(deployUrl);
    }



    /**
     * 下载应用代码
     *
     * @param appId    应用ID
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("/download/{appId}")
    @Operation(summary = "下载代码", description = "下载代码并返回代码压缩包.zip")
    public void downloadAppCode(@PathVariable Long appId,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        // 1. 基础校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用ID无效");
        // 2. 查询应用信息
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        // 3. 权限校验：只有应用创建者可以下载代码
        User loginUser = userService.getLoginUser(request);
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限下载该应用代码");
        }
        // 4. 构建应用代码目录路径（生成目录，非部署目录）
        String codeGenType = app.getCodeGenType();
        String sourceDirName = codeGenType + "_" + appId;
        String sourceDirPath = AppConstant.CODE_OUTPUT_ROOT_DIR + File.separator + sourceDirName;
        // 5. 检查代码目录是否存在
        File sourceDir = new File(sourceDirPath);
        ThrowUtils.throwIf(!sourceDir.exists() || !sourceDir.isDirectory(),
                ErrorCode.NOT_FOUND_ERROR, "应用代码不存在，请先生成代码");
        // 6. 生成下载文件名（不建议添加中文内容）
        String downloadFileName = String.valueOf(appId);
        // 7. 调用通用下载服务
        projectDownloadService.downloadProjectAsZip(sourceDirPath, downloadFileName, response);
    }



}
