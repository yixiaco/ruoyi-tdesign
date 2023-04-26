package org.dromara.common.core.transactional;

import org.springframework.context.ApplicationEvent;

/**
 * 事务回滚事件
 *
 * @author hexm
 * @date 2020/10/20 10:57
 */
public class RollbackTransactionalEvent extends ApplicationEvent {

    public RollbackTransactionalEvent(TransactionalApply source) {
        super(source);
    }

    public static RollbackTransactionalEvent of(TransactionalApply apply) {
        return new RollbackTransactionalEvent(apply);
    }

    /**
     * The object on which the Event initially occurred.
     *
     * @return The object on which the Event initially occurred.
     */
    @Override
    public TransactionalApply getSource() {
        return (TransactionalApply) super.getSource();
    }
}
