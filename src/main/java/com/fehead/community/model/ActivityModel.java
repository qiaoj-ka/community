package com.fehead.community.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ActivityModel {

    private Integer clubId;

    private Integer activityId;

    private String activityName;

    private String activityDescribe;

    private String activityCover;

    private Long activityEndtime;

    private String userAvatar;

    private String userName;

    private String time;

    private Integer hot;

    private Integer activityPeople;


}
