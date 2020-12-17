package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.entity.Clue;

import java.util.Map;

public interface ClueService {

    boolean save(Clue clue);


    PaginationVO<Clue> pageList(Map<String, Object> map);

    Clue detail(String id);


    boolean unbound(String id);

    boolean bound(Map<String, Object> map);

}
