package org.dromara.common.core.transactional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 事务监听
 *
 * @author hexm
 * @date 2020/6/9 14:18
 */
@Component
public class TransactionListener {

    /**
     * 在事务提交之前处理事件。
     *
     * @param event 监听事件
     * @see TransactionPhase#BEFORE_COMMIT
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onBeforeCommitHandler(BeforeCommitTransactionalEvent event) {
        TransactionalCallback apply = event.getSource();
        if (apply != null) {
            apply.callback();
        }
    }

    /**
     * 在提交成功完成后处理事件。
     * 注意：这是 AFTER_COMPLETION 的专用化，因此以与 AFTER_COMPLETION （而不是 ） TransactionSynchronization. afterCommit()相同的事件序列执行。
     *
     * @param event 监听事件
     * @see TransactionPhase#AFTER_COMMIT
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAlterCommitHandler(AlterCommitTransactionalEvent event) {
        TransactionalCallback apply = event.getSource();
        if (apply != null) {
            apply.callback();
        }
    }

    /**
     * 如果事务已回滚，则处理该事件。
     *
     * @param event 监听事件
     * @see TransactionPhase#AFTER_ROLLBACK
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onRollbackHandler(RollbackTransactionalEvent event) {
        TransactionalCallback apply = event.getSource();
        if (apply != null) {
            apply.callback();
        }
    }

    /**
     * 在事务完成后处理事件
     *
     * @param event 监听事件
     * @see TransactionPhase#AFTER_COMPLETION
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void onCompletionHandler(AlterCompletionTransactionalEvent event) {
        TransactionalCallback apply = event.getSource();
        if (apply != null) {
            apply.callback();
        }
    }
}
