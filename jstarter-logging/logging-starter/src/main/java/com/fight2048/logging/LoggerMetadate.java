package com.fight2048.logging;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;

/**
 * 访问日志信息,此对象包含了被拦截的方法和类信息,如果要对此对象进行序列化,请自行转换为想要的格式.
 * 或者调用{@link this#toSimpleMap}获取可序列化的map格式日志信息
 */
public class LoggerMetadate {

    /**
     * 访问的操作
     *
     * @see Logger#value()
     */
    private String action;

    /**
     * 访问的类型
     *
     * @see Logger#type()
     */
    private Logger.Type type;

    /**
     * 访问对应的java方法
     */
    private Method method;

    /**
     * 访问对应的java类
     */
    private Class<?> target;

    /**
     * 请求的参数,参数为java方法的参数而不是http参数,key为参数名,value为参数值.
     */
    private Map<String, Object> parameters;

    /**
     * 请求者ip地址
     */
    private String ip;

    /**
     * 请求的url地址
     */
    private String url;

    /**
     * http 请求头集合
     */
    private Map<String, String> httpHeaders;

    /**
     * 上下文
     */
    private Map<String, String> context;

    /**
     * http 请求方法, GET,POST...
     */
    private String httpMethod;

    /**
     * 响应结果,方法的返回值
     */
    private Object response;

    /**
     * 异常信息,请求对应方法抛出的异常
     */
    private Throwable exception;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?> getTarget() {
        return target;
    }

    public void setTarget(Class<?> target) {
        this.target = target;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public Map<String, Object> toSimpleMap(Function<Object, Serializable> objectFilter, Map<String, Object> map) {
        map.put("action", action);
        if (method != null) {
            StringJoiner methodAppender = new StringJoiner(",", method.getName().concat("("), ")");
            String[] parameterNames = parameters.keySet().toArray(new String[0]);
            Class<?>[] parameterTypes = method.getParameterTypes();

            for (int i = 0; i < parameterTypes.length; i++) {
                methodAppender.add(parameterTypes[i].getSimpleName().concat(" ").concat(parameterNames.length > i ? parameterNames[i] : ("arg" + i)));
            }
            map.put("method", methodAppender.toString());
        }
        map.put("target", target != null ? target.getName() : "");
        Map<String, Object> newParameter = new LinkedHashMap<>(parameters);
        newParameter.entrySet().forEach(entry -> {
            if (entry.getValue() != null) {
                entry.setValue(objectFilter.apply(entry.getValue()));
            }
        });

        map.put("parameters", newParameter);
        map.put("httpHeaders", httpHeaders);
        map.put("httpMethod", httpMethod);
        map.put("ip", ip);
        map.put("url", url);
        map.put("response", objectFilter.apply(response));
        if (exception != null) {
            StringWriter writer = new StringWriter();
            exception.printStackTrace(new PrintWriter(writer));
            map.put("exception", writer.toString());
        }
        return map;
    }
}
