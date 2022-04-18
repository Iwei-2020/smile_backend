package com.smile.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smile.backend.entity.Chat;
import com.smile.backend.entity.Library;
import com.smile.backend.entity.User;
import com.smile.backend.mapper.ChatMapper;
import com.smile.backend.mapper.LibraryMapper;
import com.smile.backend.mapper.UserMapper;
import com.smile.backend.service.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private ChatMapper chatMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, LibraryMapper libraryMapper, ChatMapper chatMapper) {
        this.userMapper = userMapper;
        this.libraryMapper = libraryMapper;
        this.chatMapper = chatMapper;
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

    @Override
    public void chat(Integer fromId, Integer toId, String content) {
        Chat chat = new Chat(null, fromId, toId, content, LocalDateTime.now());
        chatMapper.insert(chat);
    }

    @Override
    public List<Chat> getChat(Integer fromId, Integer toId) {
        QueryWrapper<Chat> chatQueryWrapper = new QueryWrapper<>();
        chatQueryWrapper.eq("from_id", fromId);
        chatQueryWrapper.eq("to_id", toId);
        return chatMapper.selectList(chatQueryWrapper);
    }

}
