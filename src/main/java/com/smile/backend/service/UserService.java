package com.smile.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smile.backend.entity.Chat;
import com.smile.backend.entity.User;
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
    void chat(Integer fromId, Integer toId, String content);
    List<Chat> getChat(Integer fromId, Integer toId);
}
