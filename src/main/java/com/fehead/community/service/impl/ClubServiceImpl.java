package com.fehead.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fehead.community.entities.Club;
import com.fehead.community.entities.ClubUser;
import com.fehead.community.error.BusinessException;
import com.fehead.community.error.EmBusinessError;
import com.fehead.community.mapper.ClubMapper;
import com.fehead.community.mapper.ClubUserMapper;
import com.fehead.community.service.IClubService;
import com.fehead.community.service.IClubUserService;
import com.fehead.community.view.ClubVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class ClubServiceImpl extends ServiceImpl<ClubMapper, Club> implements IClubService {

    @Resource
    private ClubMapper clubMapper;
    @Resource
    private ClubUserMapper clubUserMapper;

    @Override
    @Transactional //事务性
    public Integer createClub(ClubVO clubVo) throws BusinessException {
        clubVo.setClubEstablishTime(LocalDateTime.now());
        //判断该社团创建人以前是否创建过社团
        QueryWrapper<Club> queryWrapper=new QueryWrapper<Club>();
        queryWrapper.eq("club_creater_id",clubVo.getClubCreaterId());
        Integer count = clubMapper.selectCount(queryWrapper);
        if(count>0){
            log.info("该用户已经建立了社团，不能在创建社团");
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"该用户已经建立了社团");
        }
        //判断该社团名是否已经被注册
        QueryWrapper<Club> queryWrapper1=new QueryWrapper<Club>();
        queryWrapper1.eq("club_name",clubVo.getClubName());
        Integer count1=clubMapper.selectCount(queryWrapper1);
        if(count1>0){
            log.info("该社团名已经被注册");
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"该社团名已被注册");
        }
        Club club=transforVO(clubVo);
        try {
            club.setClubStatus(0);
            int rtValue = clubMapper.insert(club);
        }catch (Exception e){
            throw new BusinessException(EmBusinessError.DATA_INSERT_ERROR);
        }
        return club.getClubId();
    }

    //判断该用户是否创建过社团并且返回这个社团的信息
    @Override
    public Club isCreateIdhasCreate(Integer createId) throws BusinessException {
        Map<String,Object> cloumMap=new HashMap<>();
        cloumMap.put("club_creater_id",createId);
        List<Club> clubList=clubMapper.selectByMap(cloumMap);
        if(clubList.size()==0){
            log.info("没有找到该用户创建的社团");
            throw new BusinessException(EmBusinessError.DATA_SELECT_ERROR,"没有找到该用户创建的社团");
        }
        return clubList.get(0);
    }

    @Override
    public List<Club> getClub(String type) {
        QueryWrapper<Club> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("club_id","club_name");
        queryWrapper.eq("club_type",type);
        List<Club> list=clubMapper.selectList(queryWrapper);
        return list;
    }

    //通过id查找社团
    @Override
    public Club getClubById(Integer clubId) {
        Club club=new Club();
        try {
            club=clubMapper.selectById(clubId);
        }catch (Exception e){
             e.printStackTrace();
        }
        return club;
    }

    @Resource
    private IClubUserService iClubUserService;
    //获取我所有加入的社团
    @Override
    public List<Club> getMyClub(Integer userId) throws BusinessException {
        List<Club> list=new ArrayList<>();
        //获取用户加入所有社团的id
        List<ClubUser> clubUsers=iClubUserService.clubs(userId);
        //通过id查询各个社团信息
        for (ClubUser c:clubUsers){
            Club club=getClubById(c.getClubId());
            if(club!=null){
                list.add(club);
            }else {
                throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"获取社团失败");
            }
        }
        return list;
    }

    @Override
    public Club updateClubInfo(Club club) throws BusinessException {
        if(club==null){
            throw new BusinessException(EmBusinessError.DATA_UPDATE_ERROR,"不能为空值");
        }
        LambdaUpdateWrapper<Club> updateWrapper=new UpdateWrapper().lambda();
        updateWrapper.eq(Club::getClubId,club.getClubId());
        clubMapper.update(club,updateWrapper);
        return club;
    }

    private Club transforVO(ClubVO clubVO){
        if(clubVO==null){
            return null;
        }
        Club club=new Club();
        BeanUtils.copyProperties(clubVO,club);
        return club;
    }
}
