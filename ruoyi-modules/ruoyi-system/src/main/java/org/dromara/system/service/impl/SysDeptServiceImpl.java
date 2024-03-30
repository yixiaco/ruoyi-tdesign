package org.dromara.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.dromara.common.core.constant.CacheNames;
import org.dromara.common.core.enums.NormalDisableEnum;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.service.DeptService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.TreeBuildUtils;
import org.dromara.common.core.utils.spring.SpringUtils;
import org.dromara.common.mybatis.core.page.SortQuery;
import org.dromara.common.mybatis.helper.DbTypeHelper;
import org.dromara.common.redis.utils.CacheUtils;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.system.domain.SysDept;
import org.dromara.system.domain.SysRole;
import org.dromara.system.domain.SysUser;
import org.dromara.system.domain.bo.SysDeptBo;
import org.dromara.system.domain.query.SysDeptQuery;
import org.dromara.system.domain.vo.SysDeptVo;
import org.dromara.system.mapper.SysDeptMapper;
import org.dromara.system.mapper.SysRoleMapper;
import org.dromara.system.mapper.SysUserMapper;
import org.dromara.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 部门管理 服务实现
 *
 * @author Lion Li
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService, DeptService {

    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private SysUserMapper userMapper;

    /**
     * 查询部门管理数据
     *
     * @param query 部门查询对象
     * @return 部门信息集合
     */
    @Override
    public List<SysDeptVo> selectDeptList(SysDeptQuery query) {
        return SortQuery.of(() -> mapper.queryList(query));
    }

    /**
     * 查询部门树结构信息
     *
     * @param query 部门查询对象
     * @return 部门树信息集合
     */
    @Override
    public List<Tree<Long>> selectDeptTreeList(SysDeptQuery query) {
        // 只查询未禁用部门
        query.setStatus(NormalDisableEnum.NORMAL.getCode());
        List<SysDeptVo> depts = this.selectDeptList(query);
        return buildDeptTreeSelect(depts);
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<Tree<Long>> buildDeptTreeSelect(List<SysDeptVo> depts) {
        if (CollUtil.isEmpty(depts)) {
            return CollUtil.newArrayList();
        }
        return TreeBuildUtils.build(depts, (dept, tree) ->
            tree.setId(dept.getDeptId())
                .setParentId(dept.getParentId())
                .setName(dept.getDeptName())
                .setWeight(dept.getOrderNum()));
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<Long> selectDeptListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectOneById(roleId);
        return mapper.selectDeptListByRoleId(roleId, role.getDeptCheckStrictly());
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Cacheable(cacheNames = CacheNames.SYS_DEPT, key = "#deptId")
    @Override
    public SysDeptVo selectDeptById(Long deptId) {
        return mapper.queryVoById(deptId);
    }

    /**
     * 通过部门ID查询部门名称
     *
     * @param deptIds 部门ID串逗号分隔
     * @return 部门名称串逗号分隔
     */
    @Override
    public String selectDeptNameByIds(String deptIds) {
        List<String> list = new ArrayList<>();
        ISysDeptService proxy = SpringUtils.getAopProxy(this);
        for (Long id : StringUtils.splitTo(deptIds, Convert::toLong)) {
            SysDeptVo vo = proxy.selectDeptById(id);
            if (ObjectUtil.isNotNull(vo)) {
                list.add(vo.getDeptName());
            }
        }
        return String.join(StringUtils.SEPARATOR, list);
    }

    /**
     * 根据ID查询所有子部门数（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public long selectNormalChildrenDeptById(Long deptId) {
        return queryChain()
            .eq(SysDept::getStatus, NormalDisableEnum.NORMAL.getCode())
            .where(DbTypeHelper.findInSet(deptId, "ancestors"))
            .count();
    }

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(Long deptId) {
        return queryChain().eq(SysDept::getParentId, deptId).exists();
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        return userMapper.exists(QueryWrapper.create().eq(SysUser::getDeptId, deptId));
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public boolean checkDeptNameUnique(SysDeptBo dept) {
        boolean exist = queryChain()
            .eq(SysDept::getDeptName, dept.getDeptName())
            .eq(SysDept::getParentId, dept.getParentId())
            .ne(SysDept::getDeptId, dept.getDeptId(), ObjectUtil.isNotNull(dept.getDeptId()))
            .exists();
        return !exist;
    }

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门id
     */
    @Override
    public void checkDeptDataScope(Long deptId) {
        if (ObjectUtil.isNull(deptId)) {
            return;
        }
        if (LoginHelper.isSuperAdmin()) {
            return;
        }
        SysDeptVo dept = mapper.selectDeptById(deptId);
        if (ObjectUtil.isNull(dept)) {
            throw new ServiceException("没有权限访问部门数据！");
        }
    }

    /**
     * 新增保存部门信息
     *
     * @param bo 部门信息
     * @return 结果
     */
    @Override
    public int insertDept(SysDeptBo bo) {
        SysDept info = mapper.selectOneById(bo.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!NormalDisableEnum.NORMAL.getCode().equals(info.getStatus())) {
            throw new ServiceException("部门停用，不允许新增");
        }
        SysDept dept = MapstructUtils.convert(bo, SysDept.class);
        dept.setAncestors(info.getAncestors() + StringUtils.SEPARATOR + dept.getParentId());
        return mapper.insert(dept);
    }

    /**
     * 修改保存部门信息
     *
     * @param bo 部门信息
     * @return 结果
     */
    @CacheEvict(cacheNames = CacheNames.SYS_DEPT, key = "#bo.deptId")
    @Override
    public int updateDept(SysDeptBo bo) {
        SysDept dept = MapstructUtils.convert(bo, SysDept.class);
        SysDept newParentDept = mapper.selectOneById(dept.getParentId());
        SysDept oldDept = mapper.selectOneById(dept.getDeptId());
        if (ObjectUtil.isNotNull(newParentDept) && ObjectUtil.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + StringUtils.SEPARATOR + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        int result = mapper.update(dept);
        if (NormalDisableEnum.NORMAL.getCode().equals(dept.getStatus()) && StringUtils.isNotEmpty(dept.getAncestors())
            && !StringUtils.equals(NormalDisableEnum.NORMAL.getCode(), dept.getAncestors())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept);
        }
        return result;
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(SysDept dept) {
        String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        updateChain()
            .set(SysDept::getStatus, NormalDisableEnum.NORMAL.getCode())
            .in(SysDept::getDeptId, Arrays.asList(deptIds))
            .update();
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    private void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = queryChain().where(DbTypeHelper.findInSet(deptId, "ancestors")).list();
        List<SysDept> list = new ArrayList<>();
        for (SysDept child : children) {
            SysDept dept = new SysDept();
            dept.setDeptId(child.getDeptId());
            dept.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
            list.add(dept);
        }
        if (CollUtil.isNotEmpty(list)) {
            if (mapper.updateBatchById(list)) {
                list.forEach(dept -> CacheUtils.evict(CacheNames.SYS_DEPT, dept.getDeptId()));
            }
        }
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @CacheEvict(cacheNames = CacheNames.SYS_DEPT, key = "#deptId")
    @Override
    public int deleteDeptById(Long deptId) {
        return mapper.deleteById(deptId);
    }

}
