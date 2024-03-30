package org.dromara.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysDept;
import org.dromara.system.domain.query.SysDeptQuery;
import org.dromara.system.domain.vo.SysDeptVo;

import java.util.List;

/**
 * 部门管理 数据层
 *
 * @author Lion Li
 */
public interface SysDeptMapper extends BaseMapperPlus<SysDept, SysDeptVo> {

    /**
     * 查询部门列表
     *
     * @param dept
     * @return {@link SysDept}
     */
    @DataPermission({
        @DataColumn(key = "deptName", value = "dept_id")
    })
    List<SysDeptVo> queryList(SysDeptQuery dept);

    @DataPermission({
        @DataColumn(key = "deptName", value = "dept_id")
    })
    SysDeptVo selectDeptById(@Param("deptId") Long deptId);

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId            角色ID
     * @param deptCheckStrictly 部门树选择项是否关联显示
     * @return 选中部门列表
     */
    List<Long> selectDeptListByRoleId(@Param("roleId") Long roleId, @Param("deptCheckStrictly") boolean deptCheckStrictly);

    /**
     * 查询部门信息
     * @param deptId
     * @return
     */
    SysDeptVo queryVoById(@Param("deptId") Long deptId);
}
