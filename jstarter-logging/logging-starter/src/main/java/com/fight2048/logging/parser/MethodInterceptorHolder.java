package com.fight2048.logging.parser;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class MethodInterceptorHolder {
    public static final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final Method method;
    private final Object target;
    private final Object[] arguments;
    private final String[] argumentsNames;
    private Map<String, Object> namedArguments;

    public MethodInterceptorHolder(Method method, Object target, Object[] arguments, String[] argumentsNames, Map<String, Object> namedArguments) {
        this.method = method;
        this.target = target;
        this.arguments = arguments;
        this.argumentsNames = argumentsNames;
        this.namedArguments = namedArguments;
    }

    public Method getMethod() {
        return method;
    }

    public Object getTarget() {
        return target;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public String[] getArgumentsNames() {
        return argumentsNames;
    }

    public void setNamedArguments(Map<String, Object> namedArguments) {
        this.namedArguments = namedArguments;
    }

    protected Map<String, Object> createNamedArguments() {
        Map<String, Object> namedArguments = new LinkedHashMap<>(arguments.length);
        for (int i = 0, len = arguments.length; i < len; i++) {
            namedArguments.put(argumentsNames[i], arguments[i]);
        }
        return namedArguments;
    }

    public Map<String, Object> getNamedArguments() {
        return namedArguments == null ? namedArguments = createNamedArguments() : namedArguments;
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
    public static MethodInterceptorHolder create(MethodInvocation invocation) {
        String[] argNames = nameDiscoverer.getParameterNames(invocation.getMethod());
        Object[] args = invocation.getArguments();

        String[] names;
        //参数名与参数长度不一致，则填充argx来作为参数名
        if (argNames == null || argNames.length != args.length) {
            names = new String[args.length];
            for (int i = 0, len = args.length; i < len; i++) {
                names[i] = (argNames == null || argNames.length <= i || argNames[i] == null) ? "arg" + i : argNames[i];
            }
        } else {
            names = argNames;
        }
        return new MethodInterceptorHolder(
                invocation.getMethod(),
                invocation.getThis(),
                args,
                names,
                null
        );
    }

    public static <T extends Annotation> T findMethodAnnotation(Class targetClass, Method method, @Nullable Class<T> annotationType) {
        Method m = method;
        T a = org.springframework.core.annotation.AnnotationUtils.findAnnotation(m, annotationType);
        if (a != null) {
            return a;
        }
        m = ClassUtils.getMostSpecificMethod(m, targetClass);
        a = org.springframework.core.annotation.AnnotationUtils.findAnnotation(m, annotationType);
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
