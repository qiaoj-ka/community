package com.fehead.community.view;

import lombok.Data;

@Data
public class UserVO {

    private Integer userId;

    private String userName;

    private Integer userGender;

    private String userInstitute;

    private String userClass;

    private String userPhone;

    private String userStudentNumber;

    private String userOpenid;

    private String userAvatar;

    //输出他所创建的社团
    private Integer isCreateClub;

    //判断是否已经完善所有信息
    private Integer isComplete;
}
