package com.zck.aicodemother.service;

import com.mybatisflex.core.service.IService;
import com.zck.aicodemother.model.entity.User;

/**
 * 用户 服务层。
 *
 * @author 赵承康
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword,String checkPassword);
}
