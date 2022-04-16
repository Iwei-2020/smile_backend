package com.smile.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smile.backend.entity.Library;
import com.smile.backend.entity.User;
import com.smile.backend.mapper.LibraryMapper;
import com.smile.backend.mapper.UserMapper;
import com.smile.backend.service.UserService;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private UserMapper userMapper;
    private LibraryMapper libraryMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, LibraryMapper libraryMapper) {
        this.userMapper = userMapper;
        this.libraryMapper = libraryMapper;
    }

    @Override
    public List<User> getAuthorsByLibIds(List<Integer> lbIds) {
        QueryWrapper<Library> libraryQueryWrapper = new QueryWrapper<>();
        libraryQueryWrapper.in("lb_id", lbIds);
        List<Library> libraries = libraryMapper.selectList(libraryQueryWrapper);
        ArrayList<Integer> authorIds = new ArrayList<>();
        for (Library library : libraries) {
            authorIds.add(library.getOwnerId());
        }
        return userMapper.selectBatchIds(authorIds);
    }

}
