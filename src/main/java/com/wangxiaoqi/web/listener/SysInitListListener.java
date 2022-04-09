
package com.wangxiaoqi.web.listener;

import com.wangxiaoqi.settings.domain.DicValue;
import com.wangxiaoqi.settings.service.DicService;
import com.wangxiaoqi.settings.service.impl.DicServiceImpl;
import com.wangxiaoqi.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListListener implements ServletContextListener {
    /**
     * 全局作用域监听器，在全局作用域创建的时候，将数据字典放在数据库缓存中
     * @param event 全局作用域对象
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {

        System.out.println("服务器缓存处理数据字典开始");

        //获取到全局作用域
        ServletContext application = event.getServletContext();

        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map = ds.getAll();

        //拿到map里所有的key
        Set<String> keys = map.keySet();
        for (String key : keys){
            //把map中的键值对转化成全局作用域中的键值对
            application.setAttribute(key,map.get(key));
        }

        System.out.println("服务器缓存处理数据字典结束");

    }
}

