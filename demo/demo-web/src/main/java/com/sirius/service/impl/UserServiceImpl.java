package com.sirius.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sirius.domain.ResponseResult;
import com.sirius.domain.dto.LoginUserDto;
import com.sirius.domain.entity.LoginUser;
import com.sirius.domain.entity.User;
import com.sirius.enums.AppHttpCodeEnum;
import com.sirius.mapper.UserMapper;
import com.sirius.service.UserService;
import com.sirius.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseResult login(User user) {
//        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(User::getUserName, loginUserDto.getUserName());
//        queryWrapper.eq(User::getPassword, loginUserDto.getPassword());
//        User user = this.getOne(queryWrapper);
//        if (user == null) {
//            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
//        } else {
//            return ResponseResult.okResult();
//        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());

        Authentication authenticate = authenticationManager.authenticate(token);

        if(Objects.isNull(authenticate)){
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
        }

        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getUserId().toString();
        String jwt = JwtUtil.createJWT(userId);

        return ResponseResult.okResult(jwt);
    }

    @Override
    public ResponseResult register(User user) {
        String encode = passwordEncoder.encode(user.getPassword());

        user.setPassword(encode);

        save(user);

        return ResponseResult.okResult();
    }
}

