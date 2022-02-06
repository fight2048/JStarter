package com.fight2048.logging.parser;

import com.fight2048.logging.LoggerTag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

public class Swagger3LoggerParser implements LoggerParser {
    @Override
    public boolean support(Class<?> clazz, Method method) {
        Tag api = AnnotationUtils.findAnnotation(clazz, Tag.class);
        Operation operation = AnnotationUtils.findAnnotation(method, Operation.class);
        return Objects.nonNull(api) || Objects.nonNull(operation);
    }

    @Override
    public LoggerTag parse(MethodInterceptorHolder holder) {
        Tag api = holder.findAnnotation(Tag.class);
        Operation operation = AnnotatedElementUtils.findMergedAnnotation(holder.getMethod(), Operation.class);
        String action = "";
        if (Objects.nonNull(api)) {
            action = action.concat(api.name());
        }
        if (Objects.nonNull(operation)) {
            action = StringUtils.isEmpty(action)
                    ? operation.summary()
                    : action + "-" + operation.summary();
        }
        return new LoggerTag(action);
    }
}
