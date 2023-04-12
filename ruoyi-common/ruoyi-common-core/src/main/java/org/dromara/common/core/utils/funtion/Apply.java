package org.dromara.common.core.utils.funtion;

/**
 * 空函数执行
 *
 * @author hexm
 * @date 2023/03/09 17:30
 */
@FunctionalInterface
public interface Apply {

    /**
     * 执行函数
     */
    void apply();

    /**
     * 执行一个空函数
     *
     * @return
     */
    static Apply empty() {
        return () -> {
        };
    }

    /**
     * 一个复合函数
     *
     * @param after
     * @param <T>
     * @return
     */
    default <T> Apply andThen(Apply after) {
        return () -> {
            apply();
            after.apply();
        };
    }
}
