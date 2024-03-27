package org.dromara.common.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 集合差异比较工具
 *
 * @author hexm
 * @date 2024/03/15 15:12
 */
public class CollDiffOps<T, K, R> {

    /** Left数据 */
    private final Collection<T> left;
    /** Left数据转为比较对象 */
    private final Function<T, R> leftConvert;
    /** Right数据 */
    private final Collection<K> right;
    /** Right数据转为比较对象 */
    private final Function<K, R> rightConvert;

    public CollDiffOps(Collection<T> left, Function<T, R> leftConvert, Collection<K> right, Function<K, R> rightConvert) {
        this.left = left;
        this.leftConvert = leftConvert;
        this.right = right;
        this.rightConvert = rightConvert;
    }

    public static <T> CollDiffOps<T, T, T> getOps(Collection<T> left, Collection<T> right) {
        return new CollDiffOps<>(left, Function.identity(), right, Function.identity());
    }

    public static <T, R> CollDiffOps<T, R, R> getOps(Collection<T> left, Function<T, R> leftConvert, Collection<R> right) {
        return new CollDiffOps<>(left, leftConvert, right, Function.identity());
    }

    public static <K, R> CollDiffOps<R, K, R> getOps(Collection<R> left, Collection<K> right, Function<K, R> rightConvert) {
        return new CollDiffOps<>(left, Function.identity(), right, rightConvert);
    }

    public static <T, K, R> CollDiffOps<T, K, R> getOps(Collection<T> left, Function<T, R> leftConvert, Collection<K> right, Function<K, R> rightConvert) {
        return new CollDiffOps<>(left, leftConvert, right, rightConvert);
    }

    public static <T, R> CollDiffOps<T, T, R> getUniOps(Collection<T> left, Collection<T> right, Function<T, R> convert) {
        return new CollDiffOps<>(left, convert, right, convert);
    }

    /**
     * 获取差异的Left对象
     *
     * @return
     */
    public List<T> getDiffLeft() {
        return getDiff(left, leftConvert, right, rightConvert, Function.identity());
    }

    /**
     * 获取差异的Left转换后对象
     *
     * @return
     */
    public List<R> getDiffConvertLeft() {
        return getDiff(left, leftConvert, right, rightConvert, leftConvert);
    }

    /**
     * 获取差异的Right对象
     *
     * @return
     */
    public List<K> getDiffRight() {
        return getDiff(right, rightConvert, left, leftConvert, Function.identity());
    }

    /**
     * 获取差异的Right转换后对象
     *
     * @return
     */
    public List<R> getDiffConvertRight() {
        return getDiff(right, rightConvert, left, leftConvert, rightConvert);
    }

    /**
     * 获取相同的Left对象
     *
     * @return
     */
    public List<T> getSameLeft() {
        return getSame(left, leftConvert, right, rightConvert, Function.identity());
    }

    /**
     * 获取相同的Left转换后对象
     *
     * @return
     */
    public List<R> getSameConvertLeft() {
        return getSame(left, leftConvert, right, rightConvert, leftConvert);
    }

    /**
     * 获取相同的Right对象
     *
     * @return
     */
    public List<K> getSameRight() {
        return getSame(right, rightConvert, left, leftConvert, Function.identity());
    }

    /**
     * 获取相同的Right转换后对象
     *
     * @return
     */
    public List<R> getSameConvertRight() {
        return getSame(right, rightConvert, left, leftConvert, rightConvert);
    }

    /**
     * 集合的数量与元素是否相同
     * <p>注意：
     * <p>此处比较的元素将经过convert转换，且不会比较元素的重复值计数。
     * <p>例如： [1,2,2]、[1,1,2]被认为是相等的，如果要判断元素的重复计数，则应该使用{@link #isMultiEquals}方法
     */
    public boolean isEquals() {
        if (left.size() != right.size()) {
            return false;
        }

        Set<R> leftSet = left.stream().map(leftConvert).collect(Collectors.toSet());
        Set<R> rightSet = right.stream().map(rightConvert).collect(Collectors.toSet());
        return leftSet.containsAll(rightSet);
    }

    /**
     * 比较集合的数量与元素是否完全相同，并且重复的元素计数也必须相同
     * <p>注意，此处比较的元素将经过convert转换
     */
    public boolean isMultiEquals() {
        if (left.size() != right.size()) {
            return false;
        }
        Map<R, Integer> leftCountMap = new HashMap<>();
        Map<R, Integer> rightCountMap = new HashMap<>();

        // 遍历第一个集合，统计元素出现次数
        for (T item : left) {
            R key = leftConvert.apply(item);
            leftCountMap.put(key, leftCountMap.getOrDefault(key, 0) + 1);
        }

        // 遍历第二个集合，统计元素出现次数
        for (K item : right) {
            R key = rightConvert.apply(item);
            rightCountMap.put(key, rightCountMap.getOrDefault(key, 0) + 1);
        }
        // 比较两个Map是否相等
        return leftCountMap.equals(rightCountMap);
    }

    /**
     * 集合的数量、顺序、元素是否完全相同
     * 注意，此处比较的元素将经过convert转换
     */
    public boolean isEqualsInOrder() {
        if (left.size() != right.size()) {
            return false;
        }
        List<T> lList = new ArrayList<>(left);
        List<K> rList = new ArrayList<>(right);

        for (int i = 0; i < lList.size(); i++) {
            if (!leftConvert.apply(lList.get(i)).equals(rightConvert.apply(rList.get(i)))) {
                return false;
            }
        }
        return true;
    }

    public <T0> CollDiffOps<T0, K, R> newLeft(Collection<T0> left, Function<T0, R> leftConvert) {
        return new CollDiffOps<>(left, leftConvert, right, rightConvert);
    }

    public <K0> CollDiffOps<T, K0, R> newRight(Collection<K0> right, Function<K0, R> rightConvert) {
        return new CollDiffOps<>(left, leftConvert, right, rightConvert);
    }

    public <R0> CollDiffOps<T, K, R0> newConvert(Function<T, R0> leftConvert, Function<K, R0> rightConvert) {
        return new CollDiffOps<>(left, leftConvert, right, rightConvert);
    }

    /**
     * 得到差异的left数据
     */
    private static <T, K, I, R> List<R> getDiff(Collection<T> left, Function<T, I> leftConvert,
                                                Collection<K> right, Function<K, I> rightConvert,
                                                Function<T, R> resultConvert) {
        Set<I> rightSet = right.stream()
            .map(rightConvert)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        return left.stream()
            .filter(t -> !rightSet.contains(leftConvert.apply(t)))
            .map(resultConvert)
            .collect(Collectors.toList());
    }

    /**
     * 得到相同的left数据
     */
    private static <T, K, I, R> List<R> getSame(Collection<T> left, Function<T, I> leftConvert,
                                                Collection<K> right, Function<K, I> rightConvert,
                                                Function<T, R> resultConvert) {
        Set<I> rightSet = right.stream()
            .map(rightConvert)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        return left.stream()
            .filter(t -> rightSet.contains(leftConvert.apply(t)))
            .map(resultConvert)
            .collect(Collectors.toList());
    }
}
