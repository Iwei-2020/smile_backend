package com.smile.backend;

import com.smile.backend.mapper.SpecificLbMapper;
import com.smile.backend.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmileApplicationTests {


    @Autowired
    private SpecificLbMapper specificLibsMapper;
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        userMapper.userReduceLikeCount(2);
    }

}
