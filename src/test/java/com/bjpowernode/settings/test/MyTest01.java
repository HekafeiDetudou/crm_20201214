package com.bjpowernode.settings.test;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.workbench.entity.ClueRemark;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class MyTest01 {

    @Test
    public void test01(){

        //验证失效时间
        String expireTime = "2018-12-06 15:49:19";//失效时间
        String currentTime = DateTimeUtil.getSysTime();
        int count = expireTime.compareTo(currentTime);
        System.out.println(count);

        String pwd = "yhujhftrtg7645@";
        pwd = MD5Util.getMD5(pwd);
        System.out.println(pwd);

    }

    @Test
    public void test02(){

        int[] a = {1,2,3,4,5};
        for (int b : a){
            System.out.println(b);
        }
    }




}
