package com.wangxiaoqi.settings.web.controller;

import com.wangxiaoqi.exception.LoginException;
import com.wangxiaoqi.settings.domain.User;
import com.wangxiaoqi.settings.service.UserService;
import com.wangxiaoqi.settings.service.impl.UserServiceImpl;
import com.wangxiaoqi.utils.MD5Util;
import com.wangxiaoqi.utils.PrintJson;
import com.wangxiaoqi.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    /**
     * service方法既可以处理get请求，也可以处理post请求
     * @param request 请求对象
     * @param response 响应对象
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("---进入用户控制器---");

        //获取访问地址
        String path = request.getServletPath();
        System.out.println(path);
        //通过不同的访问地址，对应的进行相应的操作
        if ("/settings/user/login.do".equals(path)){
            login(request,response);
        }


    }

    //登录验证
    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("---登录验证操作---");
        //获得前端传过来的用户名、密码以及浏览器IP地址
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        String ip = request.getRemoteAddr();

        loginPwd = MD5Util.getMD5(loginPwd);
        System.out.println("1");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        System.out.println("2");
        try {
            //验证登录名和密码，若正确，返回一个user对象
            User user = us.login(loginAct,loginPwd,ip);
            System.out.println("没有异常");
            //若程序能执行到这里，证明login方法没抛异常
            request.getSession().setAttribute("user",user);
            //向前端返回true
            PrintJson.printJsonFlag(response,true);

        } catch (LoginException e) {
            System.out.println("异常");
            e.printStackTrace();

            //返回错误信息
            String msg = e.getMessage();
            System.out.println("-------------");
            System.out.println(msg);

            // {"success":false, msg:错误信息}
            Map<String,Object> map = new HashMap<>();
            map.put("success",false);
            map.put("msg",msg);

            //向前端返回数据
            PrintJson.printJsonObj(response,map);

        }



    }
}
