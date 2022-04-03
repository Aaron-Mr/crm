package com.wangxiaoqi.workbench.dao;

import com.wangxiaoqi.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int getActivityRemarkByIds(String[] ids);

    int deleteActivityRemarkByIds(String[] ids);

    int deleteActivityByIds(String[] ids);

    List<ActivityRemark> getRemarkList(String activityId);

    int deleteRemark(String id);

    int saveRemark(ActivityRemark activityRemark);

    int updateRemark(ActivityRemark activityRemark);
}
