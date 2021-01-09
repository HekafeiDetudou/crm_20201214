package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.entity.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getListByClueId(String clueId);

    int delete(ClueRemark clueRemark);

    int getCountByAids(String[] ids);

    int delete2(String[] ids);

    int saveRemark(ClueRemark clueRemark);

    List<ClueRemark> getRemarkListByClueId(String clueId);

    int deleteById(String id);

    int updateRemark(ClueRemark cr);

}
