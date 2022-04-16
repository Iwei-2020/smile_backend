package com.smile.backend.config.handler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.smile.backend.annotation.TokenRequired;
import com.smile.backend.entity.User;
import com.smile.backend.exception.BizException;
import com.smile.backend.service.UserService;
import com.smile.backend.utils.JwtUtil;
import com.smile.backend.utils.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从 http 请求头中取出 token
        String token = request.getHeader("Authorization");
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        System.out.println(34);
        // 检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(TokenRequired.class)) {
            TokenRequired userLoginToken = method.getAnnotation(TokenRequired.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new BizException(ResultEnum.UNAUTHORIZED);
                }
                // 获取 token 中的 id
                String id;
                try {
                    id = JWT.decode(token).getClaim("id").asString();
                } catch (JWTDecodeException e) {
                    throw new BizException(ResultEnum.UNAUTHORIZED);
                }
                User user = userService.getById(id);
                if (user == null) {
                    throw new BizException(ResultEnum.UNAUTHORIZED);
                }
                // 验证 token
                try {
                    if (!JwtUtil.verity(token, user.getCertificate())) {
                        throw new BizException(ResultEnum.UNAUTHORIZED);
                    }
                } catch (JWTVerificationException e) {
                    throw new BizException(ResultEnum.UNAUTHORIZED);
                }
                return true;
            }
        }

        return true;
    }
}
