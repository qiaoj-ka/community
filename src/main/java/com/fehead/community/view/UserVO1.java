package com.fehead.community.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO1 {

    private Integer userId;

    private String userName;

    private Integer userGender;

    private String userInstitute;

    private String userClass;

    private String userPhone;

    private String userAvatar;
}
