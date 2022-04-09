package com.wangxiaoqi.workbench.service;

import com.wangxiaoqi.vo.PaginationVo;
import com.wangxiaoqi.workbench.domain.Activity;
import com.wangxiaoqi.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    boolean save(Clue clue);

    PaginationVo<Clue> pageList(Map<String, Integer> map);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(String cid, String[] aids);
}
