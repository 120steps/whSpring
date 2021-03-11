package com.wh.springframework.aop;

import com.wh.springframework.aop.support.AdvisedSupport;

public class CglibAopProxy implements AopProxy {

    public CglibAopProxy(AdvisedSupport config) {
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
