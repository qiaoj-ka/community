package com.fehead.community.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fehead.community.entities.Activity;
import com.fehead.community.entities.Club;
import com.fehead.community.error.BusinessException;
import com.fehead.community.error.EmBusinessError;
import com.fehead.community.model.ActivityModel;
import com.fehead.community.response.CommonReturnType;
import com.fehead.community.service.IActivityService;
import com.fehead.community.service.IActivityUserService;
import com.fehead.community.service.IClubService;
import com.fehead.community.view.ActivityVO;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ktoking
 * @since 2020-04-04
 */
//@Controller
//@RequestMapping("/activity")

@Slf4j
@RestController
public class ActivityController {

    @Resource
    private IActivityService iActivityService;
    @Resource
    private IActivityUserService iActivityUserService;

    @Autowired
    private IClubService iClubService;

    /**
     * 可上传图片、视频，只需在nginx配置中配置可识别的后缀
     */
    @PostMapping("/upload")
    public String pictureUpload(@RequestParam(value = "file") MultipartFile uploadFile) throws JsonProcessingException, BusinessException, JSchException, SftpException {
        long begin = System.currentTimeMillis();
        String json = "";

        Object result = iActivityService.uploadPicture(uploadFile);
        json = new ObjectMapper().writeValueAsString(result);

        long end = System.currentTimeMillis();
        log.info("任务结束，共耗时：[" + (end-begin) + "]毫秒");
        return json;
    }

    @PostMapping("/uploads")
    public Object picturesUpload(@RequestParam(value = "file") MultipartFile[] uploadFile) throws BusinessException, JSchException, SftpException {
        long begin = System.currentTimeMillis();
        Map<Object, Object> map = new HashMap<>(10);

        int count = 0;
        for (MultipartFile file : uploadFile) {
            Object result = iActivityService.uploadPicture(file);
            map.put(count, result);
            count++;
        }
        long end = System.currentTimeMillis();
        log.info("任务结束，共耗时：[" + (end-begin) + "]毫秒");
        return map;
    }

    //发布活动
    @PostMapping(value = "/publish/activity")
    public CommonReturnType createActivity(@RequestParam(value = "activityName")String activityName,
                                           @RequestParam(value = "activityDescribe")String activityDescribe,
                                           @RequestParam(value = "activityReward")String activityReward,
                                           @RequestParam(value = "activityPosition")String activityPosition,
                                           @RequestParam(value = "activityPeople")Integer activityPeople,
                                           @RequestParam(value = "activityStarttime")Long activityStarttime,
                                           @RequestParam(value = "activityEndtime")Long activityEndtime,
                                           @RequestParam(value = "activityCover")MultipartFile activityCover,
                                           @RequestParam(value = "activityCreater_id")Integer activityCreaterId,
                                           @RequestParam(value = "clubId")Integer clubId) throws JsonProcessingException, BusinessException, JSchException, SftpException {
        Object result = iActivityService.uploadPicture(activityCover);
        String cover = new ObjectMapper().writeValueAsString(result);
        String newLogo= cover.replace("\"", "");
        LocalDateTime time1=LocalDateTime.ofEpochSecond(activityStarttime/1000,0, ZoneOffset.ofHours(8));
        LocalDateTime time2=LocalDateTime.ofEpochSecond(activityEndtime/1000,0,ZoneOffset.ofHours(8));
        Activity activity=new Activity(null,activityName,time1,time2,activityDescribe,activityReward,activityPeople,clubId,0,activityCreaterId,newLogo,activityPosition);
        iActivityService.publishNewActivity(activity);
        return CommonReturnType.creat("success");
    }

    //获取活动详情
    @GetMapping("/get/activity")
    public CommonReturnType getActivity(@RequestParam(value = "activityId")Integer activityId,
                                        @RequestParam(value = "userId")Integer userId) throws BusinessException {
        ActivityVO activityVO=iActivityService.showActivity(activityId);
        Integer count=iActivityUserService.getActivityNumber(activityId);
        activityVO.setActivityNumber(activityVO.getActivityPeople()-count);//获取人数
        Integer state=iActivityUserService.getState(activityId,userId);
        if(state==2){
            activityVO.setAdd(true); //人是否加入这个活动
        }else {
            activityVO.setAdd(false);
        }
        return CommonReturnType.creat(activityVO);

    }
    //分页获取活动(简介)
    @GetMapping(value = "/get/activity/byPage")
    public CommonReturnType getActivityByPage(@RequestParam(value = "page")Integer page) throws BusinessException {
        List<ActivityModel> activityModels=iActivityService.getAcitivity(page);
        return CommonReturnType.creat(activityModels);
    }
    //模糊查询活动
    @GetMapping(value = "/search/activity")
    public CommonReturnType searchActivity(@RequestParam(value = "name") String name) throws BusinessException {
        List<ActivityModel> activityModels=iActivityService.searchActivity(name);
        return CommonReturnType.creat(activityModels);
    }

    //查找已经参加的活动
    @GetMapping(value = "/get/myActivity")
    public CommonReturnType getMyActivity(@RequestParam(value = "userId")Integer userId,
                                          @RequestParam(value = "state")Integer state) throws BusinessException {
        List<ActivityModel> activityModels=iActivityService.myActivity(userId,state);
        return CommonReturnType.creat(activityModels);
    }
    //社团管理人删除活动
    @PutMapping(value = "/delete/activity")
    public CommonReturnType deleteActivity(@RequestParam(value = "activityId")Integer activityId,
                                           @RequestParam(value = "userId")Integer userId) throws BusinessException {
        Club club=iClubService.isCreateIdhasCreate(userId);
        if(club==null){
            throw new BusinessException(EmBusinessError.DATA_SELECT_ERROR,"你没有权限删除活动");
        }
        iActivityService.updateState(activityId);
        return CommonReturnType.creat("删除成功");
    }
    //测试
    @GetMapping(value = "/hello")
    public String hello(){
        return "hello world";
    }
}

