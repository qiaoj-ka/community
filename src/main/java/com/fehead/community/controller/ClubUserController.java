package com.fehead.community.controller;


import com.fehead.community.entities.Club;
import com.fehead.community.error.BusinessException;
import com.fehead.community.response.CommonReturnType;
import com.fehead.community.service.IClubService;
import com.fehead.community.service.IClubUserService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ClubUserController extends BaseController{

    @Resource
    private IClubUserService clubUserService;

    @Resource
    private IClubService iClubService;
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

    //管理人员提出社团成员
    @DeleteMapping(value = "delete/club/mumber")
    public CommonReturnType deleteClubNumber(@RequestParam(value = "createId")Integer createId,
                                             @RequestParam(value = "clubId")Integer clubId,
                                             @RequestParam(value = "userId")Integer userId) throws BusinessException {
        Club club=iClubService.isCreateIdhasCreate(createId);
        if(club==null){
            log.info("你没有删除用户的权限");
        }
        quitClub(userId,clubId);//直接调用上面取消两者关系的方法
        return CommonReturnType.creat("删除成功");
    }


    @GetMapping(value = "/hahhahh")
    public String gethahah(){
        return "hahahah";
    }

}

