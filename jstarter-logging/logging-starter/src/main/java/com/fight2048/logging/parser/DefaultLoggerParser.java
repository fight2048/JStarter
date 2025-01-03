package com.fight2048.logging.parser;

import com.fight2048.logging.LoggerTag;
import com.fight2048.logging.annotation.Logger;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;


public class DefaultLoggerParser implements LoggerParser {
    @Override
    public boolean support(Class<?> clazz, Method method) {
        Logger logger = AnnotationUtils.findAnnotation(method, Logger.class);
        //注解了并且未忽略
        return Objects.nonNull(logger) && !logger.ignore();
    }

    @Override
    public LoggerTag parse(MethodInterceptorHolder holder) {
        Logger classAnnotation = holder.findClassAnnotation(Logger.class);
        Logger methodAnnotation = holder.findMethodAnnotation(Logger.class);
        String action = Stream.of(classAnnotation, methodAnnotation)
                .filter(Objects::nonNull)
                .map(Logger::value)
                .reduce((c, m) -> c.concat("/").concat(m))
                .orElse("");

        //类上有类型，则去类上的，方法上有类型，则取方法上的
        Logger.Type classType = Optional.ofNullable(classAnnotation)
                .map(a -> a.type())
                .orElse(Logger.Type.NONE);

        Logger.Type type = Optional.ofNullable(methodAnnotation)
                .map(a -> a.type())
                .orElse(classType);

        return new LoggerTag(action, type);
    }
}
