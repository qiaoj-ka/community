package com.fehead.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fehead.community.entities.Club;
import com.fehead.community.entities.ClubUser;
import com.fehead.community.error.BusinessException;
import com.fehead.community.error.EmBusinessError;
import com.fehead.community.mapper.ClubMapper;
import com.fehead.community.mapper.ClubUserMapper;
import com.fehead.community.service.IClubUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
public class ClubUserServiceImpl extends ServiceImpl<ClubUserMapper, ClubUser> implements IClubUserService {

    @Resource
    private ClubUserMapper clubUserMapper;
    @Resource
    private ClubMapper clubMapper;
    //加入社团
    @Override
    public void addClub(Integer userId, Integer clubId) throws BusinessException {
        LambdaQueryWrapper<ClubUser> queryWrapper=new QueryWrapper().lambda();
        queryWrapper.eq(ClubUser::getClubId,clubId).eq(ClubUser::getUserId,userId);
        ClubUser clubUser=clubUserMapper.selectOne(queryWrapper);
        LambdaQueryWrapper<Club> queryWrapper1=new QueryWrapper().lambda();
        queryWrapper1.select(Club::getClubCreaterId).eq(Club::getClubId,clubId);
        Club club=clubMapper.selectOne(queryWrapper1);
        if(club.getClubCreaterId()==userId){
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"你是社团负责人，已经加入该社团");
        }
        if(clubUser!=null){
            throw new BusinessException(EmBusinessError.DATA_INSERT_ERROR,"您已加入该社团");
        }
        try {
             ClubUser clubUser1=new ClubUser(null,clubId,userId);
             clubUserMapper.insert(clubUser1);
        }catch (Exception e){
             throw new BusinessException(EmBusinessError.DATA_INSERT_ERROR,"加入社团失败");
        }
    }

    @Override
    public List<ClubUser> clubs(Integer userId) {
        LambdaQueryWrapper<ClubUser> queryWrapper=new QueryWrapper().lambda();
        queryWrapper.select(ClubUser::getClubId).eq(ClubUser::getUserId,userId);
        List<ClubUser> list=clubUserMapper.selectList(queryWrapper);
        return list;
    }

    //获取社团下的所有用户id
    @Override
    public List<ClubUser> getClubUser(Integer clubId) throws BusinessException {
        List<ClubUser> list=new ArrayList<>();
        try {
            LambdaQueryWrapper<ClubUser> queryWrapper=new QueryWrapper().lambda();
            queryWrapper.select(ClubUser::getUserId).eq(ClubUser::getClubId,clubId);
            list=clubUserMapper.selectList(queryWrapper);
        }catch (Exception e){
            log.info("用户获取失败");
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"用户获取失败");
        }
        return list;
    }

    @Override
    public List<ClubUser> getAllUserByPage(Integer page,Integer clubId) throws BusinessException {
        List<ClubUser> clubUsers=new ArrayList<>();
        try {
            LambdaQueryWrapper<ClubUser> queryWrapper=new QueryWrapper().lambda();
            queryWrapper.eq(ClubUser::getClubId,clubId);
            Page<ClubUser> page1=new Page<>(page,7);
            IPage<ClubUser> iPage=clubUserMapper.selectPage(page1,queryWrapper);
            clubUsers=iPage.getRecords();
        }catch (Exception e){
            log.info("用户获取失败");
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"用户获取失败");
        }

        return clubUsers;
    }

    //退出社团
    public Integer quiteClub(Integer userId,Integer clubId) throws BusinessException {
        LambdaQueryWrapper<ClubUser> queryWrapper=new QueryWrapper().lambda();
        queryWrapper.eq(ClubUser::getClubId,clubId).eq(ClubUser::getUserId,userId);
        ClubUser clubUser = clubUserMapper.selectOne(queryWrapper);
        if(clubUser==null){
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"用户并没有加入这个社团");
        }else {
            return clubUserMapper.delete(queryWrapper);
        }
    }
}
