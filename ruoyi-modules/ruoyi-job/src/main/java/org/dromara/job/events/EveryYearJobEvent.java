package org.dromara.job.events;

/**
 * 每年执行job事件
 *
 * @author hexm
 * @date 2023/06/09 16:33
 */
public class EveryYearJobEvent extends EveryJobEvent {
    public EveryYearJobEvent(String jobParam) {
        super(jobParam);
    }
}
