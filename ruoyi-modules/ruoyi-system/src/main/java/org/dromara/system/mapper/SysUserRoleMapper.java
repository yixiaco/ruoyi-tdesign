package org.dromara.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.MyBaseMapperVo;
import org.dromara.system.domain.SysUserRole;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 *
 * @author Lion Li
 */
public interface SysUserRoleMapper extends MyBaseMapperVo<SysUserRole, SysUserRole> {

    List<Long> selectUserIdsByRoleId(@Param("roleId") Long roleId);

}
