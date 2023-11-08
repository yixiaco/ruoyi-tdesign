package org.dromara.amqp.config;

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

    @Bean
    public MessageConverter createMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
