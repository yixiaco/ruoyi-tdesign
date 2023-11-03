package org.dromara.job.events;

/**
 * 每小时执行job事件
 *
 * @author hexm
 * @date 2023/06/09 16:33
 */
public class EveryHourJobEvent extends EveryJobEvent {
    public EveryHourJobEvent(String jobParam) {
        super(jobParam);
    }
}
