package com.fehead.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fehead.community.entities.Activity;
import org.springframework.data.domain.Page;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ktoking
 * @since 2020-04-04
 */
public interface ActivityMapper extends BaseMapper<Activity> {
    IPage<Activity> selectPage(Page<?>page);

}
