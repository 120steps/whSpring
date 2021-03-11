package com.wh.springframework.aop.aspect;

import com.wh.springframework.aop.intercept.MethodInterceptor;
import com.wh.springframework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 异常通知
 */
public class AfterThrowingAdviceInterceptor extends AbstractAspectAdvice implements MethodInterceptor {

    private String throwingName;

    public void setThrowName(String throwName) {
        this.throwingName = throwName;
    }

    public AfterThrowingAdviceInterceptor(Method method, Object aspectTarget) {
        super(method, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        try{
            //直接调用下一个拦截器，如果不出现异常就不调用异常通知
            return mi.proceed();
        }catch (Throwable e){
            invokeAdviceMethod(mi,null, e.getCause());
            throw e;
        }
    }
}
