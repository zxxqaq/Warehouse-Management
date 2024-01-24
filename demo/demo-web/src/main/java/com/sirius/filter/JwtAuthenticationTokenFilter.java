package com.sirius.filter;

import com.alibaba.fastjson.JSON;
import com.sirius.domain.ResponseResult;
import com.sirius.domain.entity.LoginUser;
import com.sirius.domain.entity.User;
import com.sirius.enums.AppHttpCodeEnum;
import com.sirius.service.UserService;
import com.sirius.utils.JwtUtil;
import com.sirius.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //从request中拿token
        String token = httpServletRequest.getHeader("token");

        //判断token是否有内容(不为空，长度大于1，不全是空格)
        if(!StringUtils.hasText(token)){
            //如果token字段没有内容这里认为是不需要认证的页面，放行？
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        //解析token
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {

            //token解析不出来/token过时 访问失败，需要重新登录
            //这里我们还在filter层，不在controller层，没法直接new ResponsResult返回
            //利用工具里手动渲染Response

            //将认证失败的ResponseResult转为json渲染进response
            String jsonString = JSON.toJSONString(ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN));
            WebUtils.renderString(httpServletResponse, jsonString);
        }
        String userId = claims.getSubject();

        User user = userService.getById(Long.parseLong(userId));

//        //在redis中查找用户信息
//        LoginUser loginUser = redisCache.getCacheObject("bloglogin" + userId);

        LoginUser loginUser = new LoginUser(user);

        //redis中是否找到用户，有可能在redis中过时了
//        if(Objects.isNull(loginUser)){
//            String jsonString = JSON.toJSONString(ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN));
//            WebUtils.renderString(httpServletResponse, jsonString);
//            return;
//        }

        //通过认证，将用户信息保存到SecurityContextHolder 以后不需要token直接可以从SecurityContextHolder中获取该线程的loginuser


        //创建一个通过认证的Authentication对象
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}