package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.entity.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    int unbound(String id);

    int bound(ClueActivityRelation car);

    List<ClueActivityRelation> getListByClueId(String clueId);

    int delete(ClueActivityRelation clueActivityRelation);

    int getCountByAids(String[] ids);

    int delete2(String[] ids);

}
