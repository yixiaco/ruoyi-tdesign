package org.dromara.common.core.transactional;

import org.springframework.context.ApplicationEvent;

/**
 * 事务回滚事件
 *
 * @author hexm
 * @date 2020/10/20 10:57
 */
public class RollbackTransactionalEvent extends ApplicationEvent {

    public RollbackTransactionalEvent(TransactionalCallback source) {
        super(source);
    }

    public static RollbackTransactionalEvent of(TransactionalCallback apply) {
        return new RollbackTransactionalEvent(apply);
    }

    @Override
    public TransactionalCallback getSource() {
        return (TransactionalCallback) super.getSource();
    }
}
