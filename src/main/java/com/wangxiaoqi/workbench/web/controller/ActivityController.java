package com.wangxiaoqi.workbench.web.controller;

import com.wangxiaoqi.settings.domain.User;
import com.wangxiaoqi.settings.service.UserService;
import com.wangxiaoqi.settings.service.impl.UserServiceImpl;
import com.wangxiaoqi.utils.DateTimeUtil;
import com.wangxiaoqi.utils.PrintJson;
import com.wangxiaoqi.utils.ServiceFactory;
import com.wangxiaoqi.utils.UUIDUtil;
import com.wangxiaoqi.vo.PaginationVo;
import com.wangxiaoqi.workbench.domain.Activity;
import com.wangxiaoqi.workbench.service.ActivityService;
import com.wangxiaoqi.workbench.service.impl.ActivityServiceImpl;
import org.apache.ibatis.binding.MapperMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("---进入市场活动控制器---");

        //获取到访问路径
        String path = request.getServletPath();
        System.out.println(path);
        if ("/workbench/activity/getUserList.do".equals(path)){
            System.out.println("---获取用户列表---");
            getUserList(request,response);
        } else if ("/workbench/activity/save.do".equals(path)) {
            System.out.println("---新建市场活动操作---");
            save(request,response);
        } else if ("/workbench/activity/pageList.do".equals(path)) {
            System.out.println("---分页查询及条件查询---");
            pageList(request,response);
        } else if ("/workbench/activity/delete.do".equals(path)) {
            System.out.println("---市场活动删除操作---");
            delete(request,response);
        }else if ("/workbench/activity/edit.do".equals(path)) {
            System.out.println("---市场活动修改操作---");
            edit(request,response);
        }else if ("/workbench/activity/getUserListAndActivity.do".equals(path)) {
            System.out.println("---修改模态窗口铺值---");
            getUserListAndActivity(request,response);
        }else if ("/workbench/activity/detail.do".equals(path)) {
            System.out.println("---跳转到市场活动详细信息页---");
            detail(request,response);
        }
        

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取前端传过来的参数id
        String id = request.getParameter("id");
        System.out.println(id);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity activity = as.detail(id);

        request.setAttribute("a",activity);

        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);

    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        //根据id查单条
        //获得前端传过来的参数id
        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*
        * 前端需要的数据：
        *   userList activity
        *   使用map集合封装
        * */
        Map<String,Object> map = as.getUserListAndActivity(id);

        PrintJson.printJsonObj(response,map);

    }

    private void edit(HttpServletRequest request, HttpServletResponse response) {
        //获得前端传过来的参数
        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //修改时间应该是当前时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人应该是当前用户
        String editBy = ((User) request.getSession().getAttribute("user")).getName();

        //封装成一个activity对象
        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Boolean flag = as.edit(activity);

        PrintJson.printJsonFlag(response,flag);

    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        //接收前端传过来的参数
        String ids[] = request.getParameterValues("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Boolean flag = as.delete(ids);
        PrintJson.printJsonFlag(response,flag);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        //接收前端传过来的参数
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");

        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);

        //计算略过的数量
        int skipCount = (pageNo-1)*pageSize;

        //将参数封装到map中
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        PaginationVo<Activity> vo = as.pageList(map);

        PrintJson.printJsonObj(response,vo);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        //获取前端传过来的参数
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");

        //使用UUID生成一个id
        String id = UUIDUtil.getUUID();

        //创建时间应该是系统当前时间
        String createTime = DateTimeUtil.getSysTime();

        //创建人是当前登录的用户
        String createBy = ((User)(request.getSession().getAttribute("user"))).getName();

        //将前端传过来的参数封装成一个对象
        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag = as.save(activity);
        PrintJson.printJsonFlag(response,flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> list = us.getList();
        PrintJson.printJsonObj(response,list);
    }
}
