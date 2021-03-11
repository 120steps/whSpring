package com.wh.springframework.aop.aspect;

import com.wh.springframework.aop.intercept.MethodInterceptor;
import com.wh.springframework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 前置通知
 */
public class MethodBeforeAdviceInterceptor extends AbstractAspectAdvice implements MethodInterceptor {

    private JoinPoint joinPoint;

    public MethodBeforeAdviceInterceptor(Method method, Object aspectTarget) {
        super(method, aspectTarget);
    }

    private void before(Method method , Object[] args , Object target) throws  Throwable{
        super.invokeAdviceMethod(this.joinPoint,null,null);
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        this.joinPoint = mi;
        //在调用下一个拦截器前先执行前置通知
        before(mi.getMethod(),mi.getArguments(),mi.getThis());
        return mi.proceed();
    }
}
