package org.dromara.common.core.transactional;

import org.springframework.context.ApplicationEvent;

/**
 * 事务完成后事件
 *
 * @author hexm
 * @date 2020/6/9 14:15
 */
public class AlterCompletionTransactionalEvent extends ApplicationEvent {
    public AlterCompletionTransactionalEvent(TransactionalCallback source) {
        super(source);
    }

    public static AlterCompletionTransactionalEvent of(TransactionalCallback apply) {
        return new AlterCompletionTransactionalEvent(apply);
    }

    @Override
    public TransactionalCallback getSource() {
        return (TransactionalCallback) super.getSource();
    }
}
