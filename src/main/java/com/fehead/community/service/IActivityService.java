package com.fehead.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fehead.community.entities.Activity;
import com.fehead.community.error.BusinessException;
import com.fehead.community.model.ActivityModel;
import com.fehead.community.view.ActivityVO;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ktoking
 * @since 2020-04-04
 */
public interface IActivityService extends IService<Activity> {
    //发布活动
     void publishNewActivity(Activity activity) throws BusinessException;
    //上传图片
     Object uploadPicture(MultipartFile uploadFile) throws BusinessException, JSchException, SftpException;
     //展示活动
     ActivityVO showActivity(Integer activityId) throws BusinessException;
    //分页查找活动
     List<ActivityModel> getAcitivity(Integer page) throws BusinessException;
    //模糊查询活动
     List<ActivityModel> searchActivity(String name) throws BusinessException;
    //查找用户的活动
      List<ActivityModel> myActivity(Integer userId,Integer state) throws BusinessException;
      //获取该社团所有的活动
    List<ActivityModel> getAllActivityByClubId(Integer clubId);
    //加入活动
    void addActivity(Integer userId,Integer activityId);

    void updateState(Integer activityId) throws BusinessException;

}
