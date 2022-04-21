package com.smile.backend.controller;

import com.smile.backend.annotation.TokenRequired;
import com.smile.backend.service.BaseDataService;
import com.smile.backend.utils.Result;
import com.smile.backend.utils.ResultResponse;
import com.smile.backend.utils.Utils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/base")
@NoArgsConstructor
public class BaseDataController {

    private BaseDataService baseDataService;
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public BaseDataController(BaseDataService baseDataService, RedisTemplate<String, String> redisTemplate) {
        this.baseDataService = baseDataService;
        this.redisTemplate = redisTemplate;
    }

    @TokenRequired
    @GetMapping("/baseData/{userId}")
    public Result baseData(@PathVariable Integer userId) {
        return ResultResponse.getSuccessResult(baseDataService.getBaseData(userId));
    }

    @GetMapping("/uv")
    public Result uv(HttpServletRequest request) {
        redisTemplate.opsForHyperLogLog().add(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), Utils.getIpAddress(request));
        return ResultResponse.getSuccessResult();
    }
}
