package com.zck.aicode.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zck.aicode.ai.*;
import com.zck.aicode.constant.AppConstant;
import com.zck.aicode.core.AiCodeGeneratorFacade;
import com.zck.aicode.core.builder.VueProjectBuilder;
import com.zck.aicode.core.handler.StreamHandlerExecutor;
import com.zck.aicode.exception.BusinessException;
import com.zck.aicode.exception.ErrorCode;
import com.zck.aicode.exception.ThrowUtils;
import com.zck.aicode.model.dto.app.AppAddRequest;

import com.zck.aicode.model.dto.app.AppQueryRequest;
import com.zck.aicode.model.dto.app.AppUpdateRequest;
import com.zck.aicode.model.entity.App;
import com.zck.aicode.mapper.AppMapper;
import com.zck.aicode.model.entity.User;
import com.zck.aicode.model.enums.ChatHistoryMessageTypeEnum;
import com.zck.aicode.model.enums.CodeGenTypeEnum;
import com.zck.aicode.moitor.MonitorContext;
import com.zck.aicode.moitor.MonitorContextHolder;
import com.zck.aicode.service.AppService;
import com.zck.aicode.service.ChatHistoryService;
import com.zck.aicode.service.ScreenshotService;
import com.zck.aicode.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 应用 服务层实现。
 *
 * @author 赵承康
 */
