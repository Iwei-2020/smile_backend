package com.smile.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author passion
 * @since 2022-03-29
 */

@TableName("library")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Library implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "lb_id", type = IdType.AUTO)
    private Integer lbId;

    @TableField("lb_name")
    private String lbName;

    @TableField(value = "owner_id")
    private Integer ownerId;

    @TableField("lb_creator")
    private String lbCreator;

    @TableField("lb_type")
    private Integer lbType;

    @TableField("lb_description")
    private String lbDescription;

    @TableField("lb_like")
    private Integer lbLike;

    @TableField("lb_watch")
    private Integer lbWatch;

    @TableField("lb_count")
    private Integer lbCount;

    @TableField(value = "lb_created", fill = FieldFill.INSERT)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lbCreated;


}
