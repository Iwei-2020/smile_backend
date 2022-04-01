package com.smile.backend.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author passion
 * @since 2022-03-29
 */
@Getter
@Setter
@TableName("library")
@ToString
public class Library implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "lb_id", type = IdType.AUTO)
    private Integer lbId;

    @TableField("lb_name")
    private String lbName;

    @TableField("lb_type")
    private Integer lbType;

    @TableField("lb_description")
    private String lbDescription;

    @TableField("lb_like")
    private Integer lbLike;

    @TableField("lb_watch")
    private Integer lbWatch;

    @TableField(value = "lb_created", fill = FieldFill.INSERT)
    private LocalDate lbCreated;


}
