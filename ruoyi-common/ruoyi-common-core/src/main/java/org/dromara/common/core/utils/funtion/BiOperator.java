package org.dromara.common.core.utils.funtion;

import java.util.function.BinaryOperator;

/**
 * 两个值的比较运算操作
 *
 * @author hexm
 * @date 2023/03/09 17:26
 */
public interface BiOperator<T> extends BinaryOperator<T> {

    /**
     * 返回最小元素
     *
     * @param t1
     * @param t2
     * @param <T>
     * @return
     */
    static <T extends Comparable<T>> T min(T t1, T t2) {
        return t1.compareTo(t2) < 0 ? t1 : t2;
    }

    /**
     * 返回最大元素
     *
     * @param t1
     * @param t2
     * @param <T>
     * @return
     */
    static <T extends Comparable<T>> T max(T t1, T t2) {
        return t1.compareTo(t2) < 0 ? t2 : t1;
    }

    /**
     * 返回第一个元素
     *
     * @param t1
     * @param t2
     * @param <T>
     * @return
     */
    static <T> T first(T t1, T t2) {
        return t1;
    }

    /**
     * 返回最后一个元素
     *
     * @param t1
     * @param t2
     * @param <T>
     * @return
     */
    static <T> T last(T t1, T t2) {
        return t2;
    }
}
