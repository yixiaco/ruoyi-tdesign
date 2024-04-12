package org.dromara.amqp.proxy;

import org.dromara.common.core.transactional.TransactionalApply;
import org.dromara.common.core.transactional.TransactionalEventPublisher;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.ReceiveAndReplyCallback;
import org.springframework.amqp.core.ReplyToAddressCallback;
import org.springframework.core.ParameterizedTypeReference;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 支持事务的AmqpTemplate。
 * <p>
 * {@link AmqpTransactionalProxy#commit()}: 对前面的最终操作，并在事务提交后，执行方法
 * {@link AmqpTransactionalProxy#rollback()}: 对前面的最终操作，并在事务回滚后，执行方法
 * {@link AmqpTransactionalProxy#completion()}: 对前面的最终操作，并在事务提交或回滚后，执行方法
 * <p>
 * example:
 * <pre>{@code
 * // 提交事务后，执行convertAndSend方法
 * amqpTransactionalTemplate.convertAndSend('exchange', 'routerKey', new BaseMessage()).commit();
 * }</pre>
 *
 * @author hexm
 * @date 2024/4/12
 */
public class AmqpTransactionalTemplate {

    protected final TransactionalEventPublisher transactionalEventPublisher;
    protected final AmqpTemplate amqpTemplate;

    public AmqpTransactionalTemplate(TransactionalEventPublisher transactionalEventPublisher, AmqpTemplate amqpTemplate) {
        this.transactionalEventPublisher = transactionalEventPublisher;
        this.amqpTemplate = amqpTemplate;
    }

    /**
     * Send a message to a default exchange with a default routing key.
     *
     * @param message a message to send
     * @throws AmqpException if there is a problem
     */
    public AmqpTransactionalProxy send(Message message) {
        return new AmqpTransactionalProxy(() -> amqpTemplate.send(message), transactionalEventPublisher);
    }

    /**
     * Send a message to a default exchange with a specific routing key.
     *
     * @param routingKey the routing key
     * @param message    a message to send
     * @throws AmqpException if there is a problem
     */
    public AmqpTransactionalProxy send(String routingKey, Message message) {
        return new AmqpTransactionalProxy(() -> amqpTemplate.send(routingKey, message), transactionalEventPublisher);
    }

    /**
     * Send a message to a specific exchange with a specific routing key.
     *
     * @param exchange   the name of the exchange
     * @param routingKey the routing key
     * @param message    a message to send
     * @throws AmqpException if there is a problem
     */
    public AmqpTransactionalProxy send(String exchange, String routingKey, Message message) {
        return new AmqpTransactionalProxy(() -> amqpTemplate.send(exchange, routingKey, message), transactionalEventPublisher);
    }

    // send methods with conversion

    /**
     * Convert a Java object to an Amqp {@link Message} and send it to a default exchange
     * with a default routing key.
     *
     * @param message a message to send
     * @throws AmqpException if there is a problem
     */
    public AmqpTransactionalProxy convertAndSend(Object message) {
        return new AmqpTransactionalProxy(() -> amqpTemplate.convertAndSend(message), transactionalEventPublisher);
    }

    /**
     * Convert a Java object to an Amqp {@link Message} and send it to a default exchange
     * with a specific routing key.
     *
     * @param routingKey the routing key
     * @param message    a message to send
     * @throws AmqpException if there is a problem
     */
    public AmqpTransactionalProxy convertAndSend(String routingKey, Object message) {
        return new AmqpTransactionalProxy(() -> amqpTemplate.convertAndSend(routingKey, message), transactionalEventPublisher);
    }

    /**
     * Convert a Java object to an Amqp {@link Message} and send it to a specific exchange
     * with a specific routing key.
     *
     * @param exchange   the name of the exchange
     * @param routingKey the routing key
     * @param message    a message to send
     * @throws AmqpException if there is a problem
     */
    public AmqpTransactionalProxy convertAndSend(String exchange, String routingKey, Object message) {
        return new AmqpTransactionalProxy(() -> amqpTemplate.convertAndSend(exchange, routingKey, message), transactionalEventPublisher);
    }

    /**
     * Convert a Java object to an Amqp {@link Message} and send it to a default exchange
     * with a default routing key.
     *
     * @param message              a message to send
     * @param messagePostProcessor a processor to apply to the message before it is sent
     * @throws AmqpException if there is a problem
     */
    public AmqpTransactionalProxy convertAndSend(Object message, MessagePostProcessor messagePostProcessor) {
        return new AmqpTransactionalProxy(() -> amqpTemplate.convertAndSend(message, messagePostProcessor), transactionalEventPublisher);
    }

    /**
     * Convert a Java object to an Amqp {@link Message} and send it to a default exchange
     * with a specific routing key.
     *
     * @param routingKey           the routing key
     * @param message              a message to send
     * @param messagePostProcessor a processor to apply to the message before it is sent
     * @throws AmqpException if there is a problem
     */
    public AmqpTransactionalProxy convertAndSend(String routingKey, Object message, MessagePostProcessor messagePostProcessor) {
        return new AmqpTransactionalProxy(() -> amqpTemplate.convertAndSend(routingKey, message, messagePostProcessor), transactionalEventPublisher);
    }

    /**
     * Convert a Java object to an Amqp {@link Message} and send it to a specific exchange
     * with a specific routing key.
     *
     * @param exchange             the name of the exchange
     * @param routingKey           the routing key
     * @param message              a message to send
     * @param messagePostProcessor a processor to apply to the message before it is sent
     * @throws AmqpException if there is a problem
     */
    public AmqpTransactionalProxy convertAndSend(String exchange, String routingKey, Object message, MessagePostProcessor messagePostProcessor) {
        return new AmqpTransactionalProxy(() -> amqpTemplate.convertAndSend(exchange, routingKey, message, messagePostProcessor), transactionalEventPublisher);
    }

    // receive methods for messages

    /**
     * Receive a message if there is one from a default queue. Returns immediately,
     * possibly with a null value.
     *
     * @return a message or null if there is none waiting
     * @throws AmqpException if there is a problem
     */
    public AmqpResultConsumer<Message> receive() {
        return new AmqpResultConsumer<>(amqpTemplate::receive, transactionalEventPublisher);
    }

    /**
     * Receive a message if there is one from a specific queue. Returns immediately,
     * possibly with a null value.
     *
     * @param queueName the name of the queue to poll
     * @return a message or null if there is none waiting
     * @throws AmqpException if there is a problem
     */
    public AmqpResultConsumer<Message> receive(String queueName) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.receive(queueName), transactionalEventPublisher);
    }

    /**
     * Receive a message from a default queue, waiting up to the specified wait time if
     * necessary for a message to become available.
     *
     * @param timeoutMillis how long to wait before giving up. Zero value means the method
     *                      will return {@code null} immediately if there is no message available. Negative
     *                      value makes method wait for a message indefinitely.
     * @return a message or null if the time expires
     * @throws AmqpException if there is a problem
     * @since 1.6
     */
    public AmqpResultConsumer<Message> receive(long timeoutMillis) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.receive(timeoutMillis), transactionalEventPublisher);
    }

    /**
     * Receive a message from a specific queue, waiting up to the specified wait time if
     * necessary for a message to become available.
     *
     * @param queueName     the queue to receive from
     * @param timeoutMillis how long to wait before giving up. Zero value means the method
     *                      will return {@code null} immediately if there is no message available. Negative
     *                      value makes method wait for a message indefinitely.
     * @return a message or null if the time expires
     * @throws AmqpException if there is a problem
     * @since 1.6
     */
    public AmqpResultConsumer<Message> receive(String queueName, long timeoutMillis) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.receive(queueName, timeoutMillis), transactionalEventPublisher);
    }

    // receive methods with conversion

    /**
     * Receive a message if there is one from a default queue and convert it to a Java
     * object. Returns immediately, possibly with a null value.
     *
     * @return a message or null if there is none waiting
     * @throws AmqpException if there is a problem
     */
    public AmqpResultConsumer<Object> receiveAndConvert() {
        return new AmqpResultConsumer<>(amqpTemplate::receiveAndConvert, transactionalEventPublisher);
    }

    /**
     * Receive a message if there is one from a specific queue and convert it to a Java
     * object. Returns immediately, possibly with a null value.
     *
     * @param queueName the name of the queue to poll
     * @return a message or null if there is none waiting
     * @throws AmqpException if there is a problem
     */
    public AmqpResultConsumer<Object> receiveAndConvert(String queueName) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.receiveAndConvert(queueName), transactionalEventPublisher);
    }

    /**
     * Receive a message if there is one from a default queue and convert it to a Java
     * object. Wait up to the specified wait time if necessary for a message to become
     * available.
     *
     * @param timeoutMillis how long to wait before giving up. Zero value means the method
     *                      will return {@code null} immediately if there is no message available. Negative
     *                      value makes method wait for a message indefinitely.
     * @return a message or null if the time expires
     * @throws AmqpException if there is a problem
     * @since 1.6
     */
    public AmqpResultConsumer<Object> receiveAndConvert(long timeoutMillis) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.receiveAndConvert(timeoutMillis), transactionalEventPublisher);
    }

    /**
     * Receive a message if there is one from a specific queue and convert it to a Java
     * object. Wait up to the specified wait time if necessary for a message to become
     * available.
     *
     * @param queueName     the name of the queue to poll
     * @param timeoutMillis how long to wait before giving up. Zero value means the method
     *                      will return {@code null} immediately if there is no message available. Negative
     *                      value makes method wait for a message indefinitely.
     * @return a message or null if the time expires
     * @throws AmqpException if there is a problem
     * @since 1.6
     */
    public AmqpResultConsumer<Object> receiveAndConvert(String queueName, long timeoutMillis) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.receiveAndConvert(queueName, timeoutMillis), transactionalEventPublisher);
    }

    /**
     * Receive a message if there is one from a default queue and convert it to a Java
     * object. Returns immediately, possibly with a null value. Requires a
     * {@link org.springframework.amqp.support.converter.SmartMessageConverter}.
     *
     * @param type the type to convert to.
     * @param <T>  the type.
     * @return a message or null if there is none waiting.
     * @throws AmqpException if there is a problem.
     * @since 2.0
     */
    public <T> AmqpResultConsumer<T> receiveAndConvert(ParameterizedTypeReference<T> type) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.receiveAndConvert(type), transactionalEventPublisher);
    }

    /**
     * Receive a message if there is one from a specific queue and convert it to a Java
     * object. Returns immediately, possibly with a null value. Requires a
     * {@link org.springframework.amqp.support.converter.SmartMessageConverter}.
     *
     * @param queueName the name of the queue to poll
     * @param type      the type to convert to.
     * @param <T>       the type.
     * @return a message or null if there is none waiting
     * @throws AmqpException if there is a problem
     * @since 2.0
     */
    public <T> AmqpResultConsumer<T> receiveAndConvert(String queueName, ParameterizedTypeReference<T> type) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.receiveAndConvert(queueName, type), transactionalEventPublisher);
    }

    /**
     * Receive a message if there is one from a default queue and convert it to a Java
     * object. Wait up to the specified wait time if necessary for a message to become
     * available. Requires a
     * {@link org.springframework.amqp.support.converter.SmartMessageConverter}.
     *
     * @param timeoutMillis how long to wait before giving up. Zero value means the method
     *                      will return {@code null} immediately if there is no message available. Negative
     *                      value makes method wait for a message indefinitely.
     * @param type          the type to convert to.
     * @param <T>           the type.
     * @return a message or null if the time expires
     * @throws AmqpException if there is a problem
     * @since 2.0
     */
    public <T> AmqpResultConsumer<T> receiveAndConvert(long timeoutMillis, ParameterizedTypeReference<T> type) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.receiveAndConvert(timeoutMillis, type), transactionalEventPublisher);
    }

    /**
     * Receive a message if there is one from a specific queue and convert it to a Java
     * object. Wait up to the specified wait time if necessary for a message to become
     * available. Requires a
     * {@link org.springframework.amqp.support.converter.SmartMessageConverter}.
     *
     * @param queueName     the name of the queue to poll
     * @param timeoutMillis how long to wait before giving up. Zero value means the method
     *                      will return {@code null} immediately if there is no message available. Negative
     *                      value makes method wait for a message indefinitely.
     * @param type          the type to convert to.
     * @param <T>           the type.
     * @return a message or null if the time expires
     * @throws AmqpException if there is a problem
     * @since 2.0
     */
    public <T> AmqpResultConsumer<T> receiveAndConvert(String queueName, long timeoutMillis, ParameterizedTypeReference<T> type) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.receiveAndConvert(queueName, timeoutMillis, type), transactionalEventPublisher);
    }

    // receive and send methods for provided callback

    /**
     * Receive a message if there is one from a default queue, invoke provided
     * {@link ReceiveAndReplyCallback} and send reply message, if the {@code callback}
     * returns one, to the {@code replyTo} {@link org.springframework.amqp.core.Address}
     * from {@link org.springframework.amqp.core.MessageProperties} or to default exchange
     * and default routingKey.
     *
     * @param callback a user-provided {@link ReceiveAndReplyCallback} implementation to
     *                 process received message and return a reply message.
     * @param <R>      The type of the request after conversion from the {@link Message}.
     * @param <S>      The type of the response.
     * @return {@code true}, if message was received
     * @throws AmqpException if there is a problem
     */
    <R, S> AmqpResultConsumer<Boolean> receiveAndReply(ReceiveAndReplyCallback<R, S> callback) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.receiveAndReply(callback), transactionalEventPublisher);
    }

    /**
     * Receive a message if there is one from provided queue, invoke provided
     * {@link ReceiveAndReplyCallback} and send reply message, if the {@code callback}
     * returns one, to the {@code replyTo} {@link org.springframework.amqp.core.Address}
     * from {@link org.springframework.amqp.core.MessageProperties} or to default exchange
     * and default routingKey.
     *
     * @param queueName the queue name to receive a message.
     * @param callback  a user-provided {@link ReceiveAndReplyCallback} implementation to
     *                  process received message and return a reply message.
     * @param <R>       The type of the request after conversion from the {@link Message}.
     * @param <S>       The type of the response.
     * @return {@code true}, if message was received.
     * @throws AmqpException if there is a problem.
     */
    <R, S> AmqpResultConsumer<Boolean> receiveAndReply(String queueName, ReceiveAndReplyCallback<R, S> callback) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.receiveAndReply(queueName, callback), transactionalEventPublisher);
    }

    /**
     * Receive a message if there is one from default queue, invoke provided
     * {@link ReceiveAndReplyCallback} and send reply message, if the {@code callback}
     * returns one, to the provided {@code exchange} and {@code routingKey}.
     *
     * @param callback        a user-provided {@link ReceiveAndReplyCallback} implementation to
     *                        process received message and return a reply message.
     * @param replyExchange   the exchange name to send reply message.
     * @param replyRoutingKey the routing key to send reply message.
     * @param <R>             The type of the request after conversion from the {@link Message}.
     * @param <S>             The type of the response.
     * @return {@code true}, if message was received.
     * @throws AmqpException if there is a problem.
     */
    <R, S> AmqpResultConsumer<Boolean> receiveAndReply(ReceiveAndReplyCallback<R, S> callback, String replyExchange, String replyRoutingKey) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.receiveAndReply(callback, replyExchange, replyRoutingKey), transactionalEventPublisher);
    }

    /**
     * Receive a message if there is one from provided queue, invoke provided
     * {@link ReceiveAndReplyCallback} and send reply message, if the {@code callback}
     * returns one, to the provided {@code exchange} and {@code routingKey}.
     *
     * @param queueName       the queue name to receive a message.
     * @param callback        a user-provided {@link ReceiveAndReplyCallback} implementation to
     *                        process received message and return a reply message.
     * @param replyExchange   the exchange name to send reply message.
     * @param replyRoutingKey the routing key to send reply message.
     * @param <R>             The type of the request after conversion from the {@link Message}.
     * @param <S>             The type of the response.
     * @return {@code true}, if message was received
     * @throws AmqpException if there is a problem
     */
    <R, S> AmqpResultConsumer<Boolean> receiveAndReply(String queueName, ReceiveAndReplyCallback<R, S> callback, String replyExchange, String replyRoutingKey) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.receiveAndReply(queueName, callback, replyExchange, replyRoutingKey), transactionalEventPublisher);
    }

    /**
     * Receive a message if there is one from a default queue, invoke provided
     * {@link ReceiveAndReplyCallback} and send reply message, if the {@code callback}
     * returns one, to the {@code replyTo} {@link org.springframework.amqp.core.Address}
     * from result of {@link ReplyToAddressCallback}.
     *
     * @param callback               a user-provided {@link ReceiveAndReplyCallback} implementation to
     *                               process received message and return a reply message.
     * @param replyToAddressCallback the callback to determine replyTo address at runtime.
     * @param <R>                    The type of the request after conversion from the {@link Message}.
     * @param <S>                    The type of the response.
     * @return {@code true}, if message was received.
     * @throws AmqpException if there is a problem.
     */
    <R, S> AmqpResultConsumer<Boolean> receiveAndReply(ReceiveAndReplyCallback<R, S> callback,
                                                       ReplyToAddressCallback<S> replyToAddressCallback) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.receiveAndReply(callback, replyToAddressCallback), transactionalEventPublisher);
    }

    /**
     * Receive a message if there is one from provided queue, invoke provided
     * {@link ReceiveAndReplyCallback} and send reply message, if the {@code callback}
     * returns one, to the {@code replyTo} {@link org.springframework.amqp.core.Address}
     * from result of {@link ReplyToAddressCallback}.
     *
     * @param queueName              the queue name to receive a message.
     * @param callback               a user-provided {@link ReceiveAndReplyCallback} implementation to
     *                               process received message and return a reply message.
     * @param replyToAddressCallback the callback to determine replyTo address at runtime.
     * @param <R>                    The type of the request after conversion from the {@link Message}.
     * @param <S>                    The type of the response.
     * @return {@code true}, if message was received
     * @throws AmqpException if there is a problem
     */
    <R, S> AmqpResultConsumer<Boolean> receiveAndReply(String queueName, ReceiveAndReplyCallback<R, S> callback,
                                                       ReplyToAddressCallback<S> replyToAddressCallback) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.receiveAndReply(queueName, callback, replyToAddressCallback), transactionalEventPublisher);
    }

    // send and receive methods for messages

    /**
     * Basic RPC pattern. Send a message to a default exchange with a default routing key
     * and attempt to receive a response. Implementations will normally set the reply-to
     * header to an exclusive queue and wait up for some time limited by a timeout.
     *
     * @param message a message to send.
     * @return the response; or null if the reply times out.
     * @throws AmqpException if there is a problem.
     */
    public AmqpResultConsumer<Message> sendAndReceive(Message message) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.sendAndReceive(message), transactionalEventPublisher);
    }

    /**
     * Basic RPC pattern. Send a message to a default exchange with a specific routing key
     * and attempt to receive a response. Implementations will normally set the reply-to
     * header to an exclusive queue and wait up for some time limited by a timeout.
     *
     * @param routingKey the routing key.
     * @param message    a message to send.
     * @return the response; or null if the reply times out.
     * @throws AmqpException if there is a problem.
     */
    public AmqpResultConsumer<Message> sendAndReceive(String routingKey, Message message) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.sendAndReceive(routingKey, message), transactionalEventPublisher);
    }

    /**
     * Basic RPC pattern. Send a message to a specific exchange with a specific routing
     * key and attempt to receive a response. Implementations will normally set the
     * reply-to header to an exclusive queue and wait up for some time limited by a
     * timeout.
     *
     * @param exchange   the name of the exchange.
     * @param routingKey the routing key.
     * @param message    a message to send.
     * @return the response; or null if the reply times out.
     * @throws AmqpException if there is a problem.
     */
    public AmqpResultConsumer<Message> sendAndReceive(String exchange, String routingKey, Message message) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.sendAndReceive(exchange, routingKey, message), transactionalEventPublisher);
    }

    // send and receive methods with conversion

    /**
     * Basic RPC pattern with conversion. Send a Java object converted to a message to a
     * default exchange with a default routing key and attempt to receive a response,
     * converting that to a Java object. Implementations will normally set the reply-to
     * header to an exclusive queue and wait up for some time limited by a timeout.
     *
     * @param message a message to send.
     * @return the response; or null if the reply times out.
     * @throws AmqpException if there is a problem.
     */
    public AmqpResultConsumer<Object> convertSendAndReceive(Object message) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.convertSendAndReceive(message), transactionalEventPublisher);
    }

    /**
     * Basic RPC pattern with conversion. Send a Java object converted to a message to a
     * default exchange with a specific routing key and attempt to receive a response,
     * converting that to a Java object. Implementations will normally set the reply-to
     * header to an exclusive queue and wait up for some time limited by a timeout.
     *
     * @param routingKey the routing key.
     * @param message    a message to send.
     * @return the response; or null if the reply times out.
     * @throws AmqpException if there is a problem.
     */
    public AmqpResultConsumer<Object> convertSendAndReceive(String routingKey, Object message) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.convertSendAndReceive(routingKey, message), transactionalEventPublisher);
    }

    /**
     * Basic RPC pattern with conversion. Send a Java object converted to a message to a
     * specific exchange with a specific routing key and attempt to receive a response,
     * converting that to a Java object. Implementations will normally set the reply-to
     * header to an exclusive queue and wait up for some time limited by a timeout.
     *
     * @param exchange   the name of the exchange.
     * @param routingKey the routing key.
     * @param message    a message to send.
     * @return the response; or null if the reply times out.
     * @throws AmqpException if there is a problem.
     */
    public AmqpResultConsumer<Object> convertSendAndReceive(String exchange, String routingKey, Object message) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.convertSendAndReceive(exchange, routingKey, message), transactionalEventPublisher);
    }

    /**
     * Basic RPC pattern with conversion. Send a Java object converted to a message to a
     * default exchange with a default routing key and attempt to receive a response,
     * converting that to a Java object. Implementations will normally set the reply-to
     * header to an exclusive queue and wait up for some time limited by a timeout.
     *
     * @param message              a message to send.
     * @param messagePostProcessor a processor to apply to the message before it is sent.
     * @return the response; or null if the reply times out.
     * @throws AmqpException if there is a problem.
     */
    public AmqpResultConsumer<Object> convertSendAndReceive(Object message, MessagePostProcessor messagePostProcessor) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.convertSendAndReceive(message, messagePostProcessor), transactionalEventPublisher);
    }

    /**
     * Basic RPC pattern with conversion. Send a Java object converted to a message to a
     * default exchange with a specific routing key and attempt to receive a response,
     * converting that to a Java object. Implementations will normally set the reply-to
     * header to an exclusive queue and wait up for some time limited by a timeout.
     *
     * @param routingKey           the routing key.
     * @param message              a message to send.
     * @param messagePostProcessor a processor to apply to the message before it is sent.
     * @return the response; or null if the reply times out.
     * @throws AmqpException if there is a problem.
     */
    public AmqpResultConsumer<Object> convertSendAndReceive(String routingKey, Object message, MessagePostProcessor messagePostProcessor) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.convertSendAndReceive(routingKey, message, messagePostProcessor), transactionalEventPublisher);
    }

    /**
     * Basic RPC pattern with conversion. Send a Java object converted to a message to a
     * specific exchange with a specific routing key and attempt to receive a response,
     * converting that to a Java object. Implementations will normally set the reply-to
     * header to an exclusive queue and wait up for some time limited by a timeout.
     *
     * @param exchange             the name of the exchange.
     * @param routingKey           the routing key.
     * @param message              a message to send.
     * @param messagePostProcessor a processor to apply to the message before it is sent.
     * @return the response; or null if the reply times out.
     * @throws AmqpException if there is a problem.
     */
    public AmqpResultConsumer<Object> convertSendAndReceive(String exchange, String routingKey, Object message,
                                                            MessagePostProcessor messagePostProcessor) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.convertSendAndReceive(exchange, routingKey, message, messagePostProcessor), transactionalEventPublisher);
    }

    /**
     * Basic RPC pattern with conversion. Send a Java object converted to a message to a
     * default exchange with a default routing key and attempt to receive a response,
     * converting that to a Java object. Implementations will normally set the reply-to
     * header to an exclusive queue and wait up for some time limited by a timeout.
     * Requires a
     * {@link org.springframework.amqp.support.converter.SmartMessageConverter}.
     *
     * @param message      a message to send.
     * @param responseType the type to convert the reply to.
     * @param <T>          the type.
     * @return the response; or null if the reply times out.
     * @throws AmqpException if there is a problem.
     * @since 2.0
     */
    public <T> AmqpResultConsumer<T> convertSendAndReceiveAsType(Object message, ParameterizedTypeReference<T> responseType) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.convertSendAndReceiveAsType(message, responseType), transactionalEventPublisher);
    }

    /**
     * Basic RPC pattern with conversion. Send a Java object converted to a message to a
     * default exchange with a specific routing key and attempt to receive a response,
     * converting that to a Java object. Implementations will normally set the reply-to
     * header to an exclusive queue and wait up for some time limited by a timeout.
     * Requires a {@link org.springframework.amqp.support.converter.SmartMessageConverter}.
     *
     * @param routingKey   the routing key.
     * @param message      a message to send.
     * @param responseType the type to convert the reply to.
     * @param <T>          the type.
     * @return the response; or null if the reply times out.
     * @throws AmqpException if there is a problem.
     * @since 2.0
     */
    public <T> AmqpResultConsumer<T> convertSendAndReceiveAsType(String routingKey, Object message,
                                                                 ParameterizedTypeReference<T> responseType) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.convertSendAndReceiveAsType(routingKey, message, responseType), transactionalEventPublisher);
    }

    /**
     * Basic RPC pattern with conversion. Send a Java object converted to a message to a
     * specific exchange with a specific routing key and attempt to receive a response,
     * converting that to a Java object. Implementations will normally set the reply-to
     * header to an exclusive queue and wait up for some time limited by a timeout.
     * Requires a {@link org.springframework.amqp.support.converter.SmartMessageConverter}.
     *
     * @param exchange     the name of the exchange.
     * @param routingKey   the routing key.
     * @param message      a message to send.
     * @param responseType the type to convert the reply to.
     * @param <T>          the type.
     * @return the response; or null if the reply times out.
     * @throws AmqpException if there is a problem.
     * @since 2.0
     */
    public <T> AmqpResultConsumer<T> convertSendAndReceiveAsType(String exchange, String routingKey, Object message,
                                                                 ParameterizedTypeReference<T> responseType) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.convertSendAndReceiveAsType(exchange, routingKey, message, responseType), transactionalEventPublisher);
    }

    /**
     * Basic RPC pattern with conversion. Send a Java object converted to a message to a
     * default exchange with a default routing key and attempt to receive a response,
     * converting that to a Java object. Implementations will normally set the reply-to
     * header to an exclusive queue and wait up for some time limited by a timeout.
     * Requires a {@link org.springframework.amqp.support.converter.SmartMessageConverter}.
     *
     * @param message              a message to send.
     * @param messagePostProcessor a processor to apply to the message before it is sent.
     * @param responseType         the type to convert the reply to.
     * @param <T>                  the type.
     * @return the response; or null if the reply times out.
     * @throws AmqpException if there is a problem.
     * @since 2.0
     */
    public <T> AmqpResultConsumer<T> convertSendAndReceiveAsType(Object message, MessagePostProcessor messagePostProcessor,
                                                                 ParameterizedTypeReference<T> responseType) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.convertSendAndReceiveAsType(message, messagePostProcessor, responseType), transactionalEventPublisher);
    }

    /**
     * Basic RPC pattern with conversion. Send a Java object converted to a message to a
     * default exchange with a specific routing key and attempt to receive a response,
     * converting that to a Java object. Implementations will normally set the reply-to
     * header to an exclusive queue and wait up for some time limited by a timeout.
     * Requires a {@link org.springframework.amqp.support.converter.SmartMessageConverter}.
     *
     * @param routingKey           the routing key.
     * @param message              a message to send.
     * @param messagePostProcessor a processor to apply to the message before it is sent.
     * @param responseType         the type to convert the reply to.
     * @param <T>                  the type.
     * @return the response; or null if the reply times out.
     * @throws AmqpException if there is a problem.
     * @since 2.0
     */
    public <T> AmqpResultConsumer<T> convertSendAndReceiveAsType(String routingKey, Object message,
                                                                 MessagePostProcessor messagePostProcessor, ParameterizedTypeReference<T> responseType) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.convertSendAndReceiveAsType(routingKey, message, messagePostProcessor, responseType), transactionalEventPublisher);
    }

    /**
     * Basic RPC pattern with conversion. Send a Java object converted to a message to a
     * specific exchange with a specific routing key and attempt to receive a response,
     * converting that to a Java object. Implementations will normally set the reply-to
     * header to an exclusive queue and wait up for some time limited by a timeout.
     * Requires a {@link org.springframework.amqp.support.converter.SmartMessageConverter}.
     *
     * @param exchange             the name of the exchange.
     * @param routingKey           the routing key.
     * @param message              a message to send.
     * @param messagePostProcessor a processor to apply to the message before it is sent.
     * @param responseType         the type to convert the reply to.
     * @param <T>                  the type.
     * @return the response; or null if the reply times out.
     * @throws AmqpException if there is a problem.
     * @since 2.0
     */
    public <T> AmqpResultConsumer<T> convertSendAndReceiveAsType(String exchange, String routingKey, Object message, MessagePostProcessor messagePostProcessor, ParameterizedTypeReference<T> responseType) {
        return new AmqpResultConsumer<>(() -> amqpTemplate.convertSendAndReceiveAsType(exchange, routingKey, message, messagePostProcessor, responseType), transactionalEventPublisher);
    }

    /**
     * 获取延迟消息属性头
     *
     * @param delay 延迟时间
     */
    public MessagePostProcessor getDelayMessagePostProcessor(Duration delay) {
        return message -> messageDelayHandle(message, delay);
    }

    /**
     * 处理延迟消息属性头
     *
     * @param delay 延迟时间
     */
    public Message messageDelayHandle(Message message, Duration delay) {
        message.getMessageProperties().setDelayLong(delay.toMillis());
        return message;
    }

    /**
     * 获取过期消息属性头
     *
     * @param expiration 过期时间
     */
    public MessagePostProcessor getExpirationMessagePostProcessor(Duration expiration) {
        return message -> messageExpirationHandle(message, expiration);
    }

    /**
     * 处理过期消息属性头
     *
     * @param expiration 过期时间
     */
    public Message messageExpirationHandle(Message message, Duration expiration) {
        message.getMessageProperties().setExpiration(String.valueOf(expiration.toMillis()));
        return message;
    }

    /**
     * 最终提交的事务处理
     */
    public static class AmqpTransactionalProxy {

        protected final TransactionalApply apply;
        protected final TransactionalEventPublisher transactionalEventPublisher;

        public AmqpTransactionalProxy(TransactionalApply apply, TransactionalEventPublisher transactionalEventPublisher) {
            this.apply = apply;
            this.transactionalEventPublisher = transactionalEventPublisher;
        }

        /**
         * 在成功完成提交后触发事件
         */
        public void commit() {
            transactionalEventPublisher.commit(apply);
        }

        /**
         * 如果事务已回滚，则触发事件
         */
        public void rollback() {
            transactionalEventPublisher.rollback(apply);
        }

        /**
         * 在事务完成后触发事件(不管成功还是回滚)
         */
        public void completion() {
            transactionalEventPublisher.completion(apply);
        }
    }

    /**
     * 返回结果处理
     *
     * @param <R>
     */
    public static class AmqpResultConsumer<R> extends AmqpTransactionalProxy {

        private final Supplier<R> supplier;

        public AmqpResultConsumer(Supplier<R> supplier, TransactionalEventPublisher transactionalEventPublisher) {
            super(supplier::get, transactionalEventPublisher);
            this.supplier = supplier;
        }

        /**
         * 对结果进行处理
         *
         * @param consumer 对结果进行处理
         */
        public AmqpTransactionalProxy result(Consumer<R> consumer) {
            return new AmqpTransactionalProxy(() -> consumer.accept(supplier.get()), transactionalEventPublisher);
        }
    }
}
