package com.zck.aicodemother.controller;

import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.paginate.Page;
import com.zck.aicodemother.annotation.AuthCheck;
import com.zck.aicodemother.common.BaseResponse;
import com.zck.aicodemother.common.DeleteRequest;
import com.zck.aicodemother.common.PageRequest;
import com.zck.aicodemother.common.ResultUtils;
import com.zck.aicodemother.constant.UserConstant;
import com.zck.aicodemother.exception.ErrorCode;
import com.zck.aicodemother.exception.ThrowUtils;
import com.zck.aicodemother.model.dto.user.*;
import com.zck.aicodemother.model.enums.UserRoleEnum;
import com.zck.aicodemother.model.vo.LoginUserVO;
import com.zck.aicodemother.model.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.zck.aicodemother.model.entity.User;
import com.zck.aicodemother.service.UserService;

import java.util.List;

/**
 * 用户 控制层。
 *
 * @author 赵承康
 */
@RestController
@RequestMapping("/user")
@Tag(name = "UserController", description = "提供用户注册、登录、查询等功能")
public class UserController {

    @Autowired
    private UserService userService;

    //注册用户
    @PostMapping("/register")
    @Operation(summary = "注册用户", description = "用户注册新账号")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录系统并获取登录信息")
    public BaseResponse<LoginUserVO> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    @GetMapping("/get/login")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    @Operation(summary = "获取当前登录用户", description = "获取当前登录用户的详细信息")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }

    @GetMapping("/logout")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    @Operation(summary = "用户登出", description = "用户登出系统")
    public BaseResponse<Boolean> logout(HttpServletRequest request) {
        ThrowUtils.throwIf(request==null,ErrorCode.PARAMS_ERROR);
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }


    /**
     * 创建用户（管理员操作）
     *
     * @param userAddRequest 用户
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "管理员创建用户", description = "管理员创建新用户，默认密码为123456")
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest==null,ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtil.copyProperties(userAddRequest, user);
        //默认密码
        final String DEFAULT_PASSWORD = "123456";
        String encryptPassword = userService.getEncryptPassword(DEFAULT_PASSWORD);
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result,ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 根据id获取用户（管理员）
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "管理员获取用户详情", description = "管理员根据ID获取用户详细信息")
    public BaseResponse<User> getUserById(@Parameter(description = "用户ID", required = true) long id) {
        ThrowUtils.throwIf(id<=0,ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user==null,ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    @GetMapping("/get/vo")
    @Operation(summary = "获取用户信息VO", description = "根据用户ID获取用户信息VO对象")
    public BaseResponse<UserVO> getUserInfoVo(@Parameter(description = "用户ID", required = true) long id) {
        BaseResponse<User> response = getUserById(id);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 根据主键删除用户。
     *
     * @param deleteRequest
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "管理员删除用户", description = "管理员根据ID删除用户")
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest==null || deleteRequest.getId()<=0,ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(userService.removeById(deleteRequest.getId()));
    }

    /**
     * 根据主键更新用户。(管理员)
     *
     * @param userUpdateRequest 用户
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "管理员更新用户", description = "管理员根据ID更新用户信息")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        ThrowUtils.throwIf(userUpdateRequest==null || userUpdateRequest.getId()<=0,ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest,user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result,ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 分页获取用户封装列表（管理员）
     * @param userQueryRequest
     * @return
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "管理员分页查询用户", description = "管理员分页获取用户封装列表，支持数据脱敏")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest==null,ErrorCode.PARAMS_ERROR);
        int pageNum = userQueryRequest.getPageNum();
        int pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(Page.of(pageNum, pageSize),
                userService.getQueryWrapper(userQueryRequest));
        //数据脱敏
        Page<UserVO> userVOPage = new Page<>(pageNum,pageSize,userPage.getTotalRow());
        List<UserVO> userVOList = userService.getUserVOList(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }

}
