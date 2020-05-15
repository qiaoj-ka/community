package com.fehead.community.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ClubModel {
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


}
