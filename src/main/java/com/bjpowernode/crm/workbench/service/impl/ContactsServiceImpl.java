package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.entity.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ContactsActivityRelationDao;
import com.bjpowernode.crm.workbench.dao.ContactsDao;
import com.bjpowernode.crm.workbench.dao.ContactsRemarkDao;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.entity.*;
import com.bjpowernode.crm.workbench.service.ContactsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsServiceImpl implements ContactsService {

    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);


    @Override
    public List<Contacts> getContactListByName(String fullname) {

        List<Contacts> contactsList = contactsDao.getActivityListByNameJust(fullname);

        return contactsList;

    }

    @Override
    public boolean save(Contacts contacts, String customerName) {

        System.out.println("进入到保存联系人的操作");

        /*
         * contacts中保存的数据，缺少一个字段：customerId
         *   根据customerName去customer表中查询，
         *       如果有，则返回id，保存到contacts中
         *       如果没有，则新建一个客户，返回该客户的id，然后保存到contacts中
         *
         * */

        boolean flag = true;

        Customer customer = customerDao.getCustomerByName(customerName);

        if (customer == null){

            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(contacts.getOwner());
            customer.setName(customerName);
            customer.setCreateBy(contacts.getCreateBy());
            customer.setCreateTime(contacts.getCreateTime());
            customer.setContactSummary(contacts.getContactSummary());
            customer.setNextContactTime(contacts.getNextContactTime());
            customer.setDescription(contacts.getDescription());

            //添加客户
            int count1 = customerDao.save(customer);
            if (count1 != 1){
                flag = false;
            }

        }

        //将客户的id封装进contacts对象中，使得contacts对象完整
        contacts.setCustomerId(customer.getId());

        //添加联系人
        int count2 = contactsDao.save(contacts);
        if (count2 != 1){
            flag = false;
        }

        return flag;

    }

    @Override
    public PaginationVO<Contacts> pageList(Map<String, Object> map) {

        //取得total
        int total = contactsDao.getTotalByCondition(map);

        //取得dataList
        List<Contacts> dataList = contactsDao.getContactsByCondition(map);

        //创建一个vo对象，将vo和dataList封装到vo中
        PaginationVO<Contacts> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //将vo返回
        return vo;

    }

    @Override
    public boolean delete(String[] ids) {

        boolean flag = true;

        //查询出需要删除的备注的数量
        int count = contactsRemarkDao.getCountByAids(ids);

        //删除关联该市场活动的备注信息，返回受影响的条数（实际删除的条数）
        int effectCount = contactsRemarkDao.delete(ids);
        if (count != effectCount){
            flag = false;
        }

        //查询出需要产出的记录数
        int count2 = contactsActivityRelationDao.getCountByAids(ids);

        //删除和要删除的联系人在联系人和市场活动关系表中的记录
        int effectCount2 = contactsActivityRelationDao.delete(ids);
        if (count2 != effectCount2){
            flag = false;
        }


        //删除该条市场活动的信息
        int count3 = contactsDao.delete(ids);
        if (count3 != ids.length){
            flag = false;
        }

        return flag;

    }

    @Override
    public Map<String, Object> getUserListAndContacts(String id) {

        //取uList
        List<User> userList = userDao.getUserList();

        //取a
        Contacts contacts = contactsDao.getContactsById(id);

        //将uList和a封装到map中
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("uList",userList);
        map.put("c",contacts);

        //返回map
        return map;

    }

    @Override
    public boolean updateContacts(Contacts contacts, String customerName) {

        System.out.println("进入到修改联系人的操作");

        /*
         * contacts中保存的数据，缺少一个字段：customerId
         *   根据customerName去customer表中查询，
         *       如果有，则返回id，保存到contacts中
         *       如果没有，则新建一个客户，返回该客户的id，然后保存到contacts中
         *
         * */

        boolean flag = true;

        Customer customer = customerDao.getCustomerByName(customerName);

        if (customer == null){

            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(contacts.getOwner());
            customer.setName(customerName);
            customer.setCreateBy(contacts.getCreateBy());
            customer.setCreateTime(contacts.getCreateTime());
            customer.setContactSummary(contacts.getContactSummary());
            customer.setNextContactTime(contacts.getNextContactTime());
            customer.setDescription(contacts.getDescription());

            //添加客户
            int count1 = customerDao.save(customer);
            if (count1 != 1){
                flag = false;
            }

        }

        //将客户的id封装进contacts对象中，使得contacts对象完整
        contacts.setCustomerId(customer.getId());

        //添加交易
        int count2 = contactsDao.updateContacts(contacts);
        if (count2 != 1){
            flag = false;
        }

        return flag;

    }

    @Override
    public Contacts detail(String id) {

        //拿到contacts记录
        Contacts contacts = contactsDao.getDetailById(id);

        return contacts;

    }

    @Override
    public boolean saveReamrk(ContactsRemark contactsRemark) {

        boolean flag = false;
        int count = contactsRemarkDao.save(contactsRemark);
        if (count == 1){
            flag = true;
        }

        return flag;

    }

    @Override
    public List<ContactsRemark> getRemarkListByContactsId(String contactsId) {

        List<ContactsRemark> crList = contactsRemarkDao.getRemarkListByContactsId(contactsId);

        return crList;

    }

    @Override
    public boolean deleteRemark(String id) {

        boolean flag = false;

        int count = contactsRemarkDao.deleteById(id);

        if (count == 1){
            flag = true;
        }

        return flag;

    }

    @Override
    public boolean updateReamrk(ContactsRemark cr) {

        boolean flag = false;
        int count = contactsRemarkDao.updateRemark(cr);
        if (count == 1){
            flag = true;
        }

        return flag;

    }

    @Override
    public List<Contacts> getContactsListByCustomerId(String customerId) {

        List<Contacts> contactsList = contactsDao.getContactsListByCustomerId(customerId);

        return contactsList;

    }


}
