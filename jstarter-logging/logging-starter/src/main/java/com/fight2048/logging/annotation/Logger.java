package com.fight2048.logging.annotation;

import com.fight2048.logging.LoggerMetadate;

import java.lang.annotation.*;

/**
 * 访问日志,在类或者方法上注解此类,将对方法进行访问日志记录
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Logger {

    /**
     * @return 对类或方法的简单说明
     * @see LoggerMetadate#getAction()
     */
    String value() default "";

    /**
     * @return 是否取消日志记录, 如果不想记录某些方法或者类, 设置为true即可
     */
    boolean ignore() default false;

    /**
     * 功能类型
     */
    Type type() default Type.NONE;

    /**
     * 功能类型的枚举类
     */
    enum Type {
        /**
         * 其它
         */
        NONE,

        /**
         * 新增
         */
        INSERT,

        /**
         * 删除
         */
        DELETE,

        /**
         * 修改
         */
        UPDATE,

        /**
         * 查询
         */
        QUERY,

        /**
         * 授权
         */
        GRANT,

        /**
         * 导入
         */
        IMPORT,

        /**
         * 导出
         */
        EXPORT,

        /**
         * 强制
         */
        FORCE,

        /**
         * 清空
         */
        CLEAN,
    }
}
