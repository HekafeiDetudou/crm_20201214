package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ClueDao;
import com.bjpowernode.crm.workbench.entity.Clue;
import com.bjpowernode.crm.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {

    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);

    @Override
    public boolean save(Clue clue) {

        boolean flag = false;

        int count = clueDao.save(clue);

        if (count == 1){
            flag = true;
        }

        return flag;

    }

    @Override
    public PaginationVO<Clue> pageList(Map<String, Object> map) {

        //取得totoal
        int total = clueDao.getTotalByCondition(map);

        //取得dataList
        List<Clue> clueList = clueDao.getClueListByCondition(map);

        //将total和dataList封装到vo中
        PaginationVO<Clue> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(clueList);

        //返回vo
        return vo;

    }

    @Override
    public Clue detail(String id) {

        //取得clue信息
        Clue clue = clueDao.getClueById(id);

        //返回clue
        return clue;

    }
}
