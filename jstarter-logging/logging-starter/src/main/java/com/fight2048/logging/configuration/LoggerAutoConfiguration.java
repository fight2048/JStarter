package com.fight2048.logging.configuration;

import com.fight2048.logging.annotation.Logger;
import com.fight2048.logging.aop.AopLoggerAdvisor;
import com.fight2048.logging.listener.LoggerListener;
import com.fight2048.logging.parser.DefaultLoggerParser;
import com.fight2048.logging.parser.Swagger3LoggerParser;
import com.fight2048.logging.parser.SwaggerLoggerParser;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * AOP 访问日志记录自动配置
 */
@ConditionalOnClass(LoggerListener.class)
@Configuration
public class LoggerAutoConfiguration {

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public AopLoggerAdvisor aopAccessLoggerSupport() {
        return new AopLoggerAdvisor();
    }

    @Bean
    @Order(1)
    @ConditionalOnClass(Logger.class)
    public DefaultLoggerParser defaultAccessLoggerParser() {
        return new DefaultLoggerParser();
    }

    @Bean
    @Order(2)
    @ConditionalOnClass(Tag.class)
    public Swagger3LoggerParser swagger3LoggerParser() {
        return new Swagger3LoggerParser();
    }

    @Bean
    @Order(10)
    @ConditionalOnClass(Api.class)
    public SwaggerLoggerParser swaggerLoggerParser() {
        return new SwaggerLoggerParser();
    }
}
