package com.wangxiaoqi.workbench.service.impl;

import com.wangxiaoqi.utils.SqlSessionUtil;
import com.wangxiaoqi.utils.UUIDUtil;
import com.wangxiaoqi.vo.PaginationVo;
import com.wangxiaoqi.workbench.dao.ClueActivityRelationDao;
import com.wangxiaoqi.workbench.dao.ClueDao;
import com.wangxiaoqi.workbench.domain.Activity;
import com.wangxiaoqi.workbench.domain.Clue;
import com.wangxiaoqi.workbench.domain.ClueActivityRelation;
import com.wangxiaoqi.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {

    ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    @Override
    public boolean save(Clue clue) {
        boolean flag = true;

        int count = clueDao.save(clue);

        if (count != 1) {
            flag = false;
        }

        return flag;
    }

    @Override
    public PaginationVo<Clue> pageList(Map<String, Integer> map) {

        //获取总条数
        int total = clueDao.getTotal(map);

        //获取线索对象List
        List<Clue> clueList = clueDao.getClueList(map);

        PaginationVo<Clue> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(clueList);

        return vo;
    }

    @Override
    public Clue detail(String id) {

        Clue clue = clueDao.detail(id);

        return clue;
    }

    @Override
    public boolean unbund(String id) {

        boolean flag = true;

        int count = clueActivityRelationDao.unbund(id);

        if (count != 1) {
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean bund(String cid, String[] aids) {

        boolean flag = true;

        for (String aid : aids){
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(cid);
            car.setActivityId(aid);

            int count = clueActivityRelationDao.bund(car);

            if (count != 1){
                flag = false;
            }
        }

        return flag;
    }


}
