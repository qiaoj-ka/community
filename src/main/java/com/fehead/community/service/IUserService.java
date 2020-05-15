package com.fehead.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fehead.community.entities.User;
import com.fehead.community.error.BusinessException;
import com.fehead.community.view.UserVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ktoking
 * @since 2020-04-03
 */
public interface IUserService extends IService<User> {
    public void insert(User user);
    public Integer userLogin(User user);
    public User selectUser(String openId);
    public UserVO selectUserVO(User user);
    public Integer updateUser(User user);
    public Integer updateUserByUserId(User user);
    User selectUserById(Integer userId) throws BusinessException;
}
