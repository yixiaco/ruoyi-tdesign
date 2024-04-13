package org.dromara.amqp.handler;

import org.dromara.amqp.core.AmqpTransactionalTemplate;
import org.dromara.common.core.transactional.TransactionalEventPublisher;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessagePostProcessor;

import java.time.Duration;

/**
 * @author hexm
 * @date 2023/5/9 10:50
 * @see AmqpTransactionalTemplate
 * @deprecated Amqp处理(事务)发布消息
 */
@Deprecated
public class AmqpEventPublisher {

    /**
     * 通用的消息队列后缀
     */
    public static final String QUEUE = "_QUEUE";
    protected final TransactionalEventPublisher transactionalEventPublisher;
    protected final AmqpTemplate amqpTemplate;

    public AmqpEventPublisher(TransactionalEventPublisher transactionalEventPublisher, AmqpTemplate amqpTemplate) {
        this.transactionalEventPublisher = transactionalEventPublisher;
        this.amqpTemplate = amqpTemplate;
    }

    /**
     * 提交后发送消息
     *
     * @param exchange   交换机
     * @param routingKey 路由键
     * @param message    消息
     */
    public void commitSend(String exchange, String routingKey, Object message) {
        transactionalEventPublisher.commit(() -> send(exchange, routingKey, message));
    }

    /**
     * 提交后发送消息
     *
     * @param exchange 交换机
     * @param message  消息
     */
    public void commitSend(String exchange, Object message) {
        transactionalEventPublisher.commit(() -> send(exchange, message));
    }

    /**
     * 提交后发送消息
     *
     * @param exchange             交换机
     * @param routingKey           路由键
     * @param message              消息
     * @param messagePostProcessor 消息提交时的处理，例如设置延迟队列
     */
    public void commitSend(String exchange, String routingKey, Object message, MessagePostProcessor messagePostProcessor) {
        transactionalEventPublisher.commit(() -> send(exchange, routingKey, message, messagePostProcessor));
    }

    /**
     * 提交后发送消息
     *
     * @param exchange             交换机
     * @param message              消息
     * @param messagePostProcessor 消息提交时的处理，例如设置延迟队列
     */
    public void commitSend(String exchange, Object message, MessagePostProcessor messagePostProcessor) {
        transactionalEventPublisher.commit(() -> send(exchange, message, messagePostProcessor));
    }

    /**
     * 提交后发送延迟消息
     *
     * @param exchange 交换机
     * @param message  消息
     * @param delay    延迟时间
     */
    public void commitSendDelay(String exchange, Object message, Duration delay) {
        transactionalEventPublisher.commit(() -> send(exchange, message, AmqpTransactionalTemplate.getDelayMessagePostProcessor(delay)));
    }

    /**
     * 发送消息
     *
     * @param exchange   交换机
     * @param routingKey 路由键
     * @param message    消息
     */
    public void send(String exchange, String routingKey, Object message) {
        amqpTemplate.convertAndSend(exchange, routingKey, message);
    }

    /**
     * 发送消息
     *
     * @param exchange 交换机
     * @param message  消息
     */
    public void send(String exchange, Object message) {
        amqpTemplate.convertAndSend(exchange, exchange + QUEUE, message);
    }

    /**
     * 发送消息
     *
     * @param exchange             交换机
     * @param routingKey           路由键
     * @param message              消息
     * @param messagePostProcessor 消息提交时的处理，例如设置延迟队列
     */
    public void send(String exchange, String routingKey, Object message, MessagePostProcessor messagePostProcessor) {
        amqpTemplate.convertAndSend(exchange, routingKey, message, messagePostProcessor);
    }

    /**
     * 发送消息
     *
     * @param exchange             交换机
     * @param message              消息
     * @param messagePostProcessor 消息提交时的处理，例如设置延迟队列
     */
    public void send(String exchange, Object message, MessagePostProcessor messagePostProcessor) {
        amqpTemplate.convertAndSend(exchange, exchange + QUEUE, message, messagePostProcessor);
    }

    /**
     * 发送延迟消息
     *
     * @param exchange 交换机
     * @param message  消息
     * @param delay    延迟时间
     */
    public void sendDelay(String exchange, Object message, Duration delay) {
        amqpTemplate.convertAndSend(exchange, exchange + QUEUE, message, AmqpTransactionalTemplate.getDelayMessagePostProcessor(delay));
    }
}
