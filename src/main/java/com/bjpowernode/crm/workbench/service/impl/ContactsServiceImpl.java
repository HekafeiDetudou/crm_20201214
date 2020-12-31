package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.workbench.dao.ContactsActivityRelationDao;
import com.bjpowernode.crm.workbench.dao.ContactsDao;
import com.bjpowernode.crm.workbench.dao.ContactsRemarkDao;
import com.bjpowernode.crm.workbench.entity.Contacts;
import com.bjpowernode.crm.workbench.service.ContactsService;

import java.util.List;

public class ContactsServiceImpl implements ContactsService {

    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    @Override
    public List<Contacts> getContactListByName(String fullname) {

        List<Contacts> contactsList = contactsDao.getActivityListByNameJust(fullname);

        return contactsList;

    }
}
