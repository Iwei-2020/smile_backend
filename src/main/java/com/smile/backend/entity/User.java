package com.smile.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author passion
 * @since 2022-03-11
 */
@Getter
@Setter
@ToString
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("username")
    private String username;

    @TableField("phone")
    private String phone;

    @TableField("password")
    private String password;

    @TableField("gender")
    private Integer gender;

    @TableField("birthday")
    private String birthday;

    @TableField("signature")
    private String signature;

    @TableField(value = "avatar_url")
    private String avatarUrl;

    @TableField(value = "collection_count")
    private Integer collectionCount;

    @TableField(value = "liked_count")
    private Integer likedCount;
}
