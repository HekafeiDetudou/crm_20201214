package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.entity.ClueActivityRelation;

public interface ClueActivityRelationDao {


    int unbound(String id);

    int bound(ClueActivityRelation car);

}
