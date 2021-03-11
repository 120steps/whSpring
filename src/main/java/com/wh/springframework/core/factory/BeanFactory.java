package com.wh.springframework.core.factory;

public interface BeanFactory {

    Object getBean(String name) throws  Exception;

    <T> T getBean(Class<T>  requiredType) throws  Exception;
}
