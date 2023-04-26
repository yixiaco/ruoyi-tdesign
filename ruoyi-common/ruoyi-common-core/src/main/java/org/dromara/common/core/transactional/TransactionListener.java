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
     * 在成功完成提交后触发事件
     *
     * @param event 监听事件
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAlterCommitHandler(AlterCommitTransactionalEvent event) {
        TransactionalApply apply = event.getSource();
        if (apply != null) {
            apply.apply();
        }
    }

    /**
     * 如果事务已回滚，则触发事件
     *
     * @param event 监听事件
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onRollbackHandler(RollbackTransactionalEvent event) {
        TransactionalApply apply = event.getSource();
        if (apply != null) {
            apply.apply();
        }
    }

    /**
     * 在事务完成后触发事件
     *
     * @param event 监听事件
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void onCompletionHandler(AlterCompletionTransactionalEvent event) {
        TransactionalApply apply = event.getSource();
        if (apply != null) {
            apply.apply();
        }
    }
}
