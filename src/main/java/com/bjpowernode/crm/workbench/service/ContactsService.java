package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.entity.Contacts;
import com.bjpowernode.crm.workbench.entity.ContactsRemark;

import java.util.List;
import java.util.Map;

public interface ContactsService {

    List<Contacts> getContactListByName(String fullname);

    boolean save(Contacts contacts, String customerName);

    PaginationVO<Contacts> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndContacts(String id);

    boolean updateContacts(Contacts contacts, String customerName);

    Contacts detail(String id);

    boolean saveReamrk(ContactsRemark contactsRemark);

    List<ContactsRemark> getRemarkListByContactsId(String contactsId);

    boolean deleteRemark(String id);

    boolean updateReamrk(ContactsRemark cr);

}
