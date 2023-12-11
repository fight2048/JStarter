package com.fight2048.logging.parser;

import com.fight2048.logging.LoggerTag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

public class Swagger3LoggerParser implements LoggerParser {
    @Override
    public boolean support(Class<?> clazz, Method method) {
        Tag api = AnnotationUtils.findAnnotation(clazz, Tag.class);
        Operation operation = AnnotationUtils.findAnnotation(method, Operation.class);
        return Objects.nonNull(api) || Objects.nonNull(operation);
    }

    @Override
    public LoggerTag parse(MethodInterceptorHolder holder) {
        Tag tag = holder.findAnnotation(Tag.class);
        Operation operation = AnnotatedElementUtils.findMergedAnnotation(holder.getMethod(), Operation.class);

        final String tagName = Optional.ofNullable(tag)
                .map(t -> t.name())
                .orElse("");

        String action = Optional.ofNullable(operation)
                .map(o -> o.summary())
                .map(s -> StringUtils.isEmpty(tagName)
                        ? s
                        : tagName + "-" + s
                ).orElse("--");

        return new LoggerTag(action);
    }
}
