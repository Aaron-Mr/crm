package com.wangxiaoqi.settings.dao;

import com.wangxiaoqi.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getValueListByCode(String code);
}
