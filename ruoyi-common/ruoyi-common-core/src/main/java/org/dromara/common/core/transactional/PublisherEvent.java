package org.dromara.common.core.transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 事务监听事件发布
 *
 * @author hexm
 * @date 2020/10/20 10:48
 */
@Component
public class PublisherEvent {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 在成功完成提交后触发事件
     *
     * @param apply 执行事件
     */
    public void commit(TransactionalApply apply) {
        applicationEventPublisher.publishEvent(new AlterCommitTransactionalEvent(apply));
    }

    /**
     * 如果事务已回滚，则触发事件
     *
     * @param apply 执行事件
     */
    public void rollback(TransactionalApply apply) {
        applicationEventPublisher.publishEvent(new RollbackTransactionalEvent(apply));
    }

    /**
     * 在事务完成后触发事件(不管成功还是回滚)
     *
     * @param apply 执行事件
     */
    public void completion(TransactionalApply apply) {
        applicationEventPublisher.publishEvent(new AlterCompletionTransactionalEvent(apply));
    }
}
