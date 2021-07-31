package com.adobe.bookstore.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Component
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... args) {
        System.out.println("Method name " + method.getName()
                + "---" + Arrays.toString(args) + "---"
                + "error message: " + throwable.getMessage()
        );
    }
}
