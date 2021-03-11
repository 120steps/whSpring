package com.wh.springframework.aop.aspect;

import com.wh.springframework.aop.intercept.MethodInterceptor;
import com.wh.springframework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 后置通知
 */
public class AfterReturningAdviceInterceptor extends AbstractAspectAdvice implements MethodInterceptor {

    private JoinPoint joinPoint;

    public AfterReturningAdviceInterceptor(Method method, Object aspectTarget) {
        super(method, aspectTarget);
    }

    private void afterReturning(Object retVal , Method method , Object[] arguments ,Object aThis) throws  Throwable{
        super.invokeAdviceMethod(this.joinPoint,retVal,null);
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        //先调用下一个拦截器
        Object retVal = mi.proceed();
        //再调用后置通知
        this.joinPoint = mi;
        afterReturning(retVal,mi.getMethod(),mi.getArguments(),mi.getThis());
        return retVal;
    }
}
