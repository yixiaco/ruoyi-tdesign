package com.ruoyi.system.mapper;

import com.ruoyi.common.mybatis.annotation.DataColumn;
import com.ruoyi.common.mybatis.annotation.DataPermission;
import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.bo.SysRoleBo;
import com.ruoyi.system.domain.vo.SysRoleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author Lion Li
 */
public interface SysRoleMapper extends BaseMapperPlus<SysRole, SysRoleVo> {

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRoleVo> selectRolePermissionByUserId(@Param("userId") Long userId);


    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    List<Long> selectRoleListByUserId(@Param("userId") Long userId);

    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id")
    })
    SysRoleVo selectRoleById(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查询角色
     *
     * @param userName 用户名
     * @return 角色列表
     */
    List<SysRoleVo> selectRolesByUserName(@Param("userName") String userName);

    /**
     * 查询角色信息列表
     *
     * @param bo bo对象
     * @return {@link SysRole}
     */
    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id")
    })
    List<SysRoleVo> queryList(SysRoleBo bo);
}
