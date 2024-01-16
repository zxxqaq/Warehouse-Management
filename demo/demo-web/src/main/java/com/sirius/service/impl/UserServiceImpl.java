package com.sirius.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sirius.domain.ResponseResult;
import com.sirius.domain.dto.LoginUserDto;
import com.sirius.domain.entity.User;
import com.sirius.enums.AppHttpCodeEnum;
import com.sirius.mapper.UserMapper;
import com.sirius.service.UserService;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public ResponseResult login(LoginUserDto loginUserDto) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, loginUserDto.getUserName());
        queryWrapper.eq(User::getPassword, loginUserDto.getPassword());
        User user = this.getOne(queryWrapper);
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
        } else {
            return ResponseResult.okResult();
        }
    }
}

