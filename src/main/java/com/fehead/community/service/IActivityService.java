package com.fehead.community.service;

import com.fehead.community.entities.Activity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fehead.community.entities.User;
import com.fehead.community.error.BusinessException;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ktoking
 * @since 2020-04-04
 */
public interface IActivityService extends IService<Activity> {

    public void publishNewActivity(Activity activity);

     public Object uploadPicture(MultipartFile uploadFile) throws BusinessException, JSchException, SftpException;
}
