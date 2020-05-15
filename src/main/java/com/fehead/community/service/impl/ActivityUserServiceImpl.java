package com.fehead.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fehead.community.entities.Activity;
import com.fehead.community.entities.ActivityUser;
import com.fehead.community.error.BusinessException;
import com.fehead.community.error.EmBusinessError;
import com.fehead.community.mapper.ActivityMapper;
import com.fehead.community.mapper.ActivityUserMapper;
import com.fehead.community.service.IActivityUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;

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
public class ActivityUserServiceImpl extends ServiceImpl<ActivityUserMapper, ActivityUser> implements IActivityUserService {

    @Resource
    private ActivityUserMapper activityUserMapper;
    @Resource
    private ActivityMapper activityMapper;
    @Override
    public Integer getActivityNumber(Integer activityId) {
        QueryWrapper<ActivityUser> activityUserQueryWrapper=new QueryWrapper<>();
        activityUserQueryWrapper.eq("activity_id",activityId);
        Integer count=activityUserMapper.selectCount(activityUserQueryWrapper);
        return count;
    }

    @Override
    public Integer getState(Integer activityId, Integer userId) {
        Integer state=0;
        QueryWrapper<ActivityUser> queryWrapper1=new QueryWrapper<>();
        queryWrapper1.select("activity_id","user_id").eq("user_id",userId).eq("activity_id",activityId);
        ActivityUser activityUser=activityUserMapper.selectOne(queryWrapper1);
        if(activityUser!=null){
            state=2; //表示该用户已经参加过活动了
            log.info("该用户已经参加过活动了");
        }
        return state;
    }

    //加入活动
    @Override
    public void addActivity(Integer activityId, Integer userId) throws BusinessException {
        ActivityUser activityUser=new ActivityUser();
        activityUser.setActivityId(activityId);
        activityUser.setUserId(userId);
        LambdaQueryWrapper<Activity> queryWrapper=new QueryWrapper().lambda();
        queryWrapper.select(Activity::getActivityStarttime,Activity::getActivityEndtime,Activity::getActivityPeople).eq(Activity::getActivityId,activityId);
        Activity activity=activityMapper.selectOne(queryWrapper);
        Duration duration=Duration.between(activity.getActivityStarttime(), LocalDateTime.now());
        if(duration.toMillis()<0){
            throw new BusinessException(EmBusinessError.DATA_INSERT_ERROR,"活动还没开始");
        }
        Duration duration1=Duration.between(LocalDateTime.now(),activity.getActivityEndtime());
        if(duration1.toMillis()<0){
            throw new BusinessException(EmBusinessError.DATA_INSERT_ERROR,"活动已经结束");
        }
        //判断该用户是否已经参加过这个活动
        LambdaQueryWrapper<ActivityUser> queryWrapper1=new QueryWrapper().lambda();
        queryWrapper1.eq(ActivityUser::getActivityId,activityId).eq(ActivityUser::getUserId,userId);
        ActivityUser activityUser1=activityUserMapper.selectOne(queryWrapper1);
        if(activityUser1!=null){
            throw new BusinessException(EmBusinessError.DATA_INSERT_ERROR,"已经参加过该活动");
        }
        //判断这个活动人数是否已满
        LambdaQueryWrapper<ActivityUser> queryWrapper2=new QueryWrapper().lambda();
        queryWrapper2.eq(ActivityUser::getActivityId,activityId);
        Integer count=activityUserMapper.selectCount(queryWrapper2);
        if(count>=activity.getActivityPeople()){
            throw new BusinessException(EmBusinessError.DATA_INSERT_ERROR,"该活动人数已经满了");
        }
        try {
            activityUserMapper.insert(activityUser);
        }catch (Exception e){
            throw new BusinessException(EmBusinessError.DATA_INSERT_ERROR,"加入失败");
        }
    }

    //删除活动
    @Override
    public void deleteActivity(Integer activityId, Integer userId) throws BusinessException {
        LambdaQueryWrapper<Activity> queryWrapper=new QueryWrapper().lambda();
        queryWrapper.select(Activity::getActivityEndtime).eq(Activity::getActivityId,activityId);
        Activity activity=activityMapper.selectOne(queryWrapper);
        Duration duration=Duration.between(LocalDateTime.now(),activity.getActivityEndtime());
        if(duration.toMillis()<0){
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"活动已经开始，不能删除");
        }
        try {
            LambdaQueryWrapper<ActivityUser> queryWrapper1=new QueryWrapper().lambda();
            queryWrapper1.eq(ActivityUser::getActivityId,activity).eq(ActivityUser::getUserId,userId);
            activityUserMapper.delete(queryWrapper1);
        }catch (Exception e){
            log.info("退出活动失败");
            throw new BusinessException(EmBusinessError.DATA_DELETE_ERROR,"未能删除成功");
        }

    }

}
