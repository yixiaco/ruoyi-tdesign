package org.dromara.job.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 定时执行器事件
 *
 * @author hexm
 * @date 2023/06/09 16:32
 */
@Getter
@AllArgsConstructor
public class EveryJobEvent {

    /**
     * job参数
     */
    private String jobParam;
}
