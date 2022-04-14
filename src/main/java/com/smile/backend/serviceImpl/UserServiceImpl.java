package com.smile.backend.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smile.backend.entity.User;
import com.smile.backend.entity.UserLibrary;
import com.smile.backend.exception.BizException;
import com.smile.backend.mapper.UserLibraryMapper;
import com.smile.backend.mapper.UserMapper;
import com.smile.backend.service.UserService;
import com.smile.backend.utils.ResultEnum;
import com.smile.backend.vo.BaseDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author passion
 * @since 2022-03-11
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserLibraryMapper userLibraryMapper;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserLibraryMapper userLibraryMapper, UserMapper userMapper) {
        this.userLibraryMapper = userLibraryMapper;
        this.userMapper = userMapper;
    }


    @Override
    public List<User> getAuthorsByLibIds(List<Integer> lbIds) {
        QueryWrapper<UserLibrary> userLibraryQueryWrapper = new QueryWrapper<>();
        userLibraryQueryWrapper.in("lb_id", lbIds);
        List<UserLibrary> userLibraries = userLibraryMapper.selectList(userLibraryQueryWrapper);
        ArrayList<Integer> authorIds = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();
        for (UserLibrary userLibrary : userLibraries) {
            authorIds.add(userLibrary.getUserId());
        }
        for (Integer authorId : authorIds) {
            users.add(userMapper.selectById(authorId));
        }
        return users;
    }

    @Override
    public BaseDataVo getBaseData(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user.getUserRole() < 3) {
            throw new BizException(ResultEnum.FORBIDDEN);
        }
        return userMapper.getBaseData();
    }
}
