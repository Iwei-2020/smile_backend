package com.smile.backend.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smile.backend.entity.Library;
import com.smile.backend.entity.UserLibrary;
import com.smile.backend.mapper.LibraryMapper;
import com.smile.backend.mapper.UserLibraryMapper;
import com.smile.backend.service.LibraryService;
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
 * @since 2022-03-29
 */
@Service
@Transactional
public class LibraryServiceImpl extends ServiceImpl<LibraryMapper, Library> implements LibraryService {

    private final LibraryMapper libraryMapper;
    private final UserLibraryMapper userLibraryMapper;

    @Autowired
    public LibraryServiceImpl(LibraryMapper libraryMapper, UserLibraryMapper userLibraryMapper) {
        this.libraryMapper = libraryMapper;
        this.userLibraryMapper = userLibraryMapper;
    }

    @Override
    public void addLibrary(Library lib, Integer userId) {
        libraryMapper.insert(lib);
        System.out.println("36: " + lib);
        UserLibrary userLibrary = new UserLibrary(userId, lib.getLbId());
        userLibraryMapper.insert(userLibrary);
    }

    @Override
    public List<Library> getLibrary(Integer id) {
        QueryWrapper<UserLibrary> userLibraryQueryWrapper = new QueryWrapper<>();
        userLibraryQueryWrapper.eq("user_id", id);
        List<UserLibrary> userLibraries = userLibraryMapper.selectList(userLibraryQueryWrapper);
        List<Integer> lbIdBatch = new ArrayList<>();
        for (UserLibrary userLibrary : userLibraries) {
            lbIdBatch.add(userLibrary.getLbId());
        }
        return libraryMapper.selectBatchIds(lbIdBatch);
    }
}
