package org.dromara.common.core.transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;

/**
 * 事务监听事件发布
 *
 * @author hexm
 * @date 2020/10/20 10:48
 */
@Component
public class TransactionalEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 在事务提交之前处理事件
     *
     * @param callback 回调事件
     * @see TransactionPhase#BEFORE_COMMIT
     */
    public void beforeCommit(TransactionalCallback callback) {
        applicationEventPublisher.publishEvent(new BeforeCommitTransactionalEvent(callback));
    }

    /**
     * 在提交成功完成后处理事件。
     *
     * @param callback 回调事件
     * @see TransactionPhase#AFTER_COMMIT
     */
    public void commit(TransactionalCallback callback) {
        applicationEventPublisher.publishEvent(new AlterCommitTransactionalEvent(callback));
    }

    /**
     * 如果事务已回滚，则处理该事件。
     *
     * @param callback 回调事件
     * @see TransactionPhase#AFTER_ROLLBACK
     */
    public void rollback(TransactionalCallback callback) {
        applicationEventPublisher.publishEvent(new RollbackTransactionalEvent(callback));
    }

    /**
     * 在事务完成后处理事件(不管成功还是回滚)
     * 对于更细粒度的事件，请分别使用 {@link #commit} 或 {@link #rollback} 截获事务提交或回滚。
     *
     * @param callback 回调事件
     * @see TransactionPhase#AFTER_COMPLETION
     */
    public void completion(TransactionalCallback callback) {
        applicationEventPublisher.publishEvent(new AlterCompletionTransactionalEvent(callback));
    }
}
