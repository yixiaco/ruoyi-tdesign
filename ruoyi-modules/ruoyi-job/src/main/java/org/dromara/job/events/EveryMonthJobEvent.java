package org.dromara.job.events;

/**
 * 每月执行job事件
 *
 * @author hexm
 * @date 2023/06/09 16:33
 */
public class EveryMonthJobEvent extends EveryJobEvent {
    public EveryMonthJobEvent(String jobParam) {
        super(jobParam);
    }
}
