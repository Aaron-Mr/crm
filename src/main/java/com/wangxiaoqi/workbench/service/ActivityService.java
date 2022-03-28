package com.wangxiaoqi.workbench.service;

import com.wangxiaoqi.vo.PaginationVo;
import com.wangxiaoqi.workbench.domain.Activity;

import java.util.Map;

public interface ActivityService {
    Boolean save(Activity activity);

    PaginationVo<Activity> pageList(Map<String, Object> map);

    Boolean delete(String[] ids);


    Map<String, Object> getUserListAndActivity(String id);

    Boolean edit(Activity activity);

    Activity detail(String id);
}
