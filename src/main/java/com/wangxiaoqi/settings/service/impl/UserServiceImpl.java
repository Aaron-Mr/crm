package com.wangxiaoqi.settings.service.impl;

import com.wangxiaoqi.exception.LoginException;
import com.wangxiaoqi.settings.dao.UserDao;
import com.wangxiaoqi.settings.domain.User;
import com.wangxiaoqi.settings.service.UserService;
import com.wangxiaoqi.utils.DateTimeUtil;
import com.wangxiaoqi.utils.SqlSessionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    /**
     * 登录验证
     * @param loginAct 登录名
     * @param loginPwd 登录密码
     * @param ip 浏览器IP地址
     * @return 将查询到的结果封装成user对象返回
     * @throws LoginException 自定义异常
     */
    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        System.out.println("---业务层---");
        Map<String,Object> map = new HashMap<>();

        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        User user = userDao.login(map);

        //如果是null，表示没有从数据库中查询到数据
        if (user == null) {
            throw new LoginException("账号密码有误");
        }

        //能执行到这里，证明从数据库中查询到数据了
        String nowTime = DateTimeUtil.getSysTime();
        //如果失效时间比当前时间小的话，证明已过期
        if( user.getExpireTime().compareTo(nowTime) < 0 ){
            throw new LoginException("账号已失效");
        }

        //验证账号状态
        String lockState = user.getLockState();
        if ( "0".equals(lockState) ){
            throw new LoginException("账号已锁定");
        }

        //验证IP地址
        if ( !user.getAllowIps().contains(ip) ){
            throw new LoginException("IP地址受限");
        }

        System.out.println("---正常---");
        //到这里证明程序没出异常
        return user;

    }

    @Override
    public List<User> getList() {
        List<User> list = userDao.getUserList();

        return list;
    }
}
