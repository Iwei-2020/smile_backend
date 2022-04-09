package com.smile.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

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
@TableName("user_library")
@AllArgsConstructor
@NoArgsConstructor
public class UserLibrary implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private Integer lbId;

}
