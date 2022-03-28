package com.wangxiaoqi.settings.service;

import com.wangxiaoqi.exception.LoginException;
import com.wangxiaoqi.settings.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {

    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getList();
}
