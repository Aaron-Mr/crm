package com.wangxiaoqi.web.filter;

import com.wangxiaoqi.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//过滤器，防止恶意操作
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        //获取当前访问路径
        String path = request.getServletPath();

        //放行前端登录页及后端登录操作
        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            filterChain.doFilter(request,response);
        }else {

            //在登录成功后，我们将查询到的用户放入到session中，如果这里的session中有user，证明已经登录过
            User user = (User) request.getSession().getAttribute("user");
            if (user != null) {
                filterChain.doFilter(request,response);
            }else {
                response.sendRedirect("/crm/login.jsp");
            }

        }
    }
}
