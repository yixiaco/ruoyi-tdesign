package org.dromara.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.util.SqlUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.constant.CacheNames;
import org.dromara.common.core.constant.UserConstants;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.service.UserService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.helper.DbTypeHelper;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.system.domain.SysDept;
import org.dromara.system.domain.SysUser;
import org.dromara.system.domain.SysUserPost;
import org.dromara.system.domain.SysUserRole;
import org.dromara.system.domain.bo.SysUserBo;
import org.dromara.system.domain.query.SysRoleQuery;
import org.dromara.system.domain.query.SysUserQuery;
import org.dromara.system.domain.vo.SysPostVo;
import org.dromara.system.domain.vo.SysRoleVo;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.mapper.SysPostMapper;
import org.dromara.system.mapper.SysRoleMapper;
import org.dromara.system.mapper.SysUserMapper;
import org.dromara.system.mapper.SysUserPostMapper;
import org.dromara.system.mapper.SysUserRoleMapper;
import org.dromara.system.service.ISysDeptService;
import org.dromara.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 用户 业务层处理
 *
 * @author Lion Li
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService, UserService {

    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private SysPostMapper postMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    @Autowired
    private SysUserPostMapper userPostMapper;

    @Override
    public TableDataInfo<SysUserVo> selectPageUserList(SysUserQuery user) {
        if (user.getDeptId() != null) {
            List<SysDept> deptList = deptService.queryChain()
                .select(SysDept::getDeptId)
                .where(DbTypeHelper.findInSet(user.getDeptId(), "ancestors"))
                .list();
            List<Long> ids = StreamUtils.toList(deptList, SysDept::getDeptId);
            ids.add(user.getDeptId());
            user.setDeptIds(ids.toArray(Long[]::new));
        }
        return PageQuery.of(() -> mapper.queryList(user));
    }

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<SysUserVo> selectUserList(SysUserQuery user) {
        if (user.getDeptId() != null) {
            List<SysDept> deptList = deptService.queryChain()
                .select(SysDept::getDeptId)
                .where(DbTypeHelper.findInSet(user.getDeptId(), "ancestors"))
                .list();
            user.setDeptIds(deptList.stream().map(SysDept::getDeptId).toArray(Long[]::new));
        }
        return mapper.queryList(user);
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param query 用户查询对象
     * @return 用户信息集合信息
     */
    @Override
    public TableDataInfo<SysUserVo> selectAllocatedList(SysUserQuery query) {
        return PageQuery.of(() -> mapper.selectAllocatedList(query));
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param query 用户查询对象
     * @return 用户信息集合信息
     */
    @Override
    public TableDataInfo<SysUserVo> selectUnallocatedList(SysUserQuery query) {
        List<Long> userIds = userRoleMapper.selectUserIdsByRoleId(query.getRoleId());
        query.setUserIds(userIds);
        return PageQuery.of(() -> mapper.selectUnallocatedList(query));
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUserVo selectUserByUserName(String userName) {
        return mapper.selectUserByUserName(userName);
    }

    /**
     * 通过手机号查询用户
     *
     * @param phonenumber 手机号
     * @return 用户对象信息
     */
    @Override
    public SysUserVo selectUserByPhonenumber(String phonenumber) {
        return mapper.selectUserByPhonenumber(phonenumber);
    }

    /**
     * 通过权限查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUserVo selectSafeUserById(Long userId) {
        return mapper.selectSafeUserById(userId);
    }

    /**
     * 查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUserVo selectUserById(Long userId) {
        return mapper.selectUserById(userId);
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRoleVo> list = roleMapper.selectRolesByUserName(userName);
        if (CollUtil.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return StreamUtils.join(list, SysRoleVo::getRoleName);
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPostVo> list = postMapper.selectPostsByUserName(userName);
        if (CollUtil.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return StreamUtils.join(list, SysPostVo::getPostName);
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkUserNameUnique(SysUserBo user) {
        boolean exist = mapper.exists(query()
            .eq(SysUser::getUserName, user.getUserName())
            .ne(SysUser::getUserId, user.getUserId(), ObjectUtil.isNotNull(user.getUserId())));
        return !exist;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     */
    @Override
    public boolean checkPhoneUnique(SysUserBo user) {
        boolean exist = mapper.exists(query()
            .eq(SysUser::getPhonenumber, user.getPhonenumber())
            .ne(SysUser::getUserId, user.getUserId(), ObjectUtil.isNotNull(user.getUserId())));
        return !exist;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     */
    @Override
    public boolean checkEmailUnique(SysUserBo user) {
        boolean exist = mapper.exists(query()
            .eq(SysUser::getEmail, user.getEmail())
            .ne(SysUser::getUserId, user.getUserId(), ObjectUtil.isNotNull(user.getUserId())));
        return !exist;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param userId 用户ID
     */
    @Override
    public void checkUserAllowed(Long userId) {
        if (ObjectUtil.isNotNull(userId) && LoginHelper.isSuperAdmin(userId)) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    @Override
    public void checkUserDataScope(Long userId) {
        if (ObjectUtil.isNull(userId)) {
            return;
        }
        if (LoginHelper.isSuperAdmin()) {
            return;
        }
        if (ObjectUtil.isNull(mapper.selectSafeUserById(userId))) {
            throw new ServiceException("没有权限访问用户数据！");
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertUser(SysUserBo user) {
        SysUser sysUser = MapstructUtils.convert(user, SysUser.class);
        // 新增用户信息
        int rows = mapper.insert(sysUser);
        user.setUserId(sysUser.getUserId());
        // 新增用户岗位关联
        insertUserPost(user, false);
        // 新增用户与角色管理
        insertUserRole(user, false);
        return rows;
    }

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean registerUser(SysUserBo user, String tenantId) {
        user.setCreateBy(user.getUserId());
        user.setUpdateBy(user.getUserId());
        SysUser sysUser = MapstructUtils.convert(user, SysUser.class);
        sysUser.setTenantId(tenantId);
        return mapper.insert(sysUser) > 0;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUser(SysUserBo user) {
        // 新增用户与角色管理
        insertUserRole(user, true);
        // 新增用户与岗位管理
        insertUserPost(user, true);
        SysUser sysUser = MapstructUtils.convert(user, SysUser.class);
        // 防止错误更新后导致的数据误删除
        int flag = mapper.update(sysUser, true);
        if (flag < 1) {
            throw new ServiceException("修改用户" + user.getUserName() + "信息失败");
        }
        return flag;
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserAuth(Long userId, Long[] roleIds) {
        insertUserRole(userId, roleIds, true);
    }

    /**
     * 修改用户状态
     *
     * @param userId 用户ID
     * @param status 帐号状态
     * @return 结果
     */
    @Override
    public boolean updateUserStatus(Long userId, String status) {
        SysUser sysUser = mapper.getUpdateEntity(userId);
        sysUser.setStatus(status);
        return SqlUtil.toBool(mapper.update(sysUser, true));
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean updateUserProfile(SysUserBo user) {
        SysUser entity = mapper.getUpdateEntity(user.getUserId());
        mapper.getUpdateWrapper(entity)
            .set(SysUser::getNickName, user.getNickName(), ObjectUtil.isNotNull(user.getNickName()))
            .set(SysUser::getPhonenumber, user.getPhonenumber())
            .set(SysUser::getEmail, user.getEmail())
            .set(SysUser::getSex, user.getSex());
        return SqlUtil.toBool(mapper.update(entity, true));
    }

    /**
     * 修改用户头像
     *
     * @param userId 用户ID
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(Long userId, Long avatar) {
        SysUser sysUser = mapper.getUpdateEntity(userId);
        sysUser.setAvatar(avatar);
        return SqlUtil.toBool(mapper.update(sysUser, true));
    }

    /**
     * 重置用户密码
     *
     * @param userId   用户ID
     * @param password 密码
     * @return 结果
     */
    @Override
    public boolean resetUserPwd(Long userId, String password) {
        SysUser sysUser = mapper.getUpdateEntity(userId);
        sysUser.setPassword(password);
        return SqlUtil.toBool(mapper.update(sysUser, true));
    }

    /**
     * 新增用户角色信息
     *
     * @param user  用户对象
     * @param clear 清除已存在的关联数据
     */
    private void insertUserRole(SysUserBo user, boolean clear) {
        this.insertUserRole(user.getUserId(), user.getRoleIds(), clear);
    }

    /**
     * 新增用户岗位信息
     *
     * @param user  用户对象
     * @param clear 清除已存在的关联数据
     */
    private void insertUserPost(SysUserBo user, boolean clear) {
        Long[] posts = user.getPostIds();
        if (ArrayUtil.isNotEmpty(posts)) {
            if (clear) {
                // 删除用户与岗位关联
                userPostMapper.deleteByQuery(query().eq(SysUserPost::getUserId, user.getUserId()));
            }
            // 新增用户与岗位管理
            List<SysUserPost> list = StreamUtils.toList(List.of(posts), postId -> {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                return up;
            });
            userPostMapper.insertBatch(list);
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     * @param clear   清除已存在的关联数据
     */
    private void insertUserRole(Long userId, Long[] roleIds, boolean clear) {
        if (ArrayUtil.isNotEmpty(roleIds)) {
            // 判断是否具有此角色的操作权限
            List<SysRoleVo> roles = roleMapper.queryList(new SysRoleQuery());
            if (CollUtil.isEmpty(roles)) {
                throw new ServiceException("没有权限访问角色的数据");
            }
            List<Long> roleList = StreamUtils.toList(roles, SysRoleVo::getRoleId);
            if (!LoginHelper.isSuperAdmin(userId)) {
                roleList.remove(UserConstants.SUPER_ADMIN_ID);
            }
            List<Long> canDoRoleList = StreamUtils.filter(List.of(roleIds), roleList::contains);
            if (CollUtil.isEmpty(canDoRoleList)) {
                throw new ServiceException("没有权限访问角色的数据");
            }
            if (clear) {
                // 删除用户与角色关联
                userRoleMapper.deleteByQuery(query().eq(SysUserRole::getUserId, userId));
            }
            // 新增用户与角色管理
            List<SysUserRole> list = StreamUtils.toList(canDoRoleList, roleId -> {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                return ur;
            });
            userRoleMapper.insertBatch(list);
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserById(Long userId) {
        SysUser user = getById(userId);
        // 删除用户与角色关联
        userRoleMapper.deleteByQuery(query().eq(SysUserRole::getUserId, userId));
        // 删除用户与岗位表
        userPostMapper.deleteByQuery(query().eq(SysUserPost::getUserId, userId));
        // 防止更新失败导致的数据删除
        int flag = mapper.deleteById(userId);
        if (flag < 1) {
            throw new ServiceException("删除用户失败!");
        }
        // 删除用户时，退出用户的登录状态
        LoginHelper.getStpLogic().logout(user.getUserType() + ":" + user.getUserId());
        return flag;
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(userId);
            checkUserDataScope(userId);
        }
        List<SysUser> users = listByIds(Arrays.asList(userIds));
        List<Long> ids = List.of(userIds);
        // 删除用户与角色关联
        userRoleMapper.deleteByQuery(query().in(SysUserRole::getUserId, ids));
        // 删除用户与岗位表
        userPostMapper.deleteByQuery(query().in(SysUserPost::getUserId, ids));
        // 防止更新失败导致的数据删除
        int flag = mapper.deleteBatchByIds(ids);
        if (flag < 1) {
            throw new ServiceException("删除用户失败!");
        }
        // 删除用户时，退出用户的登录状态
        for (SysUser user : users) {
            LoginHelper.getStpLogic().logout(user.getUserType() + ":" + user.getUserId());
        }
        return flag;
    }

    /**
     * 通过部门id查询当前部门所有用户
     *
     * @param deptId
     * @return
     */
    @Override
    public List<SysUserVo> selectUserListByDept(Long deptId) {
        return mapper.selectVoList(query()
            .eq(SysUser::getDeptId, deptId)
            .orderBy(SysUser::getUserId, true));
    }

    @Cacheable(cacheNames = CacheNames.SYS_USER_NAME, key = "#userId")
    @Override
    public String selectUserNameById(Long userId) {
        SysUser sysUser = mapper.selectOneByQuery(query()
            .select(SysUser::getUserName)
            .eq(SysUser::getUserId, userId));
        return ObjectUtil.isNull(sysUser) ? null : sysUser.getUserName();
    }

    @Override
    @Cacheable(cacheNames = CacheNames.SYS_NICKNAME, key = "#userId")
    public String selectNicknameById(Long userId) {
        SysUser sysUser = mapper.selectOneByQuery(query()
            .select(SysUser::getNickName)
            .eq(SysUser::getUserId, userId));
        return ObjectUtil.isNull(sysUser) ? null : sysUser.getNickName();
    }
}
