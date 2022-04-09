package com.wangxiaoqi.settings.service;

import com.wangxiaoqi.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicService {
    Map<String, List<DicValue>> getAll();
}
