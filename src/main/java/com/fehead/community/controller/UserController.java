package com.fehead.community.controller;


import com.fehead.community.entities.User;
import com.fehead.community.response.CommonReturnType;
import com.fehead.community.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ktoking
 * @since 2020-04-03
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private IUserService iUserService;

    //完善学生信息
    @PostMapping(value = "/addInfo")
    public CommonReturnType addInfo(User user){
        iUserService.insert(user);
        return CommonReturnType.creat("success!");
    }
}

