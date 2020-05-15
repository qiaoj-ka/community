package com.fehead.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fehead.community.entities.ClubUser;
import com.fehead.community.error.BusinessException;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ktoking
 * @since 2020-04-03
 */
public interface IClubUserService extends IService<ClubUser> {
    //加入社团
    void addClub(Integer userId,Integer clubId) throws BusinessException;
    //获取我加入的所有社团id
    List<ClubUser> clubs(Integer userId);
    //获取所有社团下的用户
    List<ClubUser> getClubUser(Integer clubId) throws BusinessException;
    //分页查找所有社团下的用户
    List<ClubUser> getAllUserByPage(Integer page,Integer clubId) throws BusinessException;
    //退出社团
    Integer quiteClub(Integer userId,Integer clubId) throws BusinessException;
}
