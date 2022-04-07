package com.smile.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 作者
 * @since 2022-04-07
 */
@TableName("specific_libs")
public class SpecificLibs implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String libIds;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getLibIds() {
        return libIds;
    }

    public void setLibIds(String libIds) {
        this.libIds = libIds;
    }

    @Override
    public String toString() {
        return "SpecificLibs{" +
            "id=" + id +
            ", name=" + name +
            ", libIds=" + libIds +
        "}";
    }
}
