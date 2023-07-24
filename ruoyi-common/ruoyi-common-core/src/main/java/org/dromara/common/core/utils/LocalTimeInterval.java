package org.dromara.common.core.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地线程计时器
 * 使用限制：不支持嵌套使用
 *
 * @author hexm
 * @date 2023/07/24 15:05
 */
public class LocalTimeInterval {

    private static final ThreadLocal<List<Long>> TIME_INTERVAL = new InheritableThreadLocal<>();

    /**
     * 开始计时
     */
    public static void start() {
        List<Long> timeInterval = new ArrayList<>();
        timeInterval.add(System.currentTimeMillis());
        TIME_INTERVAL.set(timeInterval);
    }

    /**
     * 添加标记计时
     *
     * @return 标记
     */
    public static int mark() {
        List<Long> list = TIME_INTERVAL.get();
        list.add(System.currentTimeMillis());
        return list.size() - 1;
    }

    /**
     * 获取标记时的时间戳
     *
     * @param mark 标记
     * @return 时间戳
     */
    public static long getMarkTime(int mark) {
        List<Long> list = TIME_INTERVAL.get();
        return list.get(mark);
    }

    /**
     * 获取开始到标记之间的执行时间
     *
     * @return 开始到标记之间的执行时间
     */
    public static Long getInterval(int mark) {
        List<Long> list = TIME_INTERVAL.get();
        Long start = list.get(0);
        Long markTime = list.get(mark);
        return Math.abs(markTime - start);
    }

    /**
     * 获取开始到最后一次标记之间的执行时间
     *
     * @return 开始到最后一次标记之间的执行时间
     */
    public static Long getInterval() {
        List<Long> list = TIME_INTERVAL.get();
        Long start = list.get(0);
        Long end = list.get(list.size() - 1);
        return end - start;
    }

    /**
     * 获取开始到当前时间之间的执行时间
     *
     * @return 开始到当前时间之间的执行时间
     */
    public static Long getCurrentInterval() {
        List<Long> list = TIME_INTERVAL.get();
        Long start = list.get(0);
        return System.currentTimeMillis() - start;
    }

    /**
     * 获取开始到最后一次标记之间的执行时间并清理数据
     *
     * @return 开始到最后一次标记之间的执行时间
     */
    public static Long getIntervalAndClear() {
        Long interval = getInterval();
        clear();
        return interval;
    }

    /**
     * 获取开始到当前时间之间的执行时间并清理数据
     *
     * @return 开始到当前时间之间的执行时间
     */
    public static Long getCurrentIntervalAndClear() {
        Long interval = getCurrentInterval();
        clear();
        return interval;
    }

    /**
     * 清理数据
     */
    public static void clear() {
        TIME_INTERVAL.remove();
    }
}
