package com.wangxiaoqi.workbench.dao;

import com.wangxiaoqi.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {


    List<ClueRemark> getRemarkByClueId(String clueId);

    int delete(ClueRemark clueRemark);
}
