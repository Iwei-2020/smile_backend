package com.smile.backend.controller;

import com.google.code.kaptcha.Producer;
import com.smile.backend.utils.RedisUtils;
import com.smile.backend.utils.Result;
import com.smile.backend.utils.ResultResponse;
import com.smile.backend.utils.StringConstantsEnum;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

@RestController
@RequestMapping("/kaptcha")
@NoArgsConstructor
public class KaptchaController {
    private Producer defaultKaptcha;
    private RedisUtils redisUtils;

    @Autowired
    public KaptchaController(Producer defaultKaptcha, RedisUtils redisUtils) {
        this.defaultKaptcha = defaultKaptcha;
        this.redisUtils = redisUtils;
    }

    @GetMapping("/{uuid}")
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

    @GetMapping("/check")
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
