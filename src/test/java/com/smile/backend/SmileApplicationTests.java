package com.smile.backend;

import com.smile.backend.entity.User;
import com.smile.backend.mapper.UserMapper;
import com.smile.backend.service.UserService;
import com.smile.backend.utils.StringConstantsEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class SmileApplicationTests {

    @Autowired
    private UserService userService;
//    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        String[] avatar = new String[16];
        int length = avatar.length;
        for (int i = 0; i < length; i++) {
            avatar[i] = StringConstantsEnum.DEFAULT_AVATAR_LOCAL.getConstant() + (i + 1) + ".png";
        }
        System.out.println(Arrays.toString(avatar));
    }

    @Test
    void testException() {
        userMapper.selectById(1);
    }

}
