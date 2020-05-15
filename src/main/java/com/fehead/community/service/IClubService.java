package com.fehead.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fehead.community.entities.Club;
import com.fehead.community.error.BusinessException;
import com.fehead.community.view.ClubVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ktoking
 * @since 2020-04-03
 */
public interface IClubService extends IService<Club> {
    public Integer createClub(ClubVO clubVo) throws BusinessException;
    //获取自己所创建的社团
    public Club isCreateIdhasCreate(Integer createId);
    public  List<Club> getClub(String type);
    public Club getClubById(Integer clubId);
    List<Club> getMyClub(Integer userId) throws BusinessException;
    Club updateClubInfo(Club club) throws BusinessException;
}
