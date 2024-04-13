package org.dromara.common.core.transactional;

import org.springframework.context.ApplicationEvent;

/**
 * 提交事务前事件
 *
 * @author hexm
 * @date 2024/4/13
 */
public class BeforeCommitTransactionalEvent extends ApplicationEvent {
    public BeforeCommitTransactionalEvent(TransactionalCallback source) {
        super(source);
    }

    public static BeforeCommitTransactionalEvent of(TransactionalCallback apply) {
        return new BeforeCommitTransactionalEvent(apply);
    }

    @Override
    public TransactionalCallback getSource() {
        return (TransactionalCallback) super.getSource();
    }
}
