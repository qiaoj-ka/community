package com.fehead.community.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fehead.community.entities.Club;
import com.fehead.community.entities.MethodTest;
import com.fehead.community.entities.Test1;
import com.fehead.community.error.BusinessException;
import com.fehead.community.model.ActivityModel;
import com.fehead.community.response.CommonReturnType;
import com.fehead.community.service.IActivityService;
import com.fehead.community.service.IClubService;
import com.fehead.community.view.ClubVO;
import com.fehead.community.view.ClubVO1;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ktoking
 * @since 2020-04-03
 */
@RestController
public class ClubController extends BaseController{

    @Autowired
    private IClubService iClubService;

    @Autowired
    private IActivityService iActivityService;
    //创建社团
    @PostMapping(value = "/club/create/club")
    public CommonReturnType createClub(@RequestParam("createId") Integer createId,
                                       @RequestParam("clubName") String clubName,
                                       @RequestParam("clubDescribe")String clubDescribe,
                                       @RequestParam("clubType")String clubType,
                                       @RequestParam("clubInstitute")String clubInstitute,
                                       @RequestParam("clubQQ") String clubqq,
                                       @RequestParam("clubLogo")MultipartFile file) throws BusinessException, JSchException, SftpException, JsonProcessingException {
        String logo = null;

        Object result = iActivityService.uploadPicture(file);
        logo = new ObjectMapper().writeValueAsString(result);
        String newLogo= logo.replace("\"", "");
        ClubVO clubVO=new ClubVO(null,clubName,clubType,clubDescribe,newLogo,null,clubInstitute,null,createId,clubqq);
        Integer clubId = iClubService.createClub(clubVO);
        return CommonReturnType.creat(clubId);
    }

    //管理社团-简介页面
    @GetMapping(value = "club/getClub/byCreateId")
    public CommonReturnType getClubByCreateId(@RequestParam("userId") Integer id){
        Club club=iClubService.isCreateIdhasCreate(id);
        ClubVO clubVO=transforToVO(club);
        return CommonReturnType.creat(clubVO);
    }

    //获得详情信息
    @GetMapping(value = "/club/getClubInfo")
    public CommonReturnType getClubInfo(@RequestParam("clubId")Integer clubId){

        Club club=iClubService.getClubById(clubId);
        return CommonReturnType.creat(club);
    }


//    //社团首页获取社团
//    @GetMapping(value = "club/demo")
//    public CommonReturnType getAllClub(){
//
//    }

    //按照类型查找社团目录
    @GetMapping(value = "club/getClubDemo")
    public CommonReturnType getClubDemo(@RequestParam("type")String type){
        MethodTest methodTest=new MethodTest();
        methodTest.setText(type);
        List<Club> list=iClubService.getClub(type);
        List<Test1> list1=new ArrayList<>();
        for (Club club:list){
            Test1 test1=new Test1();
            test1.setId(club.getClubId());
            test1.setText(club.getClubName());
            list1.add(test1);
        }
        methodTest.setChildren(list1);
        return CommonReturnType.creat(methodTest);
    }
    //进入社团首页
    @GetMapping(value = "get/club/all/info")
    public CommonReturnType getClubAllInfo(@RequestParam(value = "clubId")Integer clubId){
        List<ActivityModel> activityModels=iActivityService.getAllActivityByClubId(clubId);
        return CommonReturnType.creat(activityModels);
    }

    //查找该用户所有的社团信息
    @GetMapping(value = "/get/my/club")
    public CommonReturnType getMyClub(@RequestParam(value = "userId")Integer userId) throws BusinessException {
        List<Club> clubList=iClubService.getMyClub(userId);
        List<ClubVO1> clubVO1s=transferToVO1(clubList);
        return CommonReturnType.creat(clubVO1s);
    }

    //更改社团信息
    @PostMapping(value = "update/club/info")
    public CommonReturnType upDateClubInfo(Club club) throws BusinessException {
        club=iClubService.updateClubInfo(club);
        return CommonReturnType.creat(club);
    }

    private List<ClubVO1> transferToVO1(List<Club> list){
        List<ClubVO1> list1=new ArrayList<>();
        for (Club club:list){
            ClubVO1 clubVO1=new ClubVO1();
            BeanUtils.copyProperties(club,clubVO1);
            list1.add(clubVO1);
        }
        return list1;
    }
    private ClubVO transforToVO(Club club){
        ClubVO clubVO=new ClubVO();
        BeanUtils.copyProperties(club,clubVO);
        return clubVO;
    }
}

