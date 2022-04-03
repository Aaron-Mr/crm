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
import com.wangxiaoqi.workbench.domain.ActivityRemark;
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
        }else if ("/workbench/activity/getRemarkList.do".equals(path)) {
            System.out.println("---获取备注列表---");
            getRemarkList(request,response);
        }else if ("/workbench/activity/deleteRemark.do".equals(path)) {
            System.out.println("---悬浮窗删除备注---");
            deleteRemark(request,response);
        }else if ("/workbench/activity/updateRemark.do".equals(path)) {
            System.out.println("---更新备注---");
            updateRemark(request,response);
        }else if ("/workbench/activity/saveRemark.do".equals(path)) {
            System.out.println("---添加备注---");
            saveRemark(request,response);
        }


    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        //获取前端传过来的备注id和新备注内容
        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");

        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        //封装成一个对象
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setEditTime(editTime);
        activityRemark.setEditBy(editBy);
        activityRemark.setEditFlag(editFlag);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.updateRemark(activityRemark);

        PrintJson.printJsonFlag(response,flag);

    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        //获取前端传过来的参数
        String noteContent = request.getParameter("noteContent");
        String activityId = request.getParameter("activityId");

        System.out.println(noteContent);
        System.out.println(activityId);

        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        //封装成一个activityRemark对象
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setActivityId(activityId);
        activityRemark.setCreateTime(createTime);
        activityRemark.setCreateBy(createBy);
        activityRemark.setEditFlag(editFlag);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.saveRemark(activityRemark);

        PrintJson.printJsonFlag(response,flag);

    }


    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        //拿到前端传过来的参数
        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.deleteRemark(id);

        PrintJson.printJsonFlag(response,flag);

    }

    private void getRemarkList(HttpServletRequest request, HttpServletResponse response) {
        //拿到前端传过来的参数
        String activityId = request.getParameter("activityId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<ActivityRemark> activityRemarkList = as.getRemarkList(activityId);

        PrintJson.printJsonObj(response,activityRemarkList);
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
