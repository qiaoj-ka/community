package com.fehead.community.service.impl;

import com.fehead.community.entities.ClubUser;
import com.fehead.community.mapper.ClubUserMapper;
import com.fehead.community.service.IClubUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ktoking
 * @since 2020-04-03
 */
@Service
public class ClubUserServiceImpl extends ServiceImpl<ClubUserMapper, ClubUser> implements IClubUserService {

}
