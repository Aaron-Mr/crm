package com.wangxiaoqi.workbench.dao;


import com.wangxiaoqi.workbench.domain.Activity;
import com.wangxiaoqi.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {

    int save(Clue clue);

    int getTotal(Map<String, Integer> map);

    List<Clue> getClueList(Map<String, Integer> map);

    Clue detail(String id);

    Clue getClueById(String clueId);

    int delete(Clue clue);
}
