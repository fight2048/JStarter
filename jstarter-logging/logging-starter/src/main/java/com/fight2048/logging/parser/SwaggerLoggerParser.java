package com.fight2048.logging.parser;

import com.fight2048.logging.LoggerTag;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

public class SwaggerLoggerParser implements LoggerParser {
    @Override
    public boolean support(Class<?> clazz, Method method) {
        Api api = AnnotationUtils.findAnnotation(clazz, Api.class);
        ApiOperation operation = AnnotationUtils.findAnnotation(method, ApiOperation.class);
        return Objects.nonNull(api) || Objects.nonNull(operation);
    }

    @Override
    public LoggerTag parse(MethodInterceptorHolder holder) {
        Api api = holder.findAnnotation(Api.class);
        ApiOperation operation = holder.findAnnotation(ApiOperation.class);
        String action = "";
        if (Objects.nonNull(api)) {
            action = action.concat(api.value());
        }
        if (Objects.nonNull(operation)) {
            action = StringUtils.isEmpty(action)
                    ? operation.value()
                    : action + "-" + operation.value();
        }
        return new LoggerTag(action);
    }
}
