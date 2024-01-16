package com.sirius.controller;

import com.sirius.domain.ResponseResult;
import com.sirius.domain.dto.LoginUserDto;
import com.sirius.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody LoginUserDto loginUserDto) {
        return userService.login(loginUserDto);
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
