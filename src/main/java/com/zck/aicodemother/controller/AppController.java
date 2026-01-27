package com.zck.aicodemother.controller;

import com.mybatisflex.core.paginate.Page;
import com.zck.aicodemother.annotation.AuthCheck;
import com.zck.aicodemother.common.BaseResponse;
import com.zck.aicodemother.common.DeleteRequest;
import com.zck.aicodemother.common.ResultUtils;
import com.zck.aicodemother.constant.UserConstant;
import com.zck.aicodemother.exception.ErrorCode;
import com.zck.aicodemother.exception.ThrowUtils;
import com.zck.aicodemother.model.dto.app.AppAddRequest;

import com.zck.aicodemother.model.dto.app.AppQueryRequest;
import com.zck.aicodemother.model.dto.app.AppUpdateRequest;
import com.zck.aicodemother.model.entity.App;
import com.zck.aicodemother.service.AppService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * 应用 控制层。
 *
 * @author 赵承康
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @Resource
    private AppService appService;

    // 【用户】创建应用（须填写 initPrompt）
    @PostMapping("/create")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Long> createApp(@RequestBody AppAddRequest addAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(addAddRequest == null, ErrorCode.PARAMS_ERROR);
        long result = appService.createApp(addAddRequest, request);
        return ResultUtils.success(result);
    }

    // 【用户】根据 id 修改自己的应用（目前只支持修改应用名称）
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Boolean> updateApp(@RequestBody AppUpdateRequest appUpdateRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(appUpdateRequest == null || appUpdateRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        boolean result = appService.updateApp(appUpdateRequest, request);
        return ResultUtils.success(result);
    }

    // 【用户】根据 id 删除自己的应用
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Boolean> deleteApp(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);
        boolean result = appService.deleteApp(deleteRequest.getId(), request);
        return ResultUtils.success(result);
    }

    // 【用户】根据 id 查看应用详情
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<App> getAppById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        App app = appService.getAppById(id, request);
        return ResultUtils.success(app);
    }

    // 【用户】分页查询自己的应用列表（支持根据名称查询，每页最多 20 个）
    @PostMapping("/list/my/page")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Page<App>> listMyAppByPage(@RequestBody AppQueryRequest appQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(appQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Page<App> appPage = appService.listMyAppByPage(appQueryRequest, request);
        return ResultUtils.success(appPage);
    }

    // 【用户】分页查询精选的应用列表（支持根据名称查询，每页最多 20 个）
    @PostMapping("/list/featured/page")
    public BaseResponse<Page<App>> listFeaturedAppByPage(@RequestBody AppQueryRequest appQueryRequest) {
        ThrowUtils.throwIf(appQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Page<App> appPage = appService.listFeaturedAppByPage(appQueryRequest);
        return ResultUtils.success(appPage);
    }

    // 【管理员】根据 id 删除任意应用
    @PostMapping("/admin/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> adminDeleteApp(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);
        boolean result = appService.adminDeleteApp(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    // 【管理员】根据 id 更新任意应用（支持更新应用名称、应用封面、优先级）
    @PostMapping("/admin/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> adminUpdateApp(@RequestBody AppUpdateRequest appUpdateRequest) {
        ThrowUtils.throwIf(appUpdateRequest == null || appUpdateRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        boolean result = appService.adminUpdateApp(appUpdateRequest);
        return ResultUtils.success(result);
    }

    // 【管理员】分页查询应用列表（支持根据除时间外的任何字段查询，每页数量不限）
    @PostMapping("/admin/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<App>> adminListAppByPage(@RequestBody AppQueryRequest appQueryRequest) {
        ThrowUtils.throwIf(appQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Page<App> appPage = appService.adminListAppByPage(appQueryRequest);
        return ResultUtils.success(appPage);
    }

    // 【管理员】根据 id 查看应用详情
    @GetMapping("/admin/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<App> adminGetAppById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        App app = appService.adminGetAppById(id);
        return ResultUtils.success(app);
    }

}
