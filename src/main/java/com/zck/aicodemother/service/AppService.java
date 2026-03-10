package com.zck.aicodemother.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.zck.aicodemother.model.dto.app.AppAddRequest;

import com.zck.aicodemother.model.dto.app.AppQueryRequest;
import com.zck.aicodemother.model.dto.app.AppUpdateRequest;
import com.zck.aicodemother.model.entity.App;
import com.zck.aicodemother.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import reactor.core.publisher.Flux;

/**
 * 应用 服务层。
 *
 * @author 赵承康
 */
public interface AppService extends IService<App> {

    /**
     * 创建应用
     *
     * @param appCreateRequest 创建应用请求对象
     * @param loginUser          user
     * @return 应用id
     */
    long createApp(AppAddRequest appCreateRequest, User loginUser);

    /**
     * 根据id修改自己的应用
     *
     * @param appUpdateRequest 更新应用请求对象
     * @param request          HttpServletRequest
     * @return 是否成功
     */
    boolean updateApp(AppUpdateRequest appUpdateRequest, HttpServletRequest request);

    /**
     * 根据id删除自己的应用
     *
     * @param appId   应用id
     * @param request HttpServletRequest
     * @return 是否成功
     */
    boolean deleteApp(long appId, HttpServletRequest request);

    /**
     * 根据id查看应用详情
     *
     * @param appId   应用id
     * @param request HttpServletRequest
     * @return 应用详情
     */
    App getAppById(long appId, HttpServletRequest request);

    /**
     * 分页查询自己的应用列表
     *
     * @param appQueryRequest 查询应用请求对象
     * @param request         HttpServletRequest
     * @return 应用列表
     */
    com.mybatisflex.core.paginate.Page<App> listMyAppByPage(AppQueryRequest appQueryRequest, HttpServletRequest request);

    /**
     * 分页查询精选的应用列表
     *
     * @param appQueryRequest 查询应用请求对象
     * @return 应用列表
     */
    com.mybatisflex.core.paginate.Page<App> listFeaturedAppByPage(AppQueryRequest appQueryRequest);

    /**
     * 管理员：根据id删除任意应用
     *
     * @param appId 应用id
     * @return 是否成功
     */
    boolean adminDeleteApp(long appId);

    /**
     * 管理员：根据id更新任意应用
     *
     * @param appUpdateRequest 更新应用请求对象
     * @return 是否成功
     */
    boolean adminUpdateApp(AppUpdateRequest appUpdateRequest);

    /**
     * 管理员：分页查询应用列表
     *
     * @param appQueryRequest 查询应用请求对象
     * @return 应用列表
     */
    com.mybatisflex.core.paginate.Page<App> adminListAppByPage(AppQueryRequest appQueryRequest);

    /**
     * 管理员：根据id查看应用详情
     *
     * @param appId 应用id
     * @return 应用详情
     */
    App adminGetAppById(long appId);

    /**
     * 获取查询条件
     *
     * @param appQueryRequest 查询应用请求对象
     * @return QueryWrapper
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    Flux<String> chatToGenCode(Long appId, String message, User loginUser);

    String deployApp(Long appId,User longinUser);

    void generateAppScreenshotAsync(Long appId, String appUrl);
}
