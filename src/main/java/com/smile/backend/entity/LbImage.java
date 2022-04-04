package com.smile.backend.entity;

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
 * @author 作者
 * @since 2022-04-03
 */
@TableName("lb_image")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LbImage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer lbId;

    private Integer imageId;

}
