package org.dromara.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.dromara.common.core.constant.HttpStatus;
import org.dromara.common.core.constant.UserConstants;
import org.dromara.common.core.enums.MenuTypeEnum;
import org.dromara.common.core.enums.ShowHiddenEnum;
import org.dromara.common.core.enums.YesNoEnum;
import org.dromara.common.core.enums.YesNoFrameEnum;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.TreeBuildUtils;
import org.dromara.common.mybatis.core.page.SortQuery;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.system.domain.SysMenu;
import org.dromara.system.domain.SysRole;
import org.dromara.system.domain.SysRoleMenu;
import org.dromara.system.domain.bo.SysMenuBo;
import org.dromara.system.domain.query.SysMenuQuery;
import org.dromara.system.domain.vo.MetaVo;
import org.dromara.system.domain.vo.RouterVo;
import org.dromara.system.domain.vo.SysMenuVo;
import org.dromara.system.domain.vo.SysTenantPackageVo;
import org.dromara.system.mapper.SysMenuMapper;
import org.dromara.system.mapper.SysRoleMapper;
import org.dromara.system.mapper.SysRoleMenuMapper;
import org.dromara.system.service.ISysMenuService;
import org.dromara.system.service.ISysTenantPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 菜单 业务层处理
 *
 * @author Lion Li
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private SysRoleMenuMapper roleMenuMapper;
    @Autowired
    private ISysTenantPackageService tenantPackageService;

    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysMenuVo> selectMenuList(Long userId) {
        SysMenuQuery query = new SysMenuQuery();
        query.setUserId(userId);
        return selectMenuList(query);
    }

    /**
     * 查询系统菜单列表
     *
     * @param query 查询条件
     * @return 菜单列表
     */
    @Override
    public List<SysMenuVo> selectMenuList(SysMenuQuery query) {
        List<SysMenuVo> menuList;
        // 管理员显示所有菜单信息
        if (LoginHelper.isSuperAdmin(query.getUserId())) {
            menuList = SortQuery.of(() -> mapper.queryList(query));
        } else {
            List<SysMenu> list = SortQuery.of(() -> mapper.selectMenuListByUserId(query));
            menuList = MapstructUtils.convert(list, SysMenuVo.class);
        }
        return menuList;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> perms = mapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(StringUtils.splitList(perm.trim()));
            }
        }
        return permsSet;
    }

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByRoleId(Long roleId) {
        List<String> perms = mapper.selectMenuPermsByRoleId(roleId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(StringUtils.splitList(perm.trim()));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户名称
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuTreeByUserId(Long userId) {
        List<SysMenu> menus;
        if (LoginHelper.isSuperAdmin(userId)) {
            menus = mapper.selectMenuTreeAll();
        } else {
            menus = mapper.selectMenuTreeByUserId(userId);
        }
        return getChildPerms(menus, 0);
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectOneById(roleId);
        return mapper.selectMenuListByRoleId(roleId, role.getMenuCheckStrictly());
    }

    /**
     * 根据租户套餐ID查询菜单树信息
     *
     * @param packageId 租户套餐ID
     * @return 选中菜单列表
     */
    @Override
    public List<Long> selectMenuListByPackageId(Long packageId) {
        SysTenantPackageVo tenantPackage = tenantPackageService.queryById(packageId);
        List<Long> menuIds = tenantPackage.getMenuIds();
        if (CollUtil.isEmpty(menuIds)) {
            return List.of();
        }
        List<Long> parentIds = null;
        if (tenantPackage.getMenuCheckStrictly()) {
            parentIds = mapper.selectObjs(query()
                .select(SysMenu::getParentId)
                .in(SysMenu::getMenuId, menuIds), Convert::toLong);
        }
        return mapper.selectObjs(query()
            .in(SysMenu::getMenuId, menuIds)
            .notIn(SysMenu::getMenuId, parentIds, CollUtil.isNotEmpty(parentIds)), Convert::toLong);
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        return buildMenus("", menus);
    }

    private List<RouterVo> buildMenus(String path, List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setName(menu.getRouteName());
            router.setPath(menu.getRouterPath());
            router.setComponent(menu.getComponentInfo());
            router.setQuery(getQueryParam(menu.getQueryParam()));
            MetaVo meta = new MetaVo();
            meta.setTitle(menu.getMenuName());
            meta.setIcon(menu.getIcon());
            meta.setNoCache(Objects.equals(YesNoEnum.NO.getCodeNum(), menu.getIsCache()));
            meta.setLink(menu.getPath());
            meta.setHidden(ShowHiddenEnum.HIDDEN.getCode().equals(menu.getVisible()));
            if (MenuTypeEnum.MENU.getType().equals(menu.getMenuType())) {
                meta.setComponentName(StringUtils.blankToDefault(menu.getComponentName(), router.getName()));
            }

            router.setMeta(meta);
            List<SysMenu> cMenus = menu.getChildren();
            if (CollUtil.isNotEmpty(cMenus) && MenuTypeEnum.DIRECTORY.getType().equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                String redirect = path + "/" + menu.getPath();
                router.setChildren(buildMenus(redirect, cMenus));
                RouterVo childRoute = router.getChildren().get(0);
                router.setRedirect(redirect + "/" + childRoute.getPath());
            } else if (menu.isMenuFrame()) {
                router.setMeta(meta);
                router.setPath(router.getPath() + menu.getPath());
                List<RouterVo> childrenList = new ArrayList<>();
                RouterVo children = new RouterVo();
                children.setPath("");
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setQuery(getQueryParam(menu.getQueryParam()));
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (Objects.equals(menu.getParentId(), 0L) && menu.isInnerLink()) {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<>();
                RouterVo children = new RouterVo();
                String routerPath = SysMenu.innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    private Map<String, Object> getQueryParam(String queryParam) {
        if (StrUtil.isBlank(queryParam)) {
            return null;
        }
        if (JSONUtil.isTypeJSONObject(queryParam)) {
            return JSONUtil.parseObj(queryParam);
        }
        return null;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    @Override
    public List<Tree<Long>> buildMenuTreeSelect(List<SysMenuVo> menus) {
        if (CollUtil.isEmpty(menus)) {
            return CollUtil.newArrayList();
        }
        return TreeBuildUtils.build(menus, (menu, tree) ->
            tree.setId(menu.getMenuId())
                .setParentId(menu.getParentId())
                .setName(menu.getMenuName())
                .setWeight(menu.getOrderNum()));
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public SysMenuVo selectMenuById(Long menuId) {
        return mapper.selectVoById(menuId);
    }

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean hasChildByMenuId(Long menuId) {
        return mapper.exists(query().eq(SysMenu::getParentId, menuId));
    }

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean checkMenuExistRole(Long menuId) {
        return roleMenuMapper.exists(query().eq(SysRoleMenu::getMenuId, menuId));
    }

    /**
     * 新增保存菜单信息
     *
     * @param bo 菜单信息
     * @return 结果
     */
    @Override
    public int insertMenu(SysMenuBo bo) {
        if (!checkMenuNameUnique(bo)) {
            throw new ServiceException("新增菜单'" + bo.getMenuName() + "'失败，菜单名称已存在");
        } else if (!checkMenuPathUnique(bo)) {
            throw new ServiceException("新增菜单'" + bo.getMenuName() + "'失败，菜单地址已存在");
        } else if (YesNoFrameEnum.YES.getCode().equals(bo.getIsFrame()) && !StringUtils.ishttp(bo.getPath())) {
            throw new ServiceException("新增菜单'" + bo.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        SysMenu menu = MapstructUtils.convert(bo, SysMenu.class);
        return mapper.insert(menu);
    }

    /**
     * 修改保存菜单信息
     *
     * @param bo 菜单信息
     * @return 结果
     */
    @Override
    public int updateMenu(SysMenuBo bo) {
        if (!checkMenuNameUnique(bo)) {
            throw new ServiceException("修改菜单'" + bo.getMenuName() + "'失败，菜单名称已存在");
        } else if (!checkMenuPathUnique(bo)) {
            throw new ServiceException("修改菜单'" + bo.getMenuName() + "'失败，菜单地址已存在");
        } else if (YesNoFrameEnum.YES.getCode().equals(bo.getIsFrame()) && !StringUtils.ishttp(bo.getPath())) {
            throw new ServiceException("修改菜单'" + bo.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (bo.getMenuId().equals(bo.getParentId())) {
            throw new ServiceException("修改菜单'" + bo.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        SysMenu menu = MapstructUtils.convert(bo, SysMenu.class);
        return mapper.update(menu);
    }

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public int deleteMenuById(Long menuId) {
        if (hasChildByMenuId(menuId)) {
            throw new ServiceException("存在子菜单,不允许删除", HttpStatus.WARN);
        }
        if (checkMenuExistRole(menuId)) {
            throw new ServiceException("菜单已分配,不允许删除", HttpStatus.WARN);
        }
        if (TenantHelper.isEnable() && tenantPackageService.includeMenuId(menuId)) {
            throw new ServiceException("菜单已被租户套餐分配，不允许删除", HttpStatus.WARN);
        }
        return mapper.deleteById(menuId);
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public boolean checkMenuNameUnique(SysMenuBo menu) {
        boolean exist = mapper.exists(query()
            .eq(SysMenu::getMenuName, menu.getMenuName())
            .eq(SysMenu::getParentId, menu.getParentId())
            .ne(SysMenu::getMenuId, menu.getMenuId(), ObjectUtil.isNotNull(menu.getMenuId())));
        return !exist;
    }

    /**
     * 校验菜单地址是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    private boolean checkMenuPathUnique(SysMenuBo menu) {
        boolean exist = queryChain()
            .eq(SysMenu::getPath, menu.getPath())
            .in(SysMenu::getMenuType, MenuTypeEnum.MENU.getType())
            .eq(SysMenu::getParentId, menu.getParentId())
            .ne(SysMenu::getMenuId, menu.getMenuId(), ObjectUtil.isNotNull(menu.getMenuId()))
            .exists();
        return !exist;
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    private List<SysMenu> getChildPerms(List<SysMenu> list, int parentId) {
        List<SysMenu> returnList = new ArrayList<>();
        for (SysMenu t : list) {
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = StreamUtils.filter(list, n -> n.getParentId().equals(t.getMenuId()));
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            // 判断是否有子节点
            if (list.stream().anyMatch(n -> n.getParentId().equals(tChild.getMenuId()))) {
                recursionFn(list, tChild);
            }
        }
    }
}
