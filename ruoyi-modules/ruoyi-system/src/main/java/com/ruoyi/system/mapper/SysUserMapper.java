package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.mybatis.annotation.DataColumn;
import com.ruoyi.common.mybatis.annotation.DataPermission;
import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.domain.bo.SysUserBo;
import com.ruoyi.system.domain.vo.SysUserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表 数据层
 *
 * @author Lion Li
 */
public interface SysUserMapper extends BaseMapperPlus<SysUserMapper, SysUser, SysUserVo> {

    /**
     * 查询用户信息列表
     *
     * @param bo bo对象
     * @return {@link SysUser}
     */
    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.user_id")
    })
    List<SysUserVo> queryList(SysUserBo bo);

    /**
     * 根据条件分页查询已配用户角色列表
     *
     * @param user 查询条件
     * @return 用户信息集合信息
     */
    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.user_id")
    })
    List<SysUserVo> selectAllocatedList(SysUserBo user);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param queryWrapper 查询条件
     * @return 用户信息集合信息
     */
    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.user_id")
    })
    Page<SysUserVo> selectUnallocatedList(@Param("page") Page<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUserVo selectUserByUserName(String userName);

    /**
     * 通过手机号查询用户
     *
     * @param phonenumber 手机号
     * @return 用户对象信息
     */
    SysUserVo selectUserByPhonenumber(String phonenumber);

    /**
     * 通过用户名查询用户(不走租户插件)
     *
     * @param userName 用户名
     * @param tenantId 租户id
     * @return 用户对象信息
     */
    @InterceptorIgnore(tenantLine = "true")
    SysUserVo selectTenantUserByUserName(String userName, String tenantId);

    /**
     * 通过手机号查询用户(不走租户插件)
     *
     * @param phonenumber 手机号
     * @param tenantId    租户id
     * @return 用户对象信息
     */
    @InterceptorIgnore(tenantLine = "true")
    SysUserVo selectTenantUserByPhonenumber(String phonenumber, String tenantId);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    SysUserVo selectUserById(Long userId);

}
