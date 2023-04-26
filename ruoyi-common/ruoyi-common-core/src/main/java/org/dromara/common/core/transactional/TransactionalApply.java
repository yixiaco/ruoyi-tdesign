package org.dromara.common.core.transactional;

/**
 * 事务处理应用
 *
 * @author hexm
 */
@FunctionalInterface
public interface TransactionalApply {

    /**
     * 执行
     */
    void apply();
}
