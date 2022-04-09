package com.wangxiaoqi.workbench.dao;

import com.wangxiaoqi.workbench.domain.ClueActivityRelation;

public interface ClueActivityRelationDao {

    int unbund(String id);

    int bund(ClueActivityRelation car);
}
