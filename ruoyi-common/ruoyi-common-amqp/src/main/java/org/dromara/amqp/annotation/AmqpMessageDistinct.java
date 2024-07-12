package org.dromara.amqp.annotation;

import org.intellij.lang.annotations.Language;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 消息去重注解。建议只在需要去重的地方使用
 * 重要消息，例如订单，可以将过期时间设置长一点
 *
 * @author hexm
 * @date 2024/6/22
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AmqpMessageDistinct {

    /**
     * 去重依赖的key。注意，这是一个SpringEL表达式
     */
    @Language("SpEL")
    String value();

    /**
     * 去重key的前缀，可以作为业务标识。例如：order
     */
    String prefix() default "";

    /**
     * 是否忽略异常
     * 执行发生异常时，是否忽略异常，记录消息id。否则只有成功的消息id会被记录
     */
    boolean ignoreThrowable() default false;

    /**
     * 过期秒数，默认30分钟。-1表示不过期
     */
    long expire() default 30 * 60;
}
