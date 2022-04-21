package com.smile.backend;

import com.smile.backend.entity.Chat;
import com.smile.backend.entity.User;
import com.smile.backend.mapper.SpecificLbMapper;
import com.smile.backend.mapper.UserMapper;
import com.smile.backend.service.UserService;
import com.smile.backend.utils.Utils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class SmileApplicationTests {


    @Autowired
    private SpecificLbMapper specificLibsMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void contextLoads() {
//        System.out.println(LocalDate.now().minusWeeks(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Chat chat = new Chat(43, 9, 2, "宝宝", null, 0L);
        System.out.println(Utils.objectToJsonString(chat));
    }

    @Test
    void initUV() {
        for (int i = 0; i < 200; i++) {
            String ip = RandomUtils.nextInt(0, 256) + "." + RandomUtils.nextInt(0, 256) + "." + RandomUtils.nextInt(0, 256) + "." + RandomUtils.nextInt(0, 256);
            LocalDate now = LocalDate.now();
            int i1 = RandomUtils.nextInt(0, 8);
            LocalDate localDate = now.minusDays(i1);
            redisTemplate.opsForHyperLogLog().add(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), ip);
        }
    }

    @Test
    void initUser() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 48; i++) {
            User user = new User();
            user.setPhone(18379998000L + i + "");
            user.setCertificate("gg331506");
            user.setUserRole(RandomUtils.nextInt(1, 3));
            users.add(user);
        }
        userService.saveBatch(users);
    }
}
