package com.fehead.community.controller;


import com.fehead.community.error.BusinessException;
import com.fehead.community.response.CommonReturnType;
import com.fehead.community.service.IClubUserService;
import org.springframework.web.bind.annotation.*;

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
public class ClubUserController extends BaseController{

    @Resource
    private IClubUserService clubUserService;
    //加入社团
    @PostMapping(value = "/add/club")
    public CommonReturnType addClub(@RequestParam(value = "userId")Integer userId,
                                    @RequestParam(value = "clubId")Integer clubId) throws BusinessException {
        clubUserService.addClub(userId,clubId);
        return CommonReturnType.creat("success");
    }

    //退出社团
    @DeleteMapping(value = "/quit/club")
    public CommonReturnType quitClub(@RequestParam(value = "userId")Integer userId,@RequestParam(value = "clubId")Integer clubId) throws BusinessException {
       return CommonReturnType.creat(clubUserService.quiteClub(userId, clubId));
    }


    @GetMapping(value = "/hahhahh")
    public String gethahah(){
        return "hahahah";
    }

}

