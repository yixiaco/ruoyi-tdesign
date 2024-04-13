package org.dromara.common.core.transactional;

/**
 * 事务回调
 *
 * @author hexm
 */
@FunctionalInterface
public interface TransactionalCallback {

    /**
     * 回调方法
     */
    void callback();
}
