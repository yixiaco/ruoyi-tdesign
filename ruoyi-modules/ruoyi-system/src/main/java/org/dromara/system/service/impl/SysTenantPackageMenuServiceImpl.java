package org.dromara.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.common.tenant.annotation.IgnoreTenant;
import org.dromara.system.domain.SysTenantPackageMenu;
import org.dromara.system.mapper.SysTenantPackageMenuMapper;
import org.dromara.system.service.ISysTenantPackageMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 租户套餐和菜单关联Service业务层处理
 *
 * @author hexm
 * @date 2023-11-08
 */
@Service
public class SysTenantPackageMenuServiceImpl extends ServiceImpl<SysTenantPackageMenuMapper, SysTenantPackageMenu> implements ISysTenantPackageMenuService {

    /**
     * 更新租户套餐与菜单关联
     *
     * @param packageId 租户id
     * @param menuIds   菜单id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Long packageId, List<Long> menuIds) {
        List<SysTenantPackageMenu> list = menuIds.stream().map(menuId -> {
            SysTenantPackageMenu tenantPackageMenu = new SysTenantPackageMenu();
            tenantPackageMenu.setPackageId(packageId);
            tenantPackageMenu.setMenuId(menuId);
            return tenantPackageMenu;
        }).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(list)) {
            saveBatch(list);
        }
    }

    /**
     * 更新租户套餐与菜单关联
     *
     * @param packageId 租户id
     * @param menuIds   菜单id
     */
    @Override
    @IgnoreTenant
    @Transactional(rollbackFor = Exception.class)
    public void update(Long packageId, List<Long> menuIds) {
        List<SysTenantPackageMenu> list = lambdaQuery().eq(SysTenantPackageMenu::getPackageId, packageId).list();
        List<Long> olMenuIds = StreamUtils.toList(list, SysTenantPackageMenu::getMenuId);
        StreamUtils.diff(olMenuIds, menuIds, (id1, id2) -> {
            // 只有旧菜单才有的数据，需要删除
            if (CollUtil.isNotEmpty(id1)) {
                lambdaUpdate()
                    .in(SysTenantPackageMenu::getMenuId, id1)
                    .eq(SysTenantPackageMenu::getPackageId, packageId)
                    .remove();
            }
            // 只有新菜单才有的数据，需要增加
            if (CollUtil.isNotEmpty(id2)) {
                add(packageId, id2);
            }

            // 同步租户菜单

            // 1.删除租户角色多余菜单
            if (CollUtil.isNotEmpty(id1)) {
                baseMapper.deleteTenantRoleMenuId(packageId, id1);
            }

            // 2.添加租户管理员角色新增菜单
            if (CollUtil.isNotEmpty(id2)) {
                baseMapper.addTenantRoleAdminMenuId(packageId, id2);
            }
        });
    }
}
