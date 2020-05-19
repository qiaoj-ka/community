package com.fehead.community.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  ClubVO {

    private Integer clubId;

    private String clubName;

    private String clubType;

    private String clubDescribe;

    private String clubLogo;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime clubEstablishTime;

    private String clubInstitute;

    private Integer clubStatus;

    private Integer clubCreaterId;

    private String clubQQ;
    //社团参与人数
    private Integer count;
    
}
