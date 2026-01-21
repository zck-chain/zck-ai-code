package com.zck.aicodemother.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zck.aicodemother.exception.BusinessException;
import com.zck.aicodemother.exception.ErrorCode;
import com.zck.aicodemother.model.dto.user.UserQueryRequest;
import com.zck.aicodemother.model.entity.User;
import com.zck.aicodemother.mapper.UserMapper;
import com.zck.aicodemother.model.enums.UserRoleEnum;
import com.zck.aicodemother.model.vo.LoginUserVO;
import com.zck.aicodemother.model.vo.UserVO;
import com.zck.aicodemother.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zck.aicodemother.constant.UserConstant.USER_LOGIN_STATE;

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

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request){
        //1.校验参数
        if (StringUtils.isAnyBlank(userAccount,userPassword))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        if (userAccount.length()<4)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        if (userPassword.length()<8)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        //2.加密
        String encryptPassword = getEncryptPassword(userPassword);
        //3.查询用户
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.mapper.selectOneByQuery(queryWrapper);
        //4.判断
        if (user==null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在或者密码错误");
        }
        //5.记录用户信息
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        //6.返回脱敏后的用户信息
        return this.getLoginUserVO(user);
    }


    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user==null) {
            return null;
        }
        return LoginUserVO.builder()
                .id(user.getId())
                .userAccount(user.getUserAccount())
                .userAvatar(user.getUserAvatar())
                .userProfile(user.getUserProfile())
                .userName(user.getUserName())
                .userRole(user.getUserRole())
                .createTime(user.getCreateTime())
                .build();
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        //从session中获取用户信息
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser==null || currentUser.getId()==null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //查询当前用户的所有信息
        Long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser==null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        //先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);

        if (userObj==null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        //清除session中的用户信息
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user==null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        return QueryWrapper.create()
                .eq("userRole", userRole)
                .like("userAccount", userAccount)
                .like("userName", userName)
                .like("userProfile", userProfile)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }


    @Override
    public String getEncryptPassword(String userPassword) {
        //盐值，混淆密码
       final String SALT="zck";
       return DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
    }
}
