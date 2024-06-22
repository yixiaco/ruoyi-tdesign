package org.dromara.amqp.config;

import org.dromara.amqp.aspect.AmqpAspect;
import org.dromara.amqp.core.AmqpTransactionalTemplate;
import org.dromara.amqp.handler.AmqpEventPublisher;
import org.dromara.common.core.transactional.TransactionalEventPublisher;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * AMQP配置
 *
 * @author hexm
 * @date 2023/11/08 14:57
 */
@AutoConfiguration
public class AmqpConfiguration {

    /**
     * 提供mq消息转换器
     */
    @Bean
    public MessageConverter createMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * @param transactionalEventPublisher 事务发布器
     * @param amqpTemplate                Amqp
     * @deprecated 事务消息发布
     */
    @Bean
    @Deprecated
    public AmqpEventPublisher amqpEventPublisher(TransactionalEventPublisher transactionalEventPublisher, AmqpTemplate amqpTemplate) {
        return new AmqpEventPublisher(transactionalEventPublisher, amqpTemplate);
    }

    /**
     * 支持事务Amqp消息
     *
     * @param transactionalEventPublisher 事务发布器
     * @param amqpTemplate                Amqp
     */
    @Bean
    public AmqpTransactionalTemplate amqpTransactionalTemplate(TransactionalEventPublisher transactionalEventPublisher, AmqpTemplate amqpTemplate) {
        return new AmqpTransactionalTemplate(transactionalEventPublisher, amqpTemplate);
    }

    @Bean
    public AmqpAspect amqpAspect() {
        return new AmqpAspect();
    }
}
