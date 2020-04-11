package com.fehead.community.service.impl;

import com.fehead.community.entities.User;
import com.fehead.community.mapper.UserMapper;
import com.fehead.community.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ktoking
 * @since 2020-04-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;
    @Override
    public void insert(User user) {
        userMapper.insert(user);
    }
}
