package org.dromara.generator.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.generator.domain.GenTable;
import org.dromara.generator.domain.GenTableColumn;
import org.dromara.generator.domain.vo.GenTableOptions;
import org.dromara.generator.domain.vo.GenTableVo;

import java.util.List;

/**
 * 代码生成业务业务对象 gen_table
 *
 * @author yixiacoco
 * @date 2023-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMappers({
    @AutoMapper(target = GenTable.class, reverseConvertGenerate = false),
    @AutoMapper(target = GenTableVo.class, reverseConvertGenerate = false),
})
public class GenTableBo extends BaseEntity {

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

    /**
     * 表描述
     */
    @NotBlank(message = "表描述不能为空", groups = {AddGroup.class, EditGroup.class})
    private String tableComment;

    /**
     * 实体类名称
     */
    @NotBlank(message = "实体类名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String className;

    /**
     * 使用的模板（crud单表操作 tree树表操作）
     */
    @NotBlank(message = "使用的模板不能为空", groups = {AddGroup.class, EditGroup.class})
    private String tplCategory;

    /**
     * 生成包路径
     */
    @NotBlank(message = "生成包路径不能为空", groups = {AddGroup.class, EditGroup.class})
    private String packageName;

    /**
     * 生成模块名
     */
    @NotBlank(message = "生成模块名不能为空", groups = {AddGroup.class, EditGroup.class})
    private String moduleName;

    /**
     * 生成业务名
     */
    @NotBlank(message = "生成业务名不能为空", groups = {AddGroup.class, EditGroup.class})
    private String businessName;

    /**
     * 生成功能名
     */
    @NotBlank(message = "生成功能名不能为空", groups = {AddGroup.class, EditGroup.class})
    private String functionName;

    /**
     * 生成功能作者
     */
    @NotBlank(message = "生成功能作者不能为空", groups = {AddGroup.class, EditGroup.class})
    private String functionAuthor;

    /**
     * 生成代码方式（0zip压缩包 1自定义路径）
     */
    @NotBlank(message = "生成代码方式不能为空", groups = {AddGroup.class, EditGroup.class})
    private String genType;

    /**
     * 生成路径（不填默认项目路径）
     */
    private String genPath;

    /**
     * 其它生成选项
     */
    private String options;

    /**
     * 备注
     */
    private String remark;

    /**
     * 表列信息
     */
    @Valid
    private List<GenTableColumn> columns;

    /**
     * 选项
     */
    private GenTableOptions tableOptions;
}
