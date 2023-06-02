package org.dromara.amqp.handler;

import org.dromara.common.core.transactional.PublisherEvent;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

/**
 * 处理发布消息
 *
 * @author hexm
 * @date 2023/5/9 10:50
 */
public class PublisherHandler {

    /**
     * 通用的消息队列后缀
     */
    public static final String QUEUE = "_QUEUE";
    @Autowired
    private PublisherEvent publisherEvent;
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 提交后发送消息
     *
     * @param exchange   交换机
     * @param routingKey 路由键
     * @param message    消息
     */
    public void commitSend(String exchange, String routingKey, Object message) {
        publisherEvent.commit(() -> send(exchange, routingKey, message));
    }

    /**
     * 提交后发送消息
     *
     * @param exchange 交换机
     * @param message  消息
     */
    public void commitSend(String exchange, Object message) {
        publisherEvent.commit(() -> send(exchange, message));
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
        publisherEvent.commit(() -> send(exchange, routingKey, message, messagePostProcessor));
    }

    /**
     * 提交后发送消息
     *
     * @param exchange             交换机
     * @param message              消息
     * @param messagePostProcessor 消息提交时的处理，例如设置延迟队列
     */
    public void commitSend(String exchange, Object message, MessagePostProcessor messagePostProcessor) {
        publisherEvent.commit(() -> send(exchange, message, messagePostProcessor));
    }

    /**
     * 提交后发送延迟消息
     *
     * @param exchange 交换机
     * @param message  消息
     * @param time     延迟时间
     */
    public void commitSendDelay(String exchange, Object message, Duration time) {
        publisherEvent.commit(() -> send(exchange, message, message1 -> {
            message1.getMessageProperties().setDelay((int) time.toMillis());
            return message1;
        }));
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
     * @param time     延迟时间
     */
    public void sendDelay(String exchange, Object message, Duration time) {
        amqpTemplate.convertAndSend(exchange, exchange + QUEUE, message, message1 -> {
            message1.getMessageProperties().setDelay((int) time.toMillis());
            return message1;
        });
    }
}
