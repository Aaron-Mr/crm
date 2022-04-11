package com.wangxiaoqi.workbench.service;

import com.wangxiaoqi.vo.PaginationVo;
import com.wangxiaoqi.workbench.domain.Activity;
import com.wangxiaoqi.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    Boolean save(Activity activity);

    PaginationVo<Activity> pageList(Map<String, Object> map);

    Boolean delete(String[] ids);


    Map<String, Object> getUserListAndActivity(String id);

    Boolean edit(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkList(String activityId);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark activityRemark);

    boolean updateRemark(ActivityRemark activityRemark);

    List<Activity> getActivityByClue(String id);

    List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);

    List<Activity> getActivityListByName(String activityName);
}
