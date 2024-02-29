package org.dromara.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysUser;
import org.dromara.system.domain.query.SysUserQuery;
import org.dromara.system.domain.vo.SysUserVo;

import java.util.List;

/**
 * 用户表 数据层
 *
 * @author Lion Li
 */
public interface SysUserMapper extends BaseMapperPlus<SysUser, SysUserVo> {

    /**
     * 查询用户信息列表
     *
     * @param query 查询对象
     * @return {@link SysUser}
     */
    @DataPermission({
        @DataColumn(key = "deptName", value = "sd.dept_id"),
        @DataColumn(key = "userName", value = "su.user_id")
    })
    List<SysUserVo> queryList(SysUserQuery query);

    /**
     * 根据条件分页查询已配用户角色列表
     *
     * @param query 查询条件
     * @return 用户信息集合信息
     */
    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.user_id")
    })
    List<SysUserVo> selectAllocatedList(SysUserQuery query);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param query 查询条件
     * @return 用户信息集合信息
     */
    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.user_id")
    })
    List<SysUserVo> selectUnallocatedList(SysUserQuery query);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @InterceptorIgnore(tenantLine = "true")
    SysUserVo selectUserByUserName(@Param("userName") String userName);

    /**
     * 通过手机号查询用户
     *
     * @param phonenumber 手机号
     * @return 用户对象信息
     */
    @InterceptorIgnore(tenantLine = "true")
    SysUserVo selectUserByPhonenumber(@Param("phonenumber") String phonenumber);

    /**
     * 通过邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户对象信息
     */
    @InterceptorIgnore(tenantLine = "true")
    SysUserVo selectUserByEmail(@Param("email") String email);

    /**
     * 通过权限查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.user_id")
    })
    SysUserVo selectSafeUserById(@Param("userId") Long userId);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    SysUserVo selectUserById(@Param("userId") Long userId);

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "dept_id"),
        @DataColumn(key = "userName", value = "user_id")
    })
    int update(@Param(Constants.ENTITY) SysUser user, @Param(Constants.WRAPPER) Wrapper<SysUser> updateWrapper);

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "dept_id"),
        @DataColumn(key = "userName", value = "user_id")
    })
    int updateById(@Param(Constants.ENTITY) SysUser user);

}
