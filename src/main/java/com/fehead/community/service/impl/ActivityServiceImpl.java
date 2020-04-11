package com.fehead.community.service.impl;

import com.fehead.community.entities.Activity;
import com.fehead.community.error.BusinessException;
import com.fehead.community.mapper.ActivityMapper;
import com.fehead.community.service.IActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fehead.community.utility.FtpUtil;
import com.fehead.community.utility.IDUtils;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

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

    @Override
    public void publishNewActivity(Activity activity) {
        //activity.getActivityCover()
    }

    @Resource
    private FtpUtil ftpUtil;

    public Object uploadPicture(MultipartFile uploadFile) throws BusinessException, JSchException, SftpException {
        //1.给上传的图片生成新的文件名
        //1.1获取原始文件名
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
}
