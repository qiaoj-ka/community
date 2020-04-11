package com.fehead.community.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fehead.community.error.BusinessException;
import com.fehead.community.service.IActivityService;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
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

    @GetMapping(value = "/hello")
    public String hello(){
        return "hello";
    }
}

