package com.smile.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smile.backend.entity.User;
import com.smile.backend.vo.BaseDataVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author passion
 * @since 2022-03-11
 */
public interface UserService extends IService<User> {
    List<User> getAuthorsByLibIds(List<Integer> lbIds);
    BaseDataVo getBaseData(Integer userId);
}
