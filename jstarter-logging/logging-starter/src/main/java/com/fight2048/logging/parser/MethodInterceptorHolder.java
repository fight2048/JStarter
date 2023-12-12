package com.fight2048.logging.parser;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class MethodInterceptorHolder {
    public static final ParameterNameDiscoverer nameDiscoverer = new StandardReflectionParameterNameDiscoverer();
    private final Method method;
    private final Object target;
    private final Object[] parameterValues;
    private final String[] parameterNames;
    private Map<String, Object> parameters;

    public MethodInterceptorHolder(Method method, Object target,
                                   Object[] parameterValues, String[] parameterNames) {
        this.method = method;
        this.target = target;
        this.parameterValues = parameterValues;
        this.parameterNames = parameterNames;
    }

    public Method getMethod() {
        return method;
    }

    public Object getTarget() {
        return target;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    public String[] getParameterNames() {
        return parameterNames;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    protected Map<String, Object> createParameters() {
        Map<String, Object> namedArguments = new HashMap<>(parameterValues.length);
        for (int i = 0, len = parameterValues.length; i < len; i++) {
            namedArguments.put(parameterNames[i], parameterValues[i]);
        }
        return namedArguments;
    }

    public Map<String, Object> getParameters() {
        return parameters == null ? parameters = createParameters() : parameters;
    }

    public <T extends Annotation> T findMethodAnnotation(@Nullable Class<T> annotationType) {
        return findMethodAnnotation(annotationType, method, annotationType);
    }

    public <T extends Annotation> T findClassAnnotation(@Nullable Class<T> annotationType) {
        return AnnotationUtils.findAnnotation(target.getClass(), annotationType);
    }

    public <T extends Annotation> T findAnnotation(@Nullable Class<T> annotationType) {
        return findAnnotation(target.getClass(), method, annotationType);
    }

    /**
     * 参数名称获取器,用于获取方法参数的名称
     */
    public static MethodInterceptorHolder of(MethodInvocation invocation) {
        String[] parameterNames = nameDiscoverer.getParameterNames(invocation.getMethod());
        Object[] parameterValues = invocation.getArguments();

        String[] names;
        //参数名与参数长度不一致，则填充argument+数字序号来作为参数名
        if (parameterNames == null
                || parameterNames.length != parameterValues.length) {
            names = new String[parameterValues.length];
            for (int i = 0, len = parameterValues.length; i < len; i++) {
                names[i] = (parameterNames == null || parameterNames.length <= i || parameterNames[i] == null) ? "argument" + i : parameterNames[i];
            }
        } else {
            names = parameterNames;
        }
        return new MethodInterceptorHolder(
                invocation.getMethod(),
                invocation.getThis(),
                parameterValues, names
        );
    }

    public static <T extends Annotation> T findMethodAnnotation(Class targetClass, Method method, @Nullable Class<T> annotationType) {
        Method m = method;
        T a = AnnotationUtils.findAnnotation(m, annotationType);
        if (a != null) {
            return a;
        }
        m = ClassUtils.getMostSpecificMethod(m, targetClass);
        a = AnnotationUtils.findAnnotation(m, annotationType);
        if (a == null) {
            List<Class> supers = new ArrayList<>(Arrays.asList(targetClass.getInterfaces()));
            if (targetClass.getSuperclass() != Object.class) {
                supers.add(targetClass.getSuperclass());
            }

            for (Class c : supers) {
                if (c == null) {
                    continue;
                }
                AtomicReference<Method> methodRef = new AtomicReference<>();
                ReflectionUtils.doWithMethods(c, im -> {
                    if (im.getName().equals(method.getName())
                            && im.getParameterCount() == method.getParameterCount()) {
                        methodRef.set(im);
                    }
                });

                if (methodRef.get() != null) {
                    a = findMethodAnnotation(c, methodRef.get(), annotationType);
                    if (a != null) {
                        return a;
                    }
                }
            }
        }
        return a;
    }

    public static <T extends Annotation> T findAnnotation(Class targetClass, Method method, @Nullable Class<T> annotationType) {
        T a = findMethodAnnotation(targetClass, method, annotationType);
        if (a != null) {
            return a;
        }
        return AnnotationUtils.findAnnotation(targetClass, annotationType);
    }
}
