package org.dromara.common.core.transactional;

import org.springframework.context.ApplicationEvent;

/**
 * 成功完成提交后事件
 *
 * @author hexm
 * @date 2020/6/9 14:15
 */
public class AlterCommitTransactionalEvent extends ApplicationEvent {
    public AlterCommitTransactionalEvent(TransactionalCallback source) {
        super(source);
    }

    public static AlterCommitTransactionalEvent of(TransactionalCallback apply) {
        return new AlterCommitTransactionalEvent(apply);
    }

    @Override
    public TransactionalCallback getSource() {
        return (TransactionalCallback) super.getSource();
    }
}
