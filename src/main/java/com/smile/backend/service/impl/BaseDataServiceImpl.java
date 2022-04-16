package com.smile.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smile.backend.entity.User;
import com.smile.backend.exception.BizException;
import com.smile.backend.mapper.BaseDataMapper;
import com.smile.backend.mapper.UserMapper;
import com.smile.backend.service.BaseDataService;
import com.smile.backend.utils.ResultEnum;
import com.smile.backend.utils.Utils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
@NoArgsConstructor
public class BaseDataServiceImpl implements BaseDataService {

    private RedisTemplate<String, String> redisTemplate;
    private BaseDataMapper baseDataMapper;
    private UserMapper userMapper;

    @Autowired
    public BaseDataServiceImpl(RedisTemplate<String, String> redisTemplate, BaseDataMapper baseDataMapper, UserMapper userMapper) {
        this.redisTemplate = redisTemplate;
        this.baseDataMapper = baseDataMapper;
        this.userMapper = userMapper;
    }

    @Override
    public ObjectNode getBaseData(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user.getUserRole() < 3) {
            throw new BizException(ResultEnum.FORBIDDEN);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("uv", getUV());
        map.put("baseData", baseDataMapper.getBaseData());
        map.put("userAdmin", getAdmin());
        map.put("userDistribution", getUserDistribution());
        return Utils.objectNodeFormat(map);
    }

    public Map<String, Long> getUV() {
        HashMap<String, Long> map = new LinkedHashMap<>();
        LocalDate now = LocalDate.now();
        for (int i = 7; i > 0; i--) {
            String format = now.minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String resultFormat = now.minusDays(i).format(DateTimeFormatter.ofPattern("MM-dd"));
            Long size = redisTemplate.opsForHyperLogLog().size(format);
            map.put(resultFormat, size);
        }
        return map;
    }

    public List<User> getAdmin() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.ge("user_role", 4);
        return userMapper.selectList(userQueryWrapper);
    }

    // 获取用户分布
    public List<ObjectNode> getUserDistribution() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ObjectNode> objectNodes = new ArrayList<>();

        ObjectNode objectNode = objectMapper.createObjectNode();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_role", 1);
        objectNode.put("name", "普通用户");
        objectNode.put("value", userMapper.selectCount(userQueryWrapper));
        objectNodes.add(objectNode);

        objectNode = objectMapper.createObjectNode();
        userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_role", 2);
        objectNode.put("name", "会员用户");
        objectNode.put("value", userMapper.selectCount(userQueryWrapper));
        objectNodes.add(objectNode);

        objectNode = objectMapper.createObjectNode();
        userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_role", 3);
        objectNode.put("name", "大会员用户");
        objectNode.put("value", userMapper.selectCount(userQueryWrapper));
        objectNodes.add(objectNode);
        return objectNodes;
    }
}
