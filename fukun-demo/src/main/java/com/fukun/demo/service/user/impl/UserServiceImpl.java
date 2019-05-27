package com.fukun.demo.service.user.impl;

import com.fukun.commons.service.impl.BaseMySqlCrudServiceImpl;
import com.fukun.demo.mapper.UserMapper;
import com.fukun.demo.service.user.UserService;
import com.fukun.user.model.po.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户服务接口实现
 *
 * @author tangyifei
 * @since 2019-5-24 15:33:14
 */
@Service
public class UserServiceImpl extends BaseMySqlCrudServiceImpl<User, String> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getUserCredential(String id) {
        return userMapper.getUserCredential(id);
    }
}
