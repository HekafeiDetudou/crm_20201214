package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.entity.Tran;
import com.bjpowernode.crm.workbench.entity.TranHistory;
import com.bjpowernode.crm.workbench.entity.TranRemark;

import java.util.List;
import java.util.Map;

public interface TranService {

    boolean save(Tran tran, String customerName);

    PaginationVO<Tran> pageList(Map<String, Object> map);

    Tran detail(String id);

    List<TranHistory> getHistoryListByTranId(String id);

    Map<String, Object> getCharts();

    boolean delete(String[] ids);

    List<TranRemark> getRemarkListByTranId(String tranId);

    boolean saveReamrk(TranRemark tranRemark);

    boolean deleteRemark(String id);

    boolean updateReamrk(TranRemark tranRemark);

    List<Tran> getTranListByCustomerId(String customerId);

    List<Tran> getTranListByContactsId(String contactsId);

    boolean edit(Tran tran, String customerName);

    Tran detail2(String id);

    Map<String, Object> getCharts2();

}
