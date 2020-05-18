package com.fehead.community.view;

import com.fehead.community.entities.Club;
import com.fehead.community.model.ActivityModel;
import lombok.Data;

import java.util.List;

@Data
public class ClubHomePageVO {
    private List<ActivityModel> list;
    private Club clubInfo;
    //社团成员数量
    private Integer numbers;
}
