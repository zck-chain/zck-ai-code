package com.zck.aicodemother.service.impl;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zck.aicodemother.exception.BusinessException;
import com.zck.aicodemother.exception.ErrorCode;
import com.zck.aicodemother.model.entity.User;
import com.zck.aicodemother.mapper.UserMapper;
import com.zck.aicodemother.model.enums.UserRoleEnum;
import com.zck.aicodemother.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * 用户 服务层实现。
 *
 * @author 赵承康
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService {

    @Resource
    private UserMapper userMapper;
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.参数校验
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if (userAccount.length()<=4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        }
        if (userPassword.length()<8 || checkPassword.length()<8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度不能小于8位");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不一致");
        }
        //2.查询账户是否已存在
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("userAccount",userAccount);
        long count = userMapper.selectCountByQuery(queryWrapper);
        if (count>0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号已存在");
        }
        //3.加密密码
        String encryptPassword = getEncryptPassword(userPassword);
        //4.插入数据
        User user = User.builder()
                .userAccount(userAccount)
                .userName("默认")
                .userPassword(encryptPassword)
                .userRole(UserRoleEnum.USER.getValue())
                .build();

        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"用户注册失败,数据库错误");
        }
        //5.返回结果

        return user.getId();
    }

    private String getEncryptPassword(String userPassword) {
        //盐值，混淆密码
       final String SALT="zck";
       return DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
    }
}
