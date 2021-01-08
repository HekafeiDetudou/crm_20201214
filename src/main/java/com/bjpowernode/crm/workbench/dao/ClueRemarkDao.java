package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.entity.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getListByClueId(String clueId);

    int delete(ClueRemark clueRemark);

    int getCountByAids(String[] ids);

    int delete2(String[] ids);

}
