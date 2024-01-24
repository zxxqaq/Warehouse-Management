package com.sirius.utils;


import com.sirius.domain.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class SecurityUtils {
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static LoginUser getLoginUser(){
        return (LoginUser) getAuthentication().getPrincipal();
    }

    public static Long getUserId(){
        return getLoginUser().getUser().getUserId();
    }

}