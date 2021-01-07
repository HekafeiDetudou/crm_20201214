package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.entity.Tran;
import com.bjpowernode.crm.workbench.entity.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {

    boolean save(Tran tran, String customerName);

    PaginationVO<Tran> pageList(Map<String, Object> map);

    Tran detail(String id);

    List<TranHistory> getHistoryListByTranId(String id);

    Map<String, Object> getCharts();

}