@Service
@Slf4j
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {
    @Value("${code.deploy-host:http://localhost}")
    private String deployHost;
    @Resource
    private UserService userService;
    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;
    @Resource
    private ChatHistoryService chatHistoryService;
    @Resource
    private StreamHandlerExecutor streamHandlerExecutor;
    @Resource
    private VueProjectBuilder vueProjectBuilder;
    @Resource
    private ScreenshotService screenshotService;
    @Resource
    private AiCodeGenTypeRoutingServiceFactory aiCodeGenTypeRoutingServiceFactory;
    @Resource
    private AiCodeGenMessageServiceFactory aiCodeGenMessageServiceFactory;
    @Resource
    private AiCodeGenPromptServiceFactory aiCodeGenPromptServiceFactory;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long createApp(AppAddRequest appCreateRequest, User loginUser) {
        // 1. 参数校验
        String initPrompt = appCreateRequest.getInitPrompt();
        ThrowUtils.throwIf(StrUtil.isBlank(initPrompt), ErrorCode.PARAMS_ERROR, "初始化提示 prompt 为空");

        // 2. 创建应用
        App app = App.builder()
                .initPrompt(initPrompt)
                .userId(loginUser.getId())
                .build();

        //3.使用AI智能选择代码生成类型
        AiCodeGenTypeRoutingService routingService = aiCodeGenTypeRoutingServiceFactory.createAiCodeGenTypeRoutingService();
        CodeGenTypeEnum selectedCodeGenType = routingService.routeCodeGenType(initPrompt);
        app.setCodeGenType(selectedCodeGenType.getValue());

        //4.使用ai生成应用标题
        AiCodeGenMessageService aiCodeGenMessageService = aiCodeGenMessageServiceFactory.createAiCodeGenMessageService();
        String title = aiCodeGenMessageService.optimiseMessage(initPrompt);
        app.setAppName(title);
        //5.保存应用
        boolean saveResult = this.save(app);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建应用失败");
        }
        //6. 返回应用id
        return app.getId();
    }

    @Override
    public boolean updateApp(AppUpdateRequest appUpdateRequest, HttpServletRequest request) {
        // 1. 参数校验
        if (appUpdateRequest == null || appUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long appId = appUpdateRequest.getId();
        String appName = appUpdateRequest.getAppName();

        if (StrUtil.isBlank(appName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应用名称不能为空");
        }

        // 2. 获取当前登录用户
        User loginUser = userService.getLoginUser(request);

        // 3. 查询应用
        App oldApp = this.getById(appId);
        if (oldApp == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        }

        // 4. 验证权限（只能修改自己的应用）
        if (!oldApp.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限修改此应用");
        }


        // 5. 更新应用
        App app = App.builder()
                .id(appId)
                .appName(appName)
                .editTime(LocalDateTime.now())
                .build();
        boolean updateResult = this.updateById(app);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新应用失败");
        }

        // 6. 返回结果
        return true;
    }

    @Override
    public boolean deleteApp(long appId, HttpServletRequest request) {
        // 1. 参数校验
        if (appId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2. 获取当前登录用户
        User loginUser = userService.getLoginUser(request);

        // 3. 查询应用
        App app = this.getById(appId);
        if (app == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        }

        // 4. 验证权限（只能删除自己的应用）
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限删除此应用");
        }

        // 5. 删除应用
        boolean deleteResult = this.removeById(appId);
        if (!deleteResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除应用失败");
        }
        // 6. 返回结果
        return true;
    }

    @Override
    public App getAppById(long appId, HttpServletRequest request) {
        // 1. 参数校验
        if (appId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2. 获取当前登录用户
        User loginUser = userService.getLoginUser(request);

        // 3. 查询应用
        App app = this.getById(appId);
        if (app == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        }

        // 4. 验证权限（只能查看自己的应用）
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限查看此应用");
        }

        // 5. 返回应用
        return app;
    }

    @Override
    public Page<App> listMyAppByPage(AppQueryRequest appQueryRequest, HttpServletRequest request) {
        // 1. 参数校验
        if (appQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2. 获取当前登录用户
        User loginUser = userService.getLoginUser(request);

        // 3. 构建查询条件
        QueryWrapper queryWrapper = getQueryWrapper(appQueryRequest);
        queryWrapper.eq("userId", loginUser.getId());

        // 4. 分页查询
        int pageNum = appQueryRequest.getPageNum();
        int pageSize = Math.min(appQueryRequest.getPageSize(), 20); // 每页最多 20 个
        Page<App> appPage = this.page(Page.of(pageNum, pageSize), queryWrapper);

        // 5. 返回结果
        return appPage;
    }

    @Override
    public Page<App> listFeaturedAppByPage(AppQueryRequest appQueryRequest) {
        // 1. 参数校验
        if (appQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2. 构建查询条件（精选应用：优先级大于 0）
        QueryWrapper queryWrapper = getQueryWrapper(appQueryRequest);
        queryWrapper.gt("priority", 0);

        // 3. 分页查询
        int pageNum = appQueryRequest.getPageNum();
        int pageSize = Math.min(appQueryRequest.getPageSize(), 20); // 每页最多 20 个
        Page<App> appPage = this.page(Page.of(pageNum, pageSize), queryWrapper);

        // 4. 返回结果
        return appPage;
    }

    @Override
    public boolean adminDeleteApp(long appId) {
        // 1. 参数校验
        if (appId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2. 查询应用
        App app = this.getById(appId);
        if (app == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        }

        // 3. 删除应用
        boolean deleteResult = this.removeById(appId);
        if (!deleteResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除应用失败");
        }

        // 4. 返回结果
        return true;
    }

    @Override
    public boolean adminUpdateApp(AppUpdateRequest appUpdateRequest) {
        // 1. 参数校验
        if (appUpdateRequest == null || appUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long appId = appUpdateRequest.getId();

        // 2. 查询应用
        App app = this.getById(appId);
        if (app == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        }

        // 3. 更新应用
        if (StrUtil.isNotBlank(appUpdateRequest.getAppName())) {
            app.setAppName(appUpdateRequest.getAppName());
        }
        if (StrUtil.isNotBlank(appUpdateRequest.getCover())) {
            app.setCover(appUpdateRequest.getCover());
        }
        if (appUpdateRequest.getPriority() != null) {
            app.setPriority(appUpdateRequest.getPriority());
        }

        boolean updateResult = this.updateById(app);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新应用失败");
        }

        // 4. 返回结果
        return true;
    }

    @Override
    public Page<App> adminListAppByPage(AppQueryRequest appQueryRequest) {
        // 1. 参数校验
        if (appQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2. 构建查询条件
        QueryWrapper queryWrapper = getQueryWrapper(appQueryRequest);

        // 3. 分页查询（管理员无分页数量限制）
        int pageNum = appQueryRequest.getPageNum();
        int pageSize = appQueryRequest.getPageSize();
        Page<App> appPage = this.page(Page.of(pageNum, pageSize), queryWrapper);

        // 4. 返回结果
        return appPage;
    }

    @Override
    public App adminGetAppById(long appId) {
        // 1. 参数校验
        if (appId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2. 查询应用
        App app = this.getById(appId);
        if (app == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        }

        // 3. 返回应用
        return app;
    }

    @Override
    public QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest) {
        if (appQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String appName = appQueryRequest.getAppName();
        String codeGenType = appQueryRequest.getCodeGenType();
        Integer priority = appQueryRequest.getPriority();
        Long userId = appQueryRequest.getUserId();
        String sortField = appQueryRequest.getSortField();
        String sortOrder = appQueryRequest.getSortOrder();

        QueryWrapper queryWrapper = QueryWrapper.create();
        if (StrUtil.isNotBlank(appName)) {
            queryWrapper.like("appName", appName);
        }
        if (StrUtil.isNotBlank(codeGenType)) {
            queryWrapper.eq("codeGenType", codeGenType);
        }
        if (priority != null) {
            queryWrapper.eq("priority", priority);
        }
        if (userId != null) {
            queryWrapper.eq("userId", userId);
        }
        if (StrUtil.isNotBlank(sortField) && StrUtil.isNotBlank(sortOrder)) {
            queryWrapper.orderBy(sortField, "ascend".equals(sortOrder));
        }

        return queryWrapper;
    }

    @Override
    public Flux<String> chatToGenCode(Long appId, String message, User loginUser) {
        //1.参数校验
        ThrowUtils.throwIf(appId==null || appId<0, ErrorCode.PARAMS_ERROR,"appId不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(message), ErrorCode.PARAMS_ERROR,"用户消息不能为空");
        //2.查询应用
        App app = this.getById(appId);
        //3.验证用户是否有权限查看
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"没有权限查看该应用");
        }
        //4.获取应用的代码生成类型
        String codeGenType = app.getCodeGenType();
        CodeGenTypeEnum codeGenTypeEnum = CodeGenTypeEnum.getEnumByValue(codeGenType);
        if (codeGenTypeEnum==null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"不支持的生成类型");
        }
        //5.通过校验后，添加用户消息到对话历史
        chatHistoryService.addChatMessage(appId, message, ChatHistoryMessageTypeEnum.USER.getValue(),loginUser.getId());
        //6.设置监控上下文
        MonitorContextHolder.setContext(
                MonitorContext.builder()
                        .appId(appId.toString())
                        .userId(loginUser.getId().toString())
                        .build()
        );
        //7.调用ai生成代码
        Flux<String> codeStream = aiCodeGeneratorFacade.generateAndSaveCodeStream(message, codeGenTypeEnum, appId);
        //8.收集AI响应内容并在完成后记录到对话历史记录
        return streamHandlerExecutor.doExecute(codeStream, chatHistoryService ,appId, loginUser,codeGenTypeEnum)
                .doFinally(signalType -> {
                    //9.清除监控上下文
                    MonitorContextHolder.clearContext();
                });
    }

    @Override
    public String deployApp(Long appId, User longinUser) {
        //1.参数校验
        ThrowUtils.throwIf(appId==null || appId<0, ErrorCode.PARAMS_ERROR,"应用Id不能为空");
        ThrowUtils.throwIf(longinUser==null,ErrorCode.PARAMS_ERROR,"用户未登录");
        //2.查询应用信息
        App app = this.getById(appId);
        ThrowUtils.throwIf(app==null,ErrorCode.NOT_FOUND_ERROR,"应用不存在");
        //3.验证用户是否有权限部署
        if (!app.getUserId().equals(longinUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"没有权限部署该应用");
        }
        //4.检查是否有deployKey
        String deployKey = app.getDeployKey();
        if (StrUtil.isBlank(deployKey)) {
            //生成12位deployKey
            deployKey= RandomUtil.randomString(12);
        }
        //5.获取代码生成类型,构建源目录路径
        String codeGenType = app.getCodeGenType();
        String sourceDirName = codeGenType + "_" + appId;
        String sourceDirPath = AppConstant.CODE_OUTPUT_ROOT_DIR + File.separator + sourceDirName;
        //6.检查源目录是否存在
        File sourceDir = new File(sourceDirPath);
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"应用代码不存在,请先生成代码");
        }
        //7.复制文件到部署目录
        CodeGenTypeEnum codeGenTypeEnum = CodeGenTypeEnum.getEnumByValue(codeGenType);
        if (codeGenTypeEnum== CodeGenTypeEnum.VUE_PROJECT) {
            //vue项目需要构建
            boolean buildSuccess = vueProjectBuilder.buildProject(sourceDirPath);
            ThrowUtils.throwIf(!buildSuccess,ErrorCode.SYSTEM_ERROR,"Vue构建项目失败,请检查代码和依赖");
            //检查dist目录是否存在
            File distDir = new File(sourceDirPath, "dist");
            ThrowUtils.throwIf(!distDir.exists(),ErrorCode.SYSTEM_ERROR,"Vue构建完成但未生成dist目录");
            //将dist目录作为部署源
            sourceDir = distDir;
            log.info("vue项目构建完成,将dist目录作为部署源");
        }
        String deployDirPath = AppConstant.CODE_DEPLOY_ROOT_DIR + File.separator + deployKey;
        try {
            FileUtil.copyContent(sourceDir,new File(deployDirPath),true);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"部署文件失败"+e.getMessage());
        }
        //8.更新应用信息
        App updateApp = App.builder()
                .id(appId)
                .deployKey(deployKey)
                .deployedTime(LocalDateTime.now())
                .build();
        boolean updateResult = this.updateById(updateApp);
        ThrowUtils.throwIf(!updateResult,ErrorCode.OPERATION_ERROR,"更新应用部署信息失败");
        //9.返回可访问的url路径
        String appDeployUrl = String.format("%s/%s", deployHost, deployKey);
        //10.异步生成截图更新应用封面
        generateAppScreenshotAsync(appId,appDeployUrl);
        return appDeployUrl;
    }

    /**
     * 异步生成应用截图并更新封面
     *
     * @param appId  应用ID
     * @param appUrl 应用访问URL
     */
    @Override
    public void generateAppScreenshotAsync(Long appId, String appUrl) {
        // 使用虚拟线程异步执行
        Thread.startVirtualThread(() -> {
            // 调用截图服务生成截图并上传
            String screenshotUrl = screenshotService.generateAndUploadScreenshot(appUrl);
            // 更新应用封面字段
            App updateApp = new App();
            updateApp.setId(appId);
            updateApp.setCover(screenshotUrl);
            boolean updated = this.updateById(updateApp);
            ThrowUtils.throwIf(!updated, ErrorCode.OPERATION_ERROR, "更新应用封面字段失败");
        });
    }

    @Override
    public String getAiCodeGenPrompt(String prompt) {
        //1.参数校验
        ThrowUtils.throwIf(StrUtil.isBlank(prompt) , ErrorCode.PARAMS_ERROR,"应用提示词不能为空");
        AiCodeGenPromptService aiCodeGenPromptService = aiCodeGenPromptServiceFactory.createAiCodeGenPromptService();
        return aiCodeGenPromptService.getAiCodeGenPrompt(prompt);
    }


    /**
     * 删除应用时关联删除对话历史
     *
     * @param id 应用ID
     * @return 是否成功
     */
    @Override
    public boolean removeById(Serializable id) {
        if (id == null) {
            return false;
        }
        // 转换为 Long 类型
        Long appId = Long.valueOf(id.toString());
        if (appId <= 0) {
            return false;
        }
        // 先删除关联的对话历史
        try {
            chatHistoryService.deleteByAppId(appId);
        } catch (Exception e) {
            // 记录日志但不阻止应用删除
            log.error("删除应用关联对话历史失败: {}", e.getMessage());
        }
        // 删除应用
        return super.removeById(id);
    }

}
