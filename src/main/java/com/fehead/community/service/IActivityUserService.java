package com.fehead.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fehead.community.entities.ActivityUser;
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
public interface IActivityUserService extends IService<ActivityUser> {
    public Integer getActivityNumber(Integer activityId);
    public Integer getState(Integer activityId,Integer userId);
    public void addActivity(Integer activityId,Integer userId) throws BusinessException;
    void deleteActivity(Integer activityId,Integer userId) throws BusinessException;
    List<ActivityUser> getActivityUser(Integer activityId) throws BusinessException;
}
