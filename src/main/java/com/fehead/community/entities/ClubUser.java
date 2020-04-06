package com.fehead.community.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ktoking
 * @since 2020-04-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ClubUser implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer clubId;

    private Integer userId;


}
