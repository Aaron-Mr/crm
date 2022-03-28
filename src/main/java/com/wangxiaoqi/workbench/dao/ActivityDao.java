package com.wangxiaoqi.workbench.dao;

import com.wangxiaoqi.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity activity);

    List<Activity> getActivityListByCondition(Map<String, Object> map);

    int getTotalByCondition(Map<String, Object> map);


    Activity getActivityById(String id);

    int edit(Activity activity);

    Activity getById(String id);
}
