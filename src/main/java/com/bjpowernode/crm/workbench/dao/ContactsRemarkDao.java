package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.entity.ContactsRemark;

import java.util.List;

public interface ContactsRemarkDao {

    int save(ContactsRemark contactsRemark);

    int getCountByAids(String[] ids);

    int delete(String[] ids);

    List<ContactsRemark> getRemarkListByContactsId(String contactsId);

    int deleteById(String id);

    int updateRemark(ContactsRemark cr);

}
