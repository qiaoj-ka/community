package com.fehead.community.controller;


import com.fehead.community.error.BusinessException;
import com.fehead.community.error.EmBusinessError;
import com.fehead.community.model.ActivityModel;
import com.fehead.community.other.Constants;
import com.fehead.community.response.CommonReturnType;
import com.fehead.community.service.IActivityService;
import com.fehead.community.service.IActivityUserService;
import com.fehead.community.utility.RedisUtil;
import com.fehead.community.view.ActivityVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ktoking
 * @since 2020-04-03
 */
@RestController
@Slf4j
public class ActivityUserController extends BaseController {


    @Autowired
    private IActivityService iActivityService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 活动id,该活动人数是否已经满了
     */
    private static ConcurrentHashMap<Integer,Boolean> activityNumber=new ConcurrentHashMap<>();
    /**
     * 初始化缓存的一种方式，开机就将数据加入缓存
     */
    @PostConstruct
    public void init() throws BusinessException {
        //获取首页的活动
        List<ActivityModel> activityModels=iActivityService.getAcitivity(1);
        for (ActivityModel am:activityModels){
            //参加活动总人剩余人数
            if(!stringRedisTemplate.hasKey(Constants.REDIS_ACTIVITY_NUMBEROFREMAINING+am.getActivityId())){
                Integer count=am.getActivityPeople()-iActivityUserService.getActivityNumber(am.getActivityId());//获取人数
                stringRedisTemplate.opsForValue().set(Constants.REDIS_ACTIVITY_NUMBEROFREMAINING+am.getActivityId(),count.toString());
                System.out.println("活动id为"+am.getActivityId()+" "+redisUtil.get(Constants.REDIS_ACTIVITY_NUMBEROFREMAINING+am.getActivityId()).toString());
            }

        }
    }

    /**
     * 加入活动这里开始做缓存
     */
    @Resource
    private IActivityUserService iActivityUserService;
    @PostMapping(value = "/add/activity")
    public CommonReturnType addActivity(@RequestParam(value = "userId")Integer usrId,
                                        @RequestParam(value = "activityId")Integer activityId) throws BusinessException {
        //用这个避免redis一下进入多个线程，已将将redis减到几百才能返回人数已满的情况
        if(activityNumber.get(activityId)!=null){
            throw new BusinessException(EmBusinessError.DATA_INSERT_ERROR,"活动人数已满");
        }
        //将人数减1
        System.out.println(redisUtil.get(Constants.REDIS_ACTIVITY_NUMBEROFREMAINING+activityId).toString());
        String key=Constants.REDIS_ACTIVITY_NUMBEROFREMAINING+activityId;
        if(redisUtil.hasKey(key)){
            System.out.println(redisUtil.get(Constants.REDIS_ACTIVITY_NUMBEROFREMAINING+activityId).toString());
            redisUtil.decr(key,1);
        }
//        Integer number=Integer.parseInt(redisUtil.get(Constants.REDIS_ACTIVITY_NUMBEROFREMAINING+activityId).toString());
//        System.out.println(redisUtil.get(Constants.REDIS_ACTIVITY_NUMBEROFREMAINING+activityId).toString());
        Integer number=Integer.parseInt(redisUtil.get(key).toString());
        System.out.println(number);
        if(number<0){
            activityNumber.put(activityId,true);//表示人数已经满了
            throw new BusinessException(EmBusinessError.DATA_INSERT_ERROR,"活动人数已满");
        }
        try {
            iActivityUserService.addActivity(activityId,usrId);
        }catch (Exception e){
            //如果没有插入成功，要进行双写一致性，对于之前的进行还原
             redisUtil.incr(Constants.REDIS_ACTIVITY_NUMBEROFREMAINING+activityId,1);
             if(activityNumber.get(activityId)!=null){
                 activityNumber.remove(activityId);
             }
             throw e;
        }
        return CommonReturnType.creat("你已经成功加入");
    }

    @DeleteMapping(value = "/delete/my/activity")
    public CommonReturnType deleteMyActivity(@RequestParam(value = "userId")Integer userId,
                                             @RequestParam(value = "activityId")Integer activityId) throws BusinessException {
        iActivityUserService.deleteActivity(activityId,userId);
        return CommonReturnType.creat("已退出该活动");
    }
}

