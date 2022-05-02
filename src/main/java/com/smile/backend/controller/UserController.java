package com.smile.backend.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.Producer;
import com.smile.backend.annotation.TokenRequired;
import com.smile.backend.entity.User;
import com.smile.backend.exception.BizException;
import com.smile.backend.service.UserService;
import com.smile.backend.utils.*;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
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
    private Producer defaultKaptcha;
    private RedisUtils redisUtils;

    @Autowired
    public UserController(UserService userService, Producer defaultKaptcha, RedisUtils redisUtils) {
        this.userService = userService;
        this.defaultKaptcha = defaultKaptcha;
        this.redisUtils = redisUtils;
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

    @TokenRequired
    @PostMapping("/author")
    public Result author(@RequestParam List<Integer> lbIds) {
        return ResultResponse.getSuccessResult(userService.getAuthorsByLibIds(lbIds));
    }

    @TokenRequired
    @PostMapping("/getChat")
    public Result getChat(@RequestParam Integer fromId, @RequestParam Integer toId) {
        return ResultResponse.getSuccessResult(userService.getChat(fromId, toId));
    }

    @GetMapping("/kaptcha/{uuid}")
    public void getImageCode(HttpServletResponse response, @PathVariable String uuid) throws Exception {

        //禁止缓存
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        //设置响应格式为png图片
        response.setContentType("image/png");

        //为验证码创建一个文本
        String codeText = defaultKaptcha.createText();
        //将验证码存到redis
        redisUtils.set(StringConstantsEnum.KAPTCHA_KEY.getConstant() + uuid, codeText, 60);
        // 用创建的验证码文本生成图片
        BufferedImage bi = defaultKaptcha.createImage(codeText);
        ServletOutputStream out = response.getOutputStream();
        //写出图片
        ImageIO.write(bi, "png", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    @GetMapping("/kaptcha/check")
    public Result checkImageCode(
            @RequestParam(value = "imageCode") String validImageCode,
            @RequestParam("uuid")String uuid){
        try {
            if (StringUtils.isBlank(validImageCode)){
                return ResultResponse.getFailResult("验证码不能为空");
            }
            String imageCode = ((String) redisUtils.get(StringConstantsEnum.KAPTCHA_KEY.getConstant() + uuid));
            if (StringUtils.isBlank(imageCode) || !imageCode.equals(validImageCode)){
                return ResultResponse.getFailResult("验证码错误");
            }
            return ResultResponse.getSuccessResult();
        } catch (Exception e) {
            return ResultResponse.getFailResult(e.getMessage());
        }
    }

}
