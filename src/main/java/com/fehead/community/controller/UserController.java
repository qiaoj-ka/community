package com.fehead.community.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fehead.community.entities.ActivityUser;
import com.fehead.community.entities.Club;
import com.fehead.community.entities.ClubUser;
import com.fehead.community.entities.User;
import com.fehead.community.error.BusinessException;
import com.fehead.community.error.EmBusinessError;
import com.fehead.community.response.CommonReturnType;
import com.fehead.community.service.IActivityUserService;
import com.fehead.community.service.IClubUserService;
import com.fehead.community.service.IUserService;
import com.fehead.community.utility.ExcelUtil;
import com.fehead.community.utility.HttpClientUtil;
import com.fehead.community.view.ClubVO1;
import com.fehead.community.view.UserVO;
import com.fehead.community.view.UserVO1;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class UserController extends BaseController{

    @Resource
    private IUserService iUserService;
    @Resource
    private IClubUserService iClubUserService;
    @Resource
    private IActivityUserService iActivityUserService;

    //通过userId获取信息
    @GetMapping("/getUserInfo")
    public CommonReturnType getUserInfo(@RequestParam(value = "userId")Integer userId) throws BusinessException {
       return CommonReturnType.creat(iUserService.selectUserById(userId));
    }
    //完善学生信息
    @PostMapping(value = "/addInfo")
    public CommonReturnType addInfo(@RequestBody User user) throws BusinessException {
        if(user.getUserId()==null){
            log.info("用户ID不能为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数为空");
        }
        if(user.getUserInstitute()==null){
            log.info("学院不能为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数为空");
        }if(user.getUserClass()==null){
            log.info("班级不能为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数为空");
        }if(user.getUserPhone()==null){
            log.info("联系电话不能为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数为空");
        }if(user.getUserStudentNumber()==null){
            log.info("学号不能为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数为空");
        }
        Integer fluence = iUserService.updateUserByUserId(user);
        UserVO userVO = iUserService.selectUserVO(user);
        return CommonReturnType.creat(userVO);//返回一个VO用来确定前端是否绑定过信息
    }

  //社团管理人管理社团获取所有的社团用户
    @GetMapping(value = "/get/myClub/user")
    public CommonReturnType getMyAllUser(@RequestParam(value = "userId")Integer userId,
                                         @RequestParam(value = "page")Integer page,
                                         @RequestParam(value = "clubId")Integer clubId) throws BusinessException {
        log.info(userId+" ");
        List<ClubUser> clubUsers=iClubUserService.getAllUserByPage(page,clubId);
        List<UserVO1> userVO1s=new ArrayList<>();
        for (ClubUser cu:clubUsers){
            User user=iUserService.selectUserById(cu.getUserId());
            UserVO1 userVO1=transferToVO1(user);
            userVO1s.add(userVO1);
        }
        return CommonReturnType.creat(userVO1s);
    }
    //把VO转换成VO1
    private UserVO1 transferToVO1(User user){
        UserVO1 userVO1=new UserVO1();
        BeanUtils.copyProperties(user,userVO1);
        return userVO1;
    }
    @Autowired
    private ExcelUtil excelUtil;
    //导出参加活动人员Excel
    @GetMapping("/ExcelDownload/user/activity")
    public void excelDownloadActivityUser(HttpServletResponse response,
                                          Integer activityId,
                                          String sheetName,
                                          String fileName) throws BusinessException, IOException {
        //表头数据
        //表头数据
        String[] header={"姓名","性别","学院","班级","电话"};
        //获取所有参加活动人员并包装
        List<ActivityUser> list=iActivityUserService.getActivityUser(activityId);
        List<UserVO1> userVO1s=getAllUser(list);
        List<List<String>> users=getUserStringList(userVO1s);
        fileName=URLEncoder.encode(fileName,"UTF-8");
        excelUtil.exportExcel(response,header,users,sheetName,fileName,15);
    }
   //通过所有参与活动的id获取用户
    private List<UserVO1> getAllUser(List<ActivityUser> list) throws BusinessException {
        List<UserVO1> userVO1s=new ArrayList<>();
        //获取所有成员信息
        for (ActivityUser cu:list){
            User user=iUserService.selectUserById(cu.getUserId());
            UserVO1 userVO1=transferToVO1(user);
            userVO1s.add(userVO1);
        }
        return userVO1s;
    }
    //导出参加社团人员Excel
    @GetMapping("/ExcelDownload/user/club")
    public void excelDownload(HttpServletResponse response,
                              Integer clubId,
                              String sheetName,
                              String fileName) throws BusinessException, IOException {
        //表头数据
        String[] header={"姓名","性别","学院","班级","电话"};
        //获取所有成员信息list,并进行包装
        List<UserVO1> list=getAllUser(clubId);
        List<List<String>> users=getUserStringList(list);
        fileName=URLEncoder.encode(fileName,"UTF-8");
        excelUtil.exportExcel(response,header,users,sheetName,fileName,15);
    }
    //对于获取成员进行包装
    private List<List<String>> getUserStringList(List<UserVO1> list){
        List<List<String>> lists=new ArrayList<>();
        for (UserVO1 user:list){
            List<String> users=new ArrayList<>();
            users.add(user.getUserName());
            if(user.getUserGender()==0){
                users.add("女");
            }else {
                users.add("男");
            }
            users.add(user.getUserInstitute());
            users.add(user.getUserClass());
            users.add(user.getUserPhone());
            lists.add(users);
        }
        return lists;
    }
    //获取社团成员所有信息非分页
    private List<UserVO1> getAllUser(Integer clubId) throws BusinessException {
        List<ClubUser> list=iClubUserService.getClubUser(clubId);
        List<UserVO1> userVO1s=new ArrayList<>();
        //获取所有成员信息
        for (ClubUser cu:list){
            User user=iUserService.selectUserById(cu.getUserId());
            UserVO1 userVO1=transferToVO1(user);
            userVO1s.add(userVO1);
        }
        return userVO1s;
    }
    //微信登录
    @PostMapping("/wxLogin")
    public CommonReturnType createUser(
            String code,String userHead,String userName,Integer gender
    ){
        //这一系列操作是去wx官方换取openid
        System.out.println("wx-code: "+code);
        String url="https://api.weixin.qq.com/sns/jscode2session";
        Map<String,String> param=new HashMap<>();
        param.put("appid","wxf877a322f4ec5acf");
        param.put("secret","79cea8bfd8f14595c6069c8aa90b8e24");
        param.put("js_code",code);
        param.put("grant_type","authorization_code");
        String wxResult= HttpClientUtil.doGet(url,param);
        JSONObject jsonObject = JSON.parseObject(wxResult);
        //获取到的openid与sessionKey
        System.out.println(jsonObject);
        //从wx获得用户唯一标识
        String openid = jsonObject.getString("openid");
        //初始化一个User
        User user=new User();
        user.setUserAvatar(userHead);
        user.setUserGender(gender);
        user.setUserName(userName);
        user.setUserOpenid(openid);
        //去数据库查一下存在这个对象不
        final User findUser = iUserService.selectUser(user.getUserOpenid());
        if(findUser==null){
            //若数据库没存在这个对象,则插入
            //将用户注册进数据库
            final Integer isTrueInsert = iUserService.userLogin(user);
            System.out.println("数据库插入情况:"+isTrueInsert);
            User RtUser=iUserService.selectUser(user.getUserOpenid());
            return CommonReturnType.creat(RtUser);
        }else {
            //若对象存在,则更新
            final Integer rowByFluenced = iUserService.updateUser(user);
            System.out.println("数据库更新了"+rowByFluenced+"行");
//            User RtUser=iUserService.selectUser(user.getUserOpenid());
//            UserVO userVO=iUserService.selectUserVO(RtUser);
            UserVO userVO=getUserVO(user.getUserOpenid());
            return CommonReturnType.creat(userVO);
        }
    }

    private UserVO getUserVO(String userOpenid){
        User RtUser=iUserService.selectUser(userOpenid);
        UserVO userVO=iUserService.selectUserVO(RtUser);
        return userVO;
    }
}

