package com.orainge.tools.spring_boot.utils;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Spring Bean 工具类
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(BeanUtils.class)
public class BeanUtils {
    public static ApplicationContext applicationContext;

    @Resource
    public ApplicationContext applicationContextBean;

    @PostConstruct
    public void init() {
        applicationContext = this.applicationContextBean;
    }

    public static <T> T getBeanByClass(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }
}