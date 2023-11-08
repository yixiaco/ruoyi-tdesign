package org.dromara.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    @Transactional(rollbackFor = Exception.class)
    public void update(Long packageId, List<Long> menuIds) {
        lambdaUpdate().eq(SysTenantPackageMenu::getPackageId, packageId).remove();
        add(packageId, menuIds);
    }
}
