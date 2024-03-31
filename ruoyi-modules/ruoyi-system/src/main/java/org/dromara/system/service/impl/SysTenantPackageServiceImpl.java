package org.dromara.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.dromara.common.core.enums.NormalDisableEnum;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.SysTenant;
import org.dromara.system.domain.SysTenantPackage;
import org.dromara.system.domain.bo.SysTenantPackageBo;
import org.dromara.system.domain.query.SysTenantPackageQuery;
import org.dromara.system.domain.vo.SysTenantPackageVo;
import org.dromara.system.mapper.SysTenantPackageMapper;
import org.dromara.system.service.ISysTenantPackageMenuService;
import org.dromara.system.service.ISysTenantPackageService;
import org.dromara.system.service.ISysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 租户套餐Service业务层处理
 *
 * @author Michelle.Chung
 */
@Service
public class SysTenantPackageServiceImpl extends ServiceImpl<SysTenantPackageMapper, SysTenantPackage> implements ISysTenantPackageService {

    @Autowired
    private ISysTenantService tenantService;
    @Autowired
    private ISysTenantPackageMenuService tenantPackageMenuService;

    /**
     * 查询租户套餐
     */
    @Override
    public SysTenantPackageVo queryById(Long packageId) {
        return mapper.queryById(packageId);
    }

    /**
     * 查询租户套餐列表
     */
    @Override
    public TableDataInfo<SysTenantPackageVo> queryPageList(SysTenantPackageQuery query) {
        return PageQuery.of(() -> mapper.queryList(query));
    }

    @Override
    public List<SysTenantPackageVo> selectList() {
        return mapper.selectVoList(query()
                .eq(SysTenantPackage::getStatus, NormalDisableEnum.NORMAL.getCode()));
    }

    /**
     * 查询租户套餐列表
     */
    @Override
    public List<SysTenantPackageVo> queryList(SysTenantPackageQuery query) {
        return mapper.queryList(query);
    }

    /**
     * 新增租户套餐
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(SysTenantPackageBo bo) {
        SysTenantPackage add = MapstructUtils.convert(bo, SysTenantPackage.class);
        boolean flag = mapper.insert(add) > 0;
        if (flag) {
            bo.setPackageId(add.getPackageId());
            // 保存菜单id
            tenantPackageMenuService.add(add.getPackageId(), bo.getMenuIds());
        }
        return flag;
    }

    /**
     * 修改租户套餐
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(SysTenantPackageBo bo) {
        SysTenantPackage update = MapstructUtils.convert(bo, SysTenantPackage.class);
        boolean b = updateById(update);
        if (b) {
            // 保存菜单id
            tenantPackageMenuService.update(bo.getPackageId(), bo.getMenuIds());
        }
        return b;
    }

    /**
     * 修改套餐状态
     *
     * @param bo 套餐信息
     * @return 结果
     */
    @Override
    public int updatePackageStatus(SysTenantPackageBo bo) {
        SysTenantPackage tenantPackage = MapstructUtils.convert(bo, SysTenantPackage.class);
        return mapper.update(tenantPackage);
    }

    /**
     * 批量删除租户套餐
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            boolean exists = tenantService.exists(query().in(SysTenant::getPackageId, ids));
            if (exists) {
                throw new ServiceException("租户套餐已被使用");
            }
        }
        return mapper.deleteBatchByIds(ids) > 0;
    }

    /**
     * 是否包含分配菜单
     *
     * @param menuId 菜单id
     * @return
     */
    @Override
    public boolean includeMenuId(Long menuId) {
        // 使用插件分页兼容数据库
        return PageQuery.of(0,1,false).get(() -> mapper.queryIncludeMenuId(menuId));
    }
}
