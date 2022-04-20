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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, LibraryMapper libraryMapper, ChatMapper chatMapper, RedisTemplate<String, String> redisTemplate) {
        this.userMapper = userMapper;
        this.libraryMapper = libraryMapper;
        this.chatMapper = chatMapper;
        this.redisTemplate = redisTemplate;
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
    public Chat chat(Chat chat) {
        Integer fromId = chat.getFromId();
        Integer toId = chat.getToId();
        String content = chat.getContent();
        String timeStr1 = redisTemplate.opsForValue().get(fromId + ":" + toId);
        String timeStr2 = redisTemplate.opsForValue().get(toId + ":" + fromId);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        long interval = 0L;
        if (timeStr1 == null) {
            if (timeStr2 != null) {
                localDateTime = LocalDateTime.parse(timeStr2, dateTimeFormatter);
            } else {
                // -1表示第一次聊天，第一次聊天应该展示时间
                interval = -1L;
            }
        } else {
            if (timeStr2 != null) {
                LocalDateTime time1 = LocalDateTime.parse(timeStr2, dateTimeFormatter);
                LocalDateTime time2 = LocalDateTime.parse(timeStr2, dateTimeFormatter);
                localDateTime = time1.isAfter(time2) ? time2 : time1;
            } else  {
                localDateTime = LocalDateTime.parse(timeStr1, dateTimeFormatter);
            }
        }
        chat.setChatTime(LocalDateTime.now());
        if (interval == -1) {
            chat.setChatInterval(interval);
        } else {
            chat.setChatInterval(localDateTime.until(LocalDateTime.now(), ChronoUnit.MINUTES));
        }
        chatMapper.insert(chat);
        redisTemplate.opsForValue().set(fromId + ":" + toId, LocalDateTime.now().format(dateTimeFormatter));
        redisTemplate.opsForValue().set(toId + ":" + fromId, LocalDateTime.now().format(dateTimeFormatter));
        return chat;
    }

    @Override
    public List<Chat> getChat(Integer fromId, Integer toId) {
        QueryWrapper<Chat> chatQueryWrapper = new QueryWrapper<>();
        chatQueryWrapper.eq("from_id", fromId);
        chatQueryWrapper.eq("to_id", toId);
        List<Chat> chats = chatMapper.selectList(chatQueryWrapper);
        chatQueryWrapper = new QueryWrapper<>();
        chatQueryWrapper.eq("from_id", toId);
        chatQueryWrapper.eq("to_id", fromId);
        chats.addAll(chatMapper.selectList(chatQueryWrapper));
        return chats;
    }

}
