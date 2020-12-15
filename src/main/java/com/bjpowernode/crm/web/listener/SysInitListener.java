package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.entity.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        System.out.println("服务器缓存处理数据字典开始");

        //拿到上下文作用域对象
        ServletContext application = sce.getServletContext();

        //取数据字典
        DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());
        /*
            应该向业务层要7个list

            7个list可以打包成map
            业务层应该是这样来保存的
                map.put("appellationList",dvlist1);
                map.put("clueStateList",dvlist2);
                map.put("stageList",dvlist3);
                ....

         */
        Map<String, List<DicValue>> map = dicService.getAll();
        //将map解析为上下文作用域对象中保存的键值对
        Set<String> set = map.keySet();
        for (String key:set){

            application.setAttribute(key,map.get(key));

        }

        System.out.println("服务器缓存处理数据字典结束");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        System.out.println("上下文作用域对象销毁");


    }
}
