package com.st.conf;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 *
 * applicationContext通用类
 * Created by zhaoyx on 2016/11/18.
 */
@Configuration
public class ApplicationContextConfiguration implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;
    @Override
    public void setApplicationContext(ApplicationContext arg) throws BeansException {
        if (ApplicationContextConfiguration.applicationContext == null)
            ApplicationContextConfiguration.applicationContext = arg;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    /**
     * 通过name获取bean
     * @param name
     * @return
     */
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取bean
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过calss,name获取指定的bean
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name,clazz);
    }
}
