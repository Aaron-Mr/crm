package com.wangxiaoqi.workbench.dao;

public interface ActivityRemarkDao {
    int getActivityRemarkByIds(String[] ids);

    int deleteActivityRemarkByIds(String[] ids);

    int deleteActivityByIds(String[] ids);
}
