package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.entity.DicValue;

import java.util.List;
import java.util.Map;

public interface DicService {
    Map<String, List<DicValue>> getAll();

}
