package com.wangxiaoqi.workbench.dao;

import com.wangxiaoqi.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {

    int unbund(String id);

    int bund(ClueActivityRelation car);

    List<ClueActivityRelation> getActivityByClueId(String clueId);

    int delete(String clueId);
}
