package org.dromara.common.core.transactional;

import org.springframework.context.ApplicationEvent;

/**
 * 成功完成提交后事件
 *
 * @author hexm
 * @date 2020/6/9 14:15
 */
public class AlterCommitTransactionalEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public AlterCommitTransactionalEvent(TransactionalApply source) {
        super(source);
    }

    public static AlterCommitTransactionalEvent of(TransactionalApply apply) {
        return new AlterCommitTransactionalEvent(apply);
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
