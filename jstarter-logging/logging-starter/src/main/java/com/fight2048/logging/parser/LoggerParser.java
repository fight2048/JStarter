package com.fight2048.logging.parser;


import com.fight2048.logging.LoggerTag;

import java.lang.reflect.Method;

public interface LoggerParser {
    boolean support(Class<?> clazz, Method method);

    LoggerTag parse(MethodInterceptorHolder holder);
}
