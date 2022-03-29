package com.smile.backend.serviceImpl;

import com.smile.backend.entity.User;
import com.smile.backend.mapper.UserMapper;
import com.smile.backend.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author passion
 * @since 2022-03-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
