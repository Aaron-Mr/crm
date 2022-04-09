package com.wangxiaoqi.settings.service.impl;

import com.wangxiaoqi.settings.dao.DicTypeDao;
import com.wangxiaoqi.settings.dao.DicValueDao;
import com.wangxiaoqi.settings.domain.DicType;
import com.wangxiaoqi.settings.domain.DicValue;
import com.wangxiaoqi.settings.service.DicService;
import com.wangxiaoqi.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);
    DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);

    /**
     * 在监听器中处理数据字典
     * @return 将数据字典值封装成map
     */
    @Override
    public Map<String, List<DicValue>> getAll() {

        Map<String, List<DicValue>> map = new HashMap<>();
        //获取所有的字典类型
        List<DicType> dicTypeList = dicTypeDao.getTypeList();

        //遍历字典类型，然后通过每个类型，获取到相应的字典数据，并存入map中
        for (DicType type:dicTypeList){
            String code = type.getCode();
            List<DicValue> dicValueList = dicValueDao.getValueListByCode(code);

            map.put(code+"List",dicValueList);
        }

        return map;
    }
}
