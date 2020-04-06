package com.fehead.community.service.impl;

import com.fehead.community.entities.Activity;
import com.fehead.community.mapper.ActivityMapper;
import com.fehead.community.service.IActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ktoking
 * @since 2020-04-04
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

}
