package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.entity.TranRemark;

import java.util.List;

public interface TranRemarkDao {

    int getCountByAids(String[] ids);

    int delete(String[] ids);

    List<TranRemark> getRemarkListByTranId(String tranId);

    int saveRemark(TranRemark tranRemark);

    int deleteById(String id);

    int updateRemark(TranRemark tranRemark);

}
