package com.fehead.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fehead.community.entities.Club;
import com.fehead.community.entities.User;
import com.fehead.community.error.BusinessException;
import com.fehead.community.error.EmBusinessError;
import com.fehead.community.mapper.ClubMapper;
import com.fehead.community.mapper.UserMapper;
import com.fehead.community.service.IUserService;
import com.fehead.community.view.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private ClubMapper clubMapper;
    @Override
    public void insert(User user) {
        LambdaUpdateWrapper<User> userLambdaUpdateWrapper=new UpdateWrapper().lambda();
        userLambdaUpdateWrapper.eq(User::getUserOpenid,user.getUserOpenid());

        userMapper.update(user,userLambdaUpdateWrapper);
    }

    @Override
    public Integer userLogin(User user) {
        return userMapper.insert(user);
    }

    @Override
    public User selectUser(String openId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("user_openid",openId);
        User user = userMapper.selectOne(queryWrapper);
        if(user==null) return null;
        else return user;
    }

    @Override
    public UserVO selectUserVO(User user) {
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(user,userVO);
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.select("club_id").eq("club_creater_id",user.getUserId());
        //查找社团
        Club club=clubMapper.selectOne(queryWrapper);
        if(club==null){
            userVO.setIsCreateClub(-1);
        }else{
            userVO.setIsCreateClub(club.getClubId());
        }
        if((user.getUserPhone()==null||user.getUserPhone().length()==0) ||
                (user.getUserStudentNumber()==null||user.getUserStudentNumber().length()==0)||
                (user.getUserClass()==null||user.getUserClass().length()==0)||
                ( user.getUserInstitute()==null|| user.getUserInstitute().length()==0)){
            userVO.setIsComplete(0);//0表示没有完善
            log.info("用户信息未完善");
        }else {
            userVO.setIsComplete(1);//表示已经完善信息了
            log.info("用户信息已经完善");
        }
        //查看此人是否需要完善信息
        return userVO;
    }

    @Override
    public Integer updateUser(User user) {
        LambdaUpdateWrapper<User> updateWrapper=new UpdateWrapper().lambda();
        updateWrapper.eq(User::getUserOpenid,user.getUserOpenid());
        user.setUserName(null); //定时更新
        return userMapper.update(user, updateWrapper);
    }

    @Override
    public Integer updateUserByUserId(User user) {
        LambdaUpdateWrapper<User> updateWrapper=new UpdateWrapper().lambda();
        updateWrapper.eq(User::getUserId,user.getUserId());
        return userMapper.update(user, updateWrapper);
    }



    //查找用户通过UserId
    @Override
    public User selectUserById(Integer userId) throws BusinessException {
        User user=new User();
        try {
            user=userMapper.selectById(userId);
            if(user==null){
                log.info("没有找到该用户");
                throw new BusinessException(EmBusinessError.DATA_SELECT_ERROR,"没有找到该用户");
            }
        }catch (Exception e){
            log.info("没有找到该用户");
            throw new BusinessException(EmBusinessError.DATA_SELECT_ERROR,"没有找到该用户");
        }
        return user;
    }

}
