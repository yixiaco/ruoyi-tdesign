package org.dromara.job.events;

/**
 * 每天执行job事件
 *
 * @author hexm
 * @date 2023/06/09 16:33
 */
public class EveryDayJobEvent extends EveryJobEvent {
    public EveryDayJobEvent(String jobParam) {
        super(jobParam);
    }
}
