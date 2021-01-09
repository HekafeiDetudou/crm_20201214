package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.entity.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.entity.*;
import com.bjpowernode.crm.workbench.service.ClueService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {

    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    //用户
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    //客户
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    //联系人
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);


    //交易
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);


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

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {

        String createTime = DateTimeUtil.getSysTime();

        boolean flag = true;

        //(1) 通过线索id获取线索对象（线索对象中封装了线索的相关信息）
        Clue clue = clueDao.getById(clueId);

        //(2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String company = clue.getCompany();
        Customer customer = customerDao.getCustomerByName(company);
        if (customer == null){

            customer = new Customer();

            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(clue.getOwner());
            customer.setName(company);
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setContactSummary(clue.getContactSummary());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setDescription(clue.getDescription());
            customer.setAddress(clue.getAddress());

            //添加客户
            int count1 = customerDao.save(customer);
            if (count1 != 1){
                flag = false;
            }
        }

        //(3) 通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setAddress(clue.getAddress());
        //添加联系人
        int count2 = contactsDao.save(contacts);
        if (count2!=1){
            flag = false;
        }

        //(4) 线索备注转换到 客户备注以及 联系人备注
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        if (clueRemarkList != null){

            for (ClueRemark clueRemark : clueRemarkList){

                //客户备注
                CustomerRemark customerRemark = new CustomerRemark();

                customerRemark.setId(UUIDUtil.getUUID());
                customerRemark.setNoteContent(clueRemark.getNoteContent());
                customerRemark.setCreateBy(createBy);
                customerRemark.setCreateTime(createTime);
                customerRemark.setEditFlag(String.valueOf(0));
                customerRemark.setCustomerId(customer.getId());
                int count3 = customerRemarkDao.save(customerRemark);

                //联系人备注
                ContactsRemark contactsRemark = new ContactsRemark();

                contactsRemark.setId(UUIDUtil.getUUID());
                contactsRemark.setNoteContent(clueRemark.getNoteContent());
                contactsRemark.setCreateBy(createBy);
                contactsRemark.setCreateTime(createTime);
                contactsRemark.setEditFlag(String.valueOf(0));
                contactsRemark.setContactsId(contacts.getId());
                int count4 = contactsRemarkDao.save(contactsRemark);

                if (count3!=1 || count4!=1){

                    flag = false;

                }
            }

        }


        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        //查询出与该条线索关联的市场活动，查询与市场活动的关联关系列表
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        if (clueActivityRelationList != null){

            //遍历出每一条与市场活动关联的关联关系记录
            for (ClueActivityRelation clueActivityRelation : clueActivityRelationList){

                //从每一条遍历出来的记录中取出关联的市场活动id
                String activityId = clueActivityRelation.getActivityId();

                //创建联系人与市场活动的关联关系对象，让第三步生成的联系人与市场活动做关联
                ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
                contactsActivityRelation.setId(UUIDUtil.getUUID());
                contactsActivityRelation.setActivityId(activityId);
                contactsActivityRelation.setContactsId(contacts.getId());

                //添加联系人与市场活动的关联关系
                int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
                if (count5 != 1){
                    flag = false;
                }

            }

        }


        //（6）如果有创建交易的需求，创建一笔交易
        if (t != null){

            t.setSource(clue.getSource());
            t.setOwner(clue.getOwner());
            t.setNextContactTime(clue.getNextContactTime());
            t.setDescription(clue.getDescription());
            t.setCustomerId(customer.getId());
            t.setContactSummary(clue.getContactSummary());
            t.setContactsId(contacts.getId());
            //添加交易
            int count6 = tranDao.save(t);
            if (count6 != 1){flag=false;}

            //(7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(createTime);
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setStage(t.getStage());
            tranHistory.setTranId(t.getId());
            //添加交易历史
            int count7 = tranHistoryDao.save(tranHistory);
            if (count7 != 1){
                flag = false;
            }

        }

        //(8) 删除线索备注
        if (clueRemarkList != null){
            for (ClueRemark clueRemark : clueRemarkList){

                int count8 = clueRemarkDao.delete(clueRemark);
                if (count8 != 1){
                    flag = false;
                }

            }
        }

        //(9) 删除线索和市场活动的关系
        if (clueActivityRelationList != null){

            for (ClueActivityRelation clueActivityRelation : clueActivityRelationList){

                int count9 = clueActivityRelationDao.delete(clueActivityRelation);
                if (count9 != 1){
                    flag = false;
                }

            }

        }


        //(10) 删除线索
        if (clueId != null){

            int count10 = clueDao.delete(clueId);
            if (count10 != 1){
                flag = false;
            }

        }

        return flag;

    }

    @Override
    public Map<String, Object> getUserListAndClue(String id) {

        //取得用户信息列表
        List<User> userList = userDao.getUserList();

        //取得单条线索记录
        Clue clue = clueDao.getClueById2(id);

        //将用户信息列表和线索记录，全部放到map中
        Map<String,Object> map = new HashMap<>();
        map.put("uList",userList);
        map.put("c",clue);

        //返回map
        return map;

    }

    @Override
    public boolean updateClue(Clue clue) {

        boolean flag = true;
        int count = clueDao.updateClue(clue);
        if (count != 1){
            //添加失败
            flag = false;
        }
        return flag;

    }

    @Override
    public boolean delete(String[] ids) {

        boolean flag = true;

        //查询出需要删除的备注的数量
        int count = clueRemarkDao.getCountByAids(ids);

        //删除关联该市场活动的备注信息，返回受影响的条数（实际删除的条数）
        int effectCount = clueRemarkDao.delete2(ids);
        if (count != effectCount){
            flag = false;
        }

        //查询出需要删除的线索和市场活动关系的记录数量
        int count2 = clueActivityRelationDao.getCountByAids(ids);

        //删除线索和市场活动关系的记录
        int effectCount2 = clueActivityRelationDao.delete2(ids);
        if (count2 != effectCount2){
            flag = false;
        }

        //删除该条市场活动的信息
        int count3 = clueDao.delete2(ids);
        if (count3 != ids.length){
            flag = false;
        }

        return flag;

    }

    @Override
    public boolean saveReamrk(ClueRemark clueRemark) {

        boolean flag = false;
        int count = clueRemarkDao.saveRemark(clueRemark);
        if (count == 1){
            flag = true;
        }

        return flag;

    }

    @Override
    public List<ClueRemark> getRemarkListByClueId(String clueId) {

        List<ClueRemark> crList = clueRemarkDao.getRemarkListByClueId(clueId);

        return crList;

    }

    @Override
    public boolean deleteRemark(String id) {

        boolean flag = false;

        int count = clueRemarkDao.deleteById(id);

        if (count == 1){
            flag = true;
        }

        return flag;

    }

    @Override
    public boolean updateReamrk(ClueRemark cr) {

        boolean flag = false;
        int count = clueRemarkDao.updateRemark(cr);
        if (count == 1){
            flag = true;
        }
        return flag;

    }

}

