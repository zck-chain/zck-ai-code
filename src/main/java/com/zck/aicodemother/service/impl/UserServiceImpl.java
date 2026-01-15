package com.zck.aicodemother.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zck.aicodemother.model.entity.User;
import com.zck.aicodemother.mapper.UserMapper;
import com.zck.aicodemother.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户 服务层实现。
 *
 * @author 赵承康
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService {

}
