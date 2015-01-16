package com.silu.dinner.database;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


public class DataSourceInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        DataSource dataSource = invocation.getMethod().getDeclaringClass().getAnnotation(DataSource.class);
        if (dataSource != null) {
            DataBaseContextHolder.setDataSource(dataSource.value());
        }
        return invocation.proceed();
    }
}
