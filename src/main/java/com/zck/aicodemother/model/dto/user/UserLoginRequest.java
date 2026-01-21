package com.zck.aicodemother.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 赵承康
 * @date 2026/1/21
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;
}
