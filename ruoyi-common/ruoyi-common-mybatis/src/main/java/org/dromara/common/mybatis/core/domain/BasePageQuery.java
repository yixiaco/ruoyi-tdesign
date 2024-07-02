package org.dromara.common.mybatis.core.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.QueryGroup;
import org.dromara.common.core.validate.UserQueryGroup;

/**
 * 基础分页查询
 *
 * @author hexm
 * @date 2023/08/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BasePageQuery extends BaseEntity {

    /**
     * 页码
     */
    @NotNull(message = "页码不能为空", groups = {QueryGroup.class, UserQueryGroup.class})
    @Min(value = 0, message = "页码不能小于0", groups = {QueryGroup.class, UserQueryGroup.class})
    private Integer pageNum;

    /**
     * 每页大小
     */
    @NotNull(message = "每页大小不能为空", groups = {QueryGroup.class, UserQueryGroup.class})
    @Min(value = 1, message = "每页大小不能小于1", groups = {QueryGroup.class, UserQueryGroup.class})
    @Max(value = 100, message = "每页大小不能大于100", groups = {QueryGroup.class, UserQueryGroup.class})
    private Integer pageSize;
}
