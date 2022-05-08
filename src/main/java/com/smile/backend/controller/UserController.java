package com.smile.backend.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smile.backend.annotation.TokenRequired;
import com.smile.backend.entity.User;
import com.smile.backend.exception.BizException;
import com.smile.backend.service.UserService;
import com.smile.backend.utils.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author passion
 * @since 2022-03-11
 */
@RestController
@RequestMapping("/user")
@NoArgsConstructor
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/login")
    public Result login(@RequestBody User user) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("phone", user.getPhone());
        User userGetOne = userService.getOne(userQueryWrapper);
        if (userGetOne == null) {
            return ResultResponse.getFailResult("该手机号不存在");
        }
        if (!userGetOne.getCertificate().equals(user.getCertificate())) {
            return ResultResponse.getFailResult("密码错误");
        }
        String token = JwtUtil.sign(userGetOne.getId() + "", userGetOne.getUsername(), userGetOne.getCertificate());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", userGetOne);
        hashMap.put("token", token);
        return ResultResponse.getSuccessResult("登录成功", Utils.objectNodeFormat(hashMap));
    }

    @RequestMapping("/register")
    public Result register(@RequestBody User user) {
        // todo 判断手机号是否唯一
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("phone", user.getPhone());
        User userGetOne = userService.getOne(userQueryWrapper);
        if (userGetOne == null) {
            user.setAvatarUrl(StringConstantsEnum.DEFAULT_AVATAR.getConstant());
            userService.save(user);
            return ResultResponse.getSuccessResult("注册成功", user);
        }
        return ResultResponse.getFailResult("该手机号已经被注册");
    }

    @TokenRequired
    @RequestMapping("/update")
    public Result update(@RequestBody User user) {
        // 保存信息
        userService.updateById(user);
        return ResultResponse.getSuccessResult("保存成功", user);
    }

    @TokenRequired
    @RequestMapping("/get")
    public Result get(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String id;
        try {
            id = JWT.decode(token).getClaim("id").asString();
        } catch (JWTDecodeException e) {
            throw new BizException(ResultEnum.UNAUTHORIZED);
        }
        User user = userService.getById(id);
        String newToken = JwtUtil.sign(user.getId() + "", user.getUsername(), user.getCertificate());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", user);
        hashMap.put("token", newToken);
        return ResultResponse.getSuccessResult(Utils.objectNodeFormat(hashMap));
    }

    @TokenRequired
    @GetMapping("/getDefaultAvatar")
    public Result getDefaultAvatar() {
        String[] avatar = new String[16];
        int length = avatar.length;
        for (int i = 0; i < length; i++) {
            avatar[i] = StringConstantsEnum.DEFAULT_AVATAR_LOCAL.getConstant() + (i + 1) + ".png";
        }
        return ResultResponse.getSuccessResult(avatar);
    }

    @TokenRequired
    @PostMapping("/avatar")
    public Result avatar(@RequestParam Map<String, String> map) {
        User user = new User();
        Integer id = Integer.parseInt(map.get("id"));
        user.setId(id);
        user.setAvatarUrl(map.get("url"));
        userService.updateById(user);
        return ResultResponse.getSuccessResult(userService.getById(id));
    }

    @PostMapping("/author")
    public Result author(@RequestParam List<Integer> lbIds) {
        return ResultResponse.getSuccessResult(userService.getAuthorsByLibIds(lbIds));
    }

    @TokenRequired
    @PostMapping("/getChat")
    public Result getChat(@RequestParam Integer fromId, @RequestParam Integer toId) {
        return ResultResponse.getSuccessResult(userService.getChat(fromId, toId));
    }

}
