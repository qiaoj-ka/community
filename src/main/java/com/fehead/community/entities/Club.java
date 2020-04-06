package com.fehead.community.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

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
public class Club implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "club_id", type = IdType.AUTO)
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
