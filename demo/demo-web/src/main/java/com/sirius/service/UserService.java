package com.sirius.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sirius.domain.ResponseResult;
import com.sirius.domain.dto.LoginUserDto;
import com.sirius.domain.entity.User;

public interface UserService extends IService<User> {

    ResponseResult login(User user);

    ResponseResult register(User user);
}
