package com.fight2048.logging.aop;

import com.fight2048.logging.LoggerMetadate;
import com.fight2048.logging.LoggerTag;
import com.fight2048.logging.event.LoggerAfterEvent;
import com.fight2048.logging.event.LoggerBeforeEvent;
import com.fight2048.logging.parser.LoggerParser;
import com.fight2048.logging.parser.MethodInterceptorHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 使用AOP记录访问日志,并触发LoggerListener事件
 */
public class AopLoggerAdvisor extends StaticMethodMatcherPointcutAdvisor {
    @Autowired(required = false)
    private final List<LoggerParser> loggerParsers = new ArrayList<>();
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public AopLoggerAdvisor() {
        MethodInterceptor methodInterceptor = (invocation) -> {
            MethodInterceptorHolder methodInterceptorHolder = MethodInterceptorHolder.of(invocation);
            LoggerMetadate metadate = createLogger(methodInterceptorHolder);
            Object response;
            try {
                eventPublisher.publishEvent(new LoggerBeforeEvent(metadate));
                response = invocation.proceed();
                metadate.setResponse(response);
            } catch (Throwable e) {
                metadate.setException(e);
                throw e;
            } finally {
                //触发监听
                eventPublisher.publishEvent(new LoggerAfterEvent(metadate));
            }
            return response;
        };

        setAdvice(methodInterceptor);
    }

    protected LoggerMetadate createLogger(MethodInterceptorHolder holder) {
        LoggerMetadate metadate = new LoggerMetadate();

        LoggerTag tag = loggerParsers.stream()
                .filter(parser -> parser.support(ClassUtils.getUserClass(holder.getTarget()), holder.getMethod()))
                .findAny()
                .map(parser -> parser.parse(holder))
                .orElse(null);

        if (Objects.nonNull(tag)) {
            metadate.setAction(tag.getAction());
        }
        metadate.setParameters(holder.getParameters());
        metadate.setTarget(holder.getTarget().getClass());
        metadate.setMethod(holder.getMethod());

        HttpServletRequest request = getHttpServletRequest();
        if (null != request) {
            metadate.setHttpHeaders(getHeaders(request));
            metadate.setIp(getIpAddr(request));
            metadate.setHttpMethod(request.getMethod());
            metadate.setUrl(request.getRequestURL().toString());
        }
        return metadate;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return loggerParsers.stream()
                .anyMatch(parser -> parser.support(targetClass, method));
    }

    /**
     * 尝试获取当前请求的HttpServletRequest实例
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    public static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    public static Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap<>();
        Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String name = String.valueOf(enumeration.nextElement());
            parameters.put(name, request.getParameter(name));
        }
        return parameters;
    }

    static final String[] ipHeaders = {
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP"
    };

    /**
     * 获取请求客户端的真实ip地址
     *
     * @param request 请求对象
     * @return ip地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        for (String ipHeader : ipHeaders) {
            String ip = request.getHeader(ipHeader);
            if (!StringUtils.isEmpty(ip) && !ip.contains("unknown")) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
}
