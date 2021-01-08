package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.entity.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue clue);

    int getTotalByCondition(Map<String, Object> map);

    List<Clue> getClueListByCondition(Map<String, Object> map);


    Clue getClueById(String id);

    Clue getById(String clueId);

    int delete(String clueId);

    Clue getClueById2(String id);

    int updateClue(Clue clue);

    int delete2(String[] ids);

}
