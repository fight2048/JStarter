package com.fight2048.logging.parser;

import com.fight2048.logging.Logger;
import com.fight2048.logging.LoggerTag;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Objects;
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
        Logger methodAnnotation = holder.findMethodAnnotation(Logger.class);
        Logger classAnnotation = holder.findClassAnnotation(Logger.class);
        String action = Stream.of(classAnnotation, methodAnnotation)
                .filter(Objects::nonNull)
                .map(Logger::value)
                .reduce((c, m) -> c.concat("-").concat(m))
                .orElse("");
        return new LoggerTag(action);
    }
}
