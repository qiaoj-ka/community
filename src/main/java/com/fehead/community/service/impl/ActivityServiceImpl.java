package com.fehead.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fehead.community.entities.Activity;
import com.fehead.community.entities.ActivityUser;
import com.fehead.community.entities.Club;
import com.fehead.community.entities.User;
import com.fehead.community.error.BusinessException;
import com.fehead.community.error.EmBusinessError;
import com.fehead.community.mapper.ActivityMapper;
import com.fehead.community.mapper.ActivityUserMapper;
import com.fehead.community.mapper.ClubMapper;
import com.fehead.community.mapper.UserMapper;
import com.fehead.community.model.ActivityModel;
import com.fehead.community.service.IActivityService;
import com.fehead.community.utility.FtpUtil;
import com.fehead.community.utility.IDUtils;
import com.fehead.community.utility.RedisUtil;
import com.fehead.community.view.ActivityVO;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ktoking
 * @since 2020-04-04
 */
@Service
@Slf4j
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ClubMapper clubMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ActivityUserMapper activityUserMapper;
    @Override
    public void publishNewActivity(Activity activity) throws BusinessException {
        try {
            QueryWrapper<Club> queryWrapper=new QueryWrapper<>();
            queryWrapper.select("club_creater_id").eq("club_id",activity.getClubId());
            Club club=clubMapper.selectOne(queryWrapper);
            if(club.getClubCreaterId()==activity.getActivityCreaterId()){
                activityMapper.insert(activity);
            }else{
                throw new BusinessException(EmBusinessError.DATA_INSERT_ERROR,"你没有权限进行增加操作");
            }
        }catch (Exception e){
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
        }
    }

    @Resource
    private FtpUtil ftpUtil;

    public Object uploadPicture(MultipartFile uploadFile) throws BusinessException, JSchException, SftpException {
        //1.给上传的图片生成新的文件名
        //1.1获取原始文件名getAcitivity
        String oldName=uploadFile.getOriginalFilename();
        //1.2使用IDUtils工具类生成新的文件名，新的文件名=newName+文件后缀
        String newName= IDUtils.getImageName();
        assert oldName!=null;
        newName=newName+oldName.substring(oldName.lastIndexOf("."));
        //1.3生成文件在服务器端存储的子目录
        String filePath=new DateTime().toString("/yyyyMMdd/");

        //2.把图片上传到图片服务器
        //2.1获取上传的io流
        InputStream inputStream=null;
        try {
            inputStream=uploadFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //2.2调用FtpUtil工具进行上传
        return ftpUtil.putImages(inputStream,filePath,newName);
    }

    @Override
    public ActivityVO showActivity(Integer activityId) throws BusinessException {
        ActivityVO  activityVO=new ActivityVO();
        try {
            Activity activity=activityMapper.selectById(activityId);
            User user=userMapper.selectById(activity.getActivityCreaterId());
            QueryWrapper<Club> queryWrapper=new QueryWrapper<>();
            queryWrapper.select("club_name").eq("club_id",activity.getClubId());
            Club club=clubMapper.selectOne(queryWrapper);
            activityVO=tansferVO(activity,user,club);
            String hotkey=activity.getActivityId()+"hot";
            if(!redisUtil.hasKey(hotkey)){
                redisUtil.set(hotkey,0);
                activityVO.setHot("0");
            }else {
                redisUtil.incr(hotkey,1);
                activityVO.setHot(redisUtil.get(hotkey).toString());
            }
        }catch (Exception e){
            log.info("活动获取失败");
            throw new BusinessException(EmBusinessError.DATA_SELECT_ERROR,"活动获取失败");
        }
        return activityVO;
    }

    //分页获取活动
    @Override
    public List<ActivityModel> getAcitivity(Integer page) throws BusinessException{
        List<ActivityModel> list=new ArrayList<>();
        try {
            QueryWrapper queryWrapper1=new QueryWrapper();
            queryWrapper1.select("*").orderByDesc("activity_id");
            queryWrapper1.ne("activity_status",1);
            Page<Activity> page1=new Page<>(page,6);
            IPage<Activity> iPage= activityMapper.selectPage(page1,queryWrapper1);
            List<Activity> activities=iPage.getRecords();
            //需要判断是否过期，只返回不过期的
            list=getAcitivityModelList(activities,1);
        }catch (Exception e){
            log.info("获取活动失败");
            throw new BusinessException(EmBusinessError.DATA_SELECT_ERROR,"活动获取失败");
        }
        return list;
    }

    /**
     *
     * @param activities 传入需要转换的list
     * @param status 传入状态 0 代表不需要判断过期时间，1代表需要判断过期时间
     * @return
     */
    private List<ActivityModel> getAcitivityModelList(List<Activity> activities,Integer status){
        List<ActivityModel> list=new ArrayList<>();
        for (Activity act:activities){
            QueryWrapper<User> queryWrapper=new QueryWrapper<>();
            queryWrapper.select("user_avatar","user_name").eq("user_id",act.getActivityCreaterId());
            User user=userMapper.selectOne(queryWrapper);
            ActivityModel activityModel=transforActivityToModel(act,user);
            Duration duration=Duration.between(LocalDateTime.now(),act.getActivityEndtime());
            //判断其是否过期，如果过期将数据库字段修改为1 表示删除或者过期
            if((duration.toMillis()<0&&status==1)){
                LambdaUpdateWrapper<Activity> updateWrapper=new UpdateWrapper().lambda();
                updateWrapper.eq(Activity::getActivityId,act.getActivityId());
                act.setActivityStatus(1);
                activityMapper.update(act,updateWrapper);
            }else if(act.getActivityStatus()==1&&status==1){//表示已经删除的，也不加到
                log.info("活动已经删除了");
            } else if(activityModel!=null) {
                list.add(activityModel);
            }
        }
        return list;
    }
    //模糊查询，活动过期的不返回
    @Override
    public List<ActivityModel> searchActivity(String name) throws BusinessException {
        List<ActivityModel> list=new ArrayList<>();
        try {
             LambdaQueryWrapper<Activity> queryWrapper=new QueryWrapper().lambda();
             queryWrapper.like(Activity::getActivityName,name);
             queryWrapper.orderByDesc(Activity::getActivityId);
             List<Activity> activities=activityMapper.selectList(queryWrapper);
             //获取未过期的活动列表
             list=getAcitivityModelList(activities,1);
        }catch (Exception e){
             throw new BusinessException(EmBusinessError.DATA_SELECT_ERROR,"未找到活动，查询失败");
        }
        return list;
    }

    //在我的信息那块找到自己的所有活动信息
    @Override
    public List<ActivityModel> myActivity(Integer userId, Integer state) throws BusinessException {
        List<ActivityModel> list=new ArrayList<>();
        try {
            List<ActivityUser> activityId=new ArrayList<>();
            LambdaQueryWrapper<ActivityUser> queryWrapper=new QueryWrapper().lambda();
            queryWrapper.eq(ActivityUser::getUserId,userId).orderByDesc(ActivityUser::getActivityId);
            activityId=activityUserMapper.selectList(queryWrapper);

            List<Activity> activities=new ArrayList<>();
            List<Activity> activities1=new ArrayList<>();
            List<Activity> activities2=new ArrayList<>();
            for (ActivityUser a:activityId){
                Activity activity=activityMapper.selectById(a.getActivityId());
                Duration duration=Duration.between(LocalDateTime.now(),activity.getActivityEndtime());
                if(state==0&&duration.toMillis()>0){//表示所有未过期的
                    activities.add(activity);
                }
                if(state==1&&duration.toDays()<1){//表示所有即将过期的
                    activities1.add(activity);
                }else if(state==2&&duration.toMillis()<0||activity.getActivityStatus()==1){//表示已经过期或删除的
                    activities2.add(activity);
                }
            }
            //这里全部返回 不考虑是否过期
            if(state==0)list=getAcitivityModelList(activities,1); //考虑是否过期
            if(state==1)list=getAcitivityModelList(activities1,1);//考虑是否过期
            if(state==2)list=getAcitivityModelList(activities2,0);
        }catch (Exception e){
            log.info("获取活动失败");
            throw new BusinessException(EmBusinessError.DATA_SELECT_ERROR,"活动获取失败");
        }
        return list;
    }

    //进入社团首页，获取的所有活动，展示未过期的所有活动
    @Override
    public List<ActivityModel> getAllActivityByClubId(Integer clubId) {
        //通过clubId查找该社团下的所有活动
        LambdaQueryWrapper<Activity> queryWrapper=new QueryWrapper().lambda();
        queryWrapper.eq(Activity::getClubId,clubId).orderByDesc(Activity::getActivityId);
        List<Activity> activities=activityMapper.selectList(queryWrapper);
        //现实未过期的所有活动
        List<ActivityModel> activityModels=getAcitivityModelList(activities,1);
        return activityModels;
    }

    //加入社团活动
    @Override
    public void addActivity(Integer userId, Integer activityId) {

    }

    @Override
    public void updateState(Integer activityId) throws BusinessException {
        LambdaUpdateWrapper<Activity> queryWrapper=new UpdateWrapper().lambda();
        queryWrapper.eq(Activity::getActivityId,activityId);
        Activity activity=activityMapper.selectById(activityId);
        if(activity==null||activity.getActivityStatus()==1){
            log.info("该活动不存在");
            throw new BusinessException(EmBusinessError.DATA_SELECT_ERROR,"该活动不存在");
        }
        activity.setActivityStatus(1);
        activityMapper.update(activity,queryWrapper);
    }

    private ActivityVO tansferVO(Activity activity,User user,Club club){
        ActivityVO activityVO=new ActivityVO();
        BeanUtils.copyProperties(activity,activityVO);
        Long startTime=activity.getActivityStarttime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Long endTime=activity.getActivityEndtime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        activityVO.setActivityStartTime(startTime);
        activityVO.setActivityEndTime(endTime);
        activityVO.setUserName(user.getUserName());
        activityVO.setUserPhone(user.getUserPhone());
        activityVO.setActivityPeople(activity.getActivityPeople());
        activityVO.setClubName(club.getClubName());
        return activityVO;
    }

    private ActivityModel transforActivityToModel(Activity activity,User user){
        ActivityModel activityModel=new ActivityModel();
        BeanUtils.copyProperties(activity,activityModel);
        BeanUtils.copyProperties(user,activityModel);
//        //计算两者时间差
//        Duration duration=Duration.between(activity.getActivityStarttime(),LocalDateTime.now());
//        //当天的零点
//        LocalDateTime minTime=LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN);
//        Duration duration2=Duration.between(minTime,activity.getActivityStarttime());
//        if(duration2.toMinutes()>0){
//            activityModel.setTime("活动今天开始");
//        }else if(duration.toMillis()<0){//表示时间还没到
//            activityModel.setTime("距离活动开始还有："+duration.toDays()*-1+"天");
//        }else if (duration.toDays()==0){
//            activityModel.setTime("活动今天开始");
//        }else if(duration.toDays()>0&&duration.toDays()<30){
//            activityModel.setTime(duration.toDays()+"天前");
//        }else if(duration.toDays()>30){
//            activityModel.setTime(duration.toDays()/30+"个月前");
//        }
        //活动开始的当天0点
        LocalDateTime minTime=LocalDateTime.of(activity.getActivityStarttime().toLocalDate(), LocalTime.MIN);
        //现在的时间
        LocalDateTime now=LocalDateTime.now();
        //现在时间的0.00
        LocalDateTime nowMin=LocalDateTime.of(now.toLocalDate(), LocalTime.MIN);
        //表示活动0.00与开始时间做比较
        Duration duration2=Duration.between(minTime,now);
        //活动开始0.00和当现在0点
        Duration duration4=Duration.between(nowMin,minTime);
        //活动今天开始
        if(duration2.toMinutes()>0&&duration4.toDays()==0){
            activityModel.setTime("活动今天开始");
            //System.out.println("活动今天开始");
        }else if(duration4.toDays()<0){//表示时间还没到
            activityModel.setTime(duration4.toDays()*-1+"天前");
            //System.out.println(duration4.toDays()*-1+"天前");
        }else if(duration4.toDays()>0&&duration4.toDays()<30){
            activityModel.setTime("距离活动开始还有："+duration4.toDays()+"天");
           // System.out.println("距离活动开始还有："+duration4.toDays()+"天");
        }else if(duration4.toDays()>30){
           // System.out.println(duration4.toDays()/30+"个月前");
            activityModel.setTime(duration4.toDays()/30+"个月前");
        }
        Duration duration1=Duration.between(LocalDateTime.now(),activity.getActivityEndtime());
        if(duration1.toMillis()<0){
            activityModel.setTime("活动已经结束");
        }
        //获取hot值
        String hotkey=activity.getActivityId()+"hot";
        if(!redisUtil.hasKey(hotkey)){
            redisUtil.set(hotkey,0);
            activityModel.setHot("0");
        }else {
            activityModel.setHot(redisUtil.get(hotkey).toString());
        }
        return activityModel;
    }
}
