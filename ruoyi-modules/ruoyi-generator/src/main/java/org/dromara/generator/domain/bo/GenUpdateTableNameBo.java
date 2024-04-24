package org.dromara.generator.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;

import java.io.Serializable;

/**
 * 修改表名业务对象
 *
 * @author yixiacoco
 * @date 2023-08-04
 */
@Data
public class GenUpdateTableNameBo implements Serializable {

    /**
     * 编号
     */
    @NotNull(message = "编号不能为空", groups = {EditGroup.class})
    private Long tableId;

    /**
     * 数据源名称
     */
    @NotBlank(message = "数据源名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String dataName;

    /**
     * 表名称
     */
    @NotBlank(message = "表名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String tableName;
}
