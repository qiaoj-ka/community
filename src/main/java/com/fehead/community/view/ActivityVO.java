package com.fehead.community.view;

import lombok.Data;

@Data
public class ActivityVO {

    private static final long serialVersionUID=1L;

    private Integer activityId;

    private String activityName;


//    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime activityStarttime;
//    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime activityEndtime;

    private Long activityStartTime;

    private Long activityEndTime;

    private String activityDescribe;

    private String activityReward;

    private Integer activityPeople;

    private Integer clubId;

    private Integer activityStatus;

    private Integer activityCreaterId;

    private String activityCover;

    private String activityPosition;

    //需要返回创建人姓名
    private String userName;
    //创建人电话
    private String userPhone;
    //已经参加该活动的人数
    private Integer activityNumber;
    //活动社团名称
    private String clubName;

}
