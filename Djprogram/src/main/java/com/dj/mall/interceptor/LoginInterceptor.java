package com.dj.mall.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.dj.mall.common.Constants;
import com.dj.mall.common.Result;
import com.dj.mall.context.UserContext;
import com.dj.mall.util.JwtUtil;
import com.dj.mall.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.prefix}")
    private String tokenPrefix;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(tokenHeader);
        
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token");
        }

        if (StrUtil.isBlank(token)) {
            writeResponse(response, Result.error(401, "请先登录"));
            return false;
        }

        if (token.startsWith(tokenPrefix)) {
            token = token.substring(tokenPrefix.length());
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            writeResponse(response, Result.error(401, "登录已过期，请重新登录"));
            return false;
        }

        String redisToken = (String) redisUtil.get(Constants.TOKEN_PREFIX + userId);
        if (StrUtil.isBlank(redisToken) || !redisToken.equals(token)) {
            writeResponse(response, Result.error(401, "登录已过期，请重新登录"));
            return false;
        }

        if (!jwtUtil.validateToken(token)) {
            writeResponse(response, Result.error(401, "登录已过期，请重新登录"));
            return false;
        }

        UserContext.setUserId(userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear();
    }

    private void writeResponse(HttpServletResponse response, Result<?> result) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(200);
        response.getWriter().write(JSON.toJSONString(result));
    }
}
