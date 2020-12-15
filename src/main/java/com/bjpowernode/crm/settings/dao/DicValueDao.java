package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.entity.DicValue;

import java.util.List;

public interface DicValueDao {

    List<DicValue> getListByCode(String code);


}
