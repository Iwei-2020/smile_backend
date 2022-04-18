package com.smile.backend.service.impl;

import com.smile.backend.entity.Chat;
import com.smile.backend.mapper.ChatMapper;
import com.smile.backend.service.IChatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 作者
 * @since 2022-04-16
 */
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements IChatService {

}
