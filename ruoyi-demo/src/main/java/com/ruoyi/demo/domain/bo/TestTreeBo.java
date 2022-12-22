package com.ruoyi.demo.domain.bo;

import com.ruoyi.common.core.domain.TreeEntity;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 测试树表业务对象 test_tree
 *
 * @author Lion Li
 * @date 2021-07-26
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TestTreeBo extends TreeEntity<TestTreeBo> {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 部门id
     */
    @NotNull(message = "部门id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long deptId;

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long userId;

    /**
     * 树节点名
     */
    @NotBlank(message = "树节点名不能为空", groups = {AddGroup.class, EditGroup.class})
    private String treeName;


    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;
}
