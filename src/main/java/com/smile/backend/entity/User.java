package com.smile.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author passion
 * @since 2022-03-11
 */
@TableName("user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("username")
    private String username;

    @TableField("phone")
    private String phone;

    @TableField("certificate")
    private String certificate;

    @TableField("user_role")
    private Integer userRole;

    @TableField("gender")
    private Integer gender;

    @TableField("birthday")
    private String birthday;

    @TableField("signature")
    private String signature;

    @TableField(value = "avatar_url")
    private String avatarUrl;

    @TableField(value = "liked_count")
    private Integer likedCount;

    @TableField(value = "like_lb")
    private String likeLb;

    @TableField(value = "star_lb")
    private String starLb;
}
