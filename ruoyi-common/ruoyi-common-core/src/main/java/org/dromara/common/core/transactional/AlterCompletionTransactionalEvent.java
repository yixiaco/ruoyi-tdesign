package org.dromara.common.core.transactional;

import org.springframework.context.ApplicationEvent;

/**
 * 事务完成后事件
 *
 * @author hexm
 * @date 2020/6/9 14:15
 */
public class AlterCompletionTransactionalEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public AlterCompletionTransactionalEvent(TransactionalApply source) {
        super(source);
    }

    public static AlterCompletionTransactionalEvent of(TransactionalApply apply) {
        return new AlterCompletionTransactionalEvent(apply);
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
