package com.fehead.community.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author ktoking
 * @since 2020-04-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Activity implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "activity_id", type = IdType.AUTO)
    private Integer activityId;

    private String activityName;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime activityStarttime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime activityEndtime;

    private String activityDescribe;

    private String activityReward;

    private Integer activityPeople;

    private Integer clubId;

    private Integer activityStatus;

    private Integer activityCreaterId;

    private String activityCover;

    private String activityPosition;

    public Activity(Integer activityId, String activityName, LocalDateTime activityStarttime, LocalDateTime activityEndtime, String activityDescribe, String activityReward, Integer activityPeople, Integer clubId, Integer activityStatus, Integer activityCreaterId, String activityCover, String activityPosition) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.activityStarttime = activityStarttime;
        this.activityEndtime = activityEndtime;
        this.activityDescribe = activityDescribe;
        this.activityReward = activityReward;
        this.activityPeople = activityPeople;
        this.clubId = clubId;
        this.activityStatus = activityStatus;
        this.activityCreaterId = activityCreaterId;
        this.activityCover = activityCover;
        this.activityPosition = activityPosition;
    }
}
