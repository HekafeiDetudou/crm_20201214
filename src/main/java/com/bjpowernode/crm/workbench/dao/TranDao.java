package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.entity.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran t);

    int getTotalByCondition(Map<String, Object> map);

    List<Tran> getTranByCondition(Map<String, Object> map);

    Tran getTranById(String id);

}
