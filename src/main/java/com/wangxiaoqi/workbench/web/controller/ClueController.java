package com.wangxiaoqi.workbench.web.controller;
import com.wangxiaoqi.settings.domain.User;
import com.wangxiaoqi.settings.service.UserService;
import com.wangxiaoqi.settings.service.impl.UserServiceImpl;
import com.wangxiaoqi.utils.*;
import com.wangxiaoqi.vo.PaginationVo;
import com.wangxiaoqi.workbench.dao.ClueDao;
import com.wangxiaoqi.workbench.domain.Activity;
import com.wangxiaoqi.workbench.domain.Clue;
import com.wangxiaoqi.workbench.service.ActivityService;
import com.wangxiaoqi.workbench.service.ClueService;
import com.wangxiaoqi.workbench.service.impl.ActivityServiceImpl;
import com.wangxiaoqi.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("---进入线索控制器---");

        //获取到访问路径
        String path = request.getServletPath();
        System.out.println(path);
        if ("/workbench/clue/getUserList.do".equals(path)){
            getUserList(request,response);
        } else if ("/workbench/clue/save.do".equals(path)) {
            save(request,response);
        }else if ("/workbench/clue/pageList.do".equals(path)) {
            pageList(request,response);
        }else if ("/workbench/clue/detail.do".equals(path)) {
            detail(request,response);
        }else if ("/workbench/clue/getActivityByClue.do".equals(path)) {
            getActivityByClue(request,response);
        }else if ("/workbench/clue/unbund.do".equals(path)) {
            unbund(request,response);
        }else if ("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)) {
            getActivityListByNameAndNotByClueId(request,response);
        }else if ("/workbench/clue/bund.do".equals(path)) {
            bund(request,response);
        }

    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入关联操作");

        String aids[] = request.getParameterValues("aid");
        String cid = request.getParameter("cid");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.bund(cid,aids);

        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取未关联的市场活动");

        //获取前端传过来的参数
        String aname = request.getParameter("aname");
        String clueId = request.getParameter("clueId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Map<String,String> map = new HashMap<>();
        map.put("aname",aname);
        map.put("clueId",clueId);

        List<Activity> activityList = as.getActivityListByNameAndNotByClueId(map);

        PrintJson.printJsonObj(response,activityList);
    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("线索解除关联市场活动");

        //获取前端传过来的id
        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.unbund(id);

        PrintJson.printJsonFlag(response,flag);

    }

    private void getActivityByClue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取到线索关联的市场活动");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        //获取当前线索的id
        String id = request.getParameter("id");

        List<Activity> activityList = as.getActivityByClue(id);

        PrintJson.printJsonObj(response,activityList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到详细信息页");

        //获取到前端传过来的id
        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Clue clue = cs.detail(id);

        request.setAttribute("c",clue);

        request.getRequestDispatcher("detail.jsp").forward(request,response);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("--- 线索分页显示 ---");

        //获取前端传过来的参数
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");

        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);

        //计算略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Integer> map = new HashMap<>();

        map.put("pageNo",pageNo);
        map.put("pageSize",pageSize);
        map.put("skipCount",skipCount);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        PaginationVo<Clue> vo = cs.pageList(map);

        PrintJson.printJsonObj(response,vo);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("--- 保存线索 ---");

        //获取前端传过来的参数
        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime  = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue clue = new Clue();
        clue.setAddress(address);
        clue.setWebsite(website);
        clue.setState(state);
        clue.setSource(source);
        clue.setPhone(phone);
        clue.setOwner(owner);
        clue.setNextContactTime(nextContactTime);
        clue.setMphone(mphone);
        clue.setJob(job);
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setEmail(email);
        clue.setDescription(description);
        clue.setCreateTime(createTime);
        clue.setCreateBy(createBy);
        clue.setAppellation(appellation);
        clue.setCompany(company);
        clue.setContactSummary(contactSummary);

        System.out.println(owner);
        System.out.println(createBy);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.save(clue);

        PrintJson.printJsonFlag(response,flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("--- 获取用户信息 ---");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> list = us.getList();

        PrintJson.printJsonObj(response,list);

    }


}
