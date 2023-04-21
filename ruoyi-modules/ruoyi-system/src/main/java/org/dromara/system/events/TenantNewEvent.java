package org.dromara.system.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.system.domain.SysTenant;

import java.io.Serializable;

/**
 * 新租户事件，该事件会在忽略租户下执行
 *
 * @author hexm
 * @date 2023/04/21 09:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantNewEvent implements Serializable {

    private SysTenant tenant;
}
