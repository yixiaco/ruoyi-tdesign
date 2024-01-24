package org.dromara.job.events;

/**
 * 每分钟执行job事件
 *
 * @author hexm
 * @date 2023/06/09 16:33
 */
public class EveryMinuteJobEvent extends EveryJobEvent {
    public EveryMinuteJobEvent(String jobParam) {
        super(jobParam);
    }
}
