package com.fehead.community.controller;


import com.fehead.community.error.BusinessException;
import com.fehead.community.response.CommonReturnType;
import com.fehead.community.service.IActivityUserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ktoking
 * @since 2020-04-03
 */
@RestController
public class ActivityUserController extends BaseController {

    @Resource
    private IActivityUserService iActivityUserService;
    @PostMapping(value = "/add/activity")
    public CommonReturnType addActivity(@RequestParam(value = "userId")Integer usrId,
                                        @RequestParam(value = "activityId")Integer activityId) throws BusinessException {
        iActivityUserService.addActivity(activityId,usrId);
        return CommonReturnType.creat("你已经成功加入");
    }

    @DeleteMapping(value = "/delete/my/activity")
    public CommonReturnType deleteMyActivity(@RequestParam(value = "userId")Integer userId,
                                             @RequestParam(value = "activityId")Integer activityId) throws BusinessException {
        iActivityUserService.deleteActivity(activityId,userId);
        return CommonReturnType.creat("已退出该活动");
    }
}

