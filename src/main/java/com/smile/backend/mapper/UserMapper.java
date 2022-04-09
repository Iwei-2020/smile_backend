package com.smile.backend.mapper;

import com.smile.backend.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author passion
 * @since 2022-03-11
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    void userAddLikeCount(@Param("lbId") Integer lbId);
    void userReduceLikeCount(@Param("lbId") Integer lbId);
}
