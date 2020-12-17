package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ClueActivityRelationDao;
import com.bjpowernode.crm.workbench.dao.ClueDao;
import com.bjpowernode.crm.workbench.entity.Clue;
import com.bjpowernode.crm.workbench.entity.ClueActivityRelation;
import com.bjpowernode.crm.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {

    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
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

    @Override
    public boolean unbound(String id) {

        boolean flag = false;

        int count = clueActivityRelationDao.unbound(id);

        if (count == 1){

            flag = true;

        }

        return flag;

    }

    @Override
    public boolean bound(Map<String, Object> map) {

        boolean flag = false;

        String cid = (String) map.get("cid");
        String[] aids = (String[]) map.get("aids");

        for (String aid : aids){

            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setClueId(cid);

            int count = clueActivityRelationDao.bound(car);

            if (count == 1){
                flag = true;
            }

        }

        return flag;

    }
}
