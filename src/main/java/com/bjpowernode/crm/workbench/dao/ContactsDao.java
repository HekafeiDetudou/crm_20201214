package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.entity.Contacts;

import java.util.List;
import java.util.Map;

public interface ContactsDao {

    int save(Contacts contacts);

    List<Contacts> getActivityListByNameJust(String fullname);

    int getTotalByCondition(Map<String, Object> map);

    List<Contacts> getContactsByCondition(Map<String, Object> map);

    int delete(String[] ids);

    Contacts getContactsById(String id);

    int updateContacts(Contacts contacts);

    Contacts getDetailById(String id);

    List<Contacts> getContactsListByCustomerId(String customerId);
}
