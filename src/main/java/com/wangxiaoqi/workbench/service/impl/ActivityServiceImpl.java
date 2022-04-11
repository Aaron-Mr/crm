package com.wangxiaoqi.workbench.service.impl;

import com.wangxiaoqi.settings.dao.UserDao;
import com.wangxiaoqi.settings.domain.User;
import com.wangxiaoqi.utils.SqlSessionUtil;
import com.wangxiaoqi.vo.PaginationVo;
import com.wangxiaoqi.workbench.dao.ActivityDao;
import com.wangxiaoqi.workbench.dao.ActivityRemarkDao;
import com.wangxiaoqi.workbench.domain.Activity;
import com.wangxiaoqi.workbench.domain.ActivityRemark;
import com.wangxiaoqi.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public Boolean save(Activity activity) {

        Boolean flag = true;

        int count = activityDao.save(activity);

        if (count != 1) {
            flag = false;
        }

        return flag;
    }

    @Override
    public PaginationVo<Activity> pageList(Map<String, Object> map) {

        //获得总条数
        int total = activityDao.getTotalByCondition(map);

        //获得市场活动集合
        List<Activity> activityList = activityDao.getActivityListByCondition(map);

        //封装成vo对象
        PaginationVo vo = new PaginationVo();
        vo.setTotal(total);
        vo.setDataList(activityList);

        return vo;
    }

    @Override
    public Boolean delete(String[] ids) {
        boolean flag = false;

        //查询市场活动备注的数量
        int count1 = activityRemarkDao.getActivityRemarkByIds(ids);

        //删除市场活动备注，并返回删除的数量
        int count2 = activityRemarkDao.deleteActivityRemarkByIds(ids);

        //删除市场活动
        int count3 = activityRemarkDao.deleteActivityByIds(ids);

        if (count3 == ids.length){
            flag = true;
        }

        return flag;
    }



    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        //获取用户名列表
        List<User> userList = userDao.getUserList();

        //根据id查市场活动
        Activity activity = activityDao.getActivityById(id);

        //封装到map中
        Map<String,Object> map = new HashMap<>();
        map.put("uList",userList);
        map.put("a",activity);

        return map;
    }

    @Override
    public Boolean edit(Activity activity) {
        Boolean flag = false;

        int count = activityDao.edit(activity);

        if (count == 1){
            flag = true;
        }

        return flag;
    }

    @Override
    public Activity detail(String id) {

        Activity activity = activityDao.getById(id);

        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkList(String activityId) {

        List<ActivityRemark> remarkList = activityRemarkDao.getRemarkList(activityId);

        return remarkList;
    }

    @Override
    public boolean deleteRemark(String id) {

        boolean flag = false;

        int count = activityRemarkDao.deleteRemark(id);

        if (count == 1){
            flag = true;
        }

        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark activityRemark) {

        boolean flag = false;

        int count = activityRemarkDao.saveRemark(activityRemark);

        if (count == 1) {
            flag = true;
        }

        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark activityRemark) {

        boolean flag = true;

        int count = activityRemarkDao.updateRemark(activityRemark);

        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public List<Activity> getActivityByClue(String id) {

        List<Activity> activityList = activityDao.getActivityByClue(id);

        return activityList;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {
        List<Activity> activityList = activityDao.getActivityListByNameAndNotByClueId(map);
        return activityList;
    }

    @Override
    public List<Activity> getActivityListByName(String activityName) {
        List<Activity> activityList = activityDao.getActivityListByName(activityName);
        return activityList;
    }


}
