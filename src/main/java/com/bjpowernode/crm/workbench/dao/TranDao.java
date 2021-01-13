package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.entity.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran t);

    int getTotalByCondition(Map<String, Object> map);

    List<Tran> getTranByCondition(Map<String, Object> map);

    Tran getTranById(String id);

    int getTotal();

    List<Map<String, Object>> getCharts();

    int delete(String[] ids);

    List<Tran> getTranListByCustomerId(String customerId);

    List<Tran> getTranListByContactsId(String contactsId);

    int update(Tran tran);

    Tran getACIdById(String id);

}
