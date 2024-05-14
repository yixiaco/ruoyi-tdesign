package org.dromara.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.dromara.common.core.constant.GlobalConstants;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.service.TenantAppService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.system.domain.SysTenantApp;
import org.dromara.system.domain.bo.SysTenantAppBo;
import org.dromara.system.domain.query.SysTenantAppQuery;
import org.dromara.system.domain.vo.SysTenantAppVo;
import org.dromara.system.mapper.SysTenantAppMapper;
import org.dromara.system.service.ISysTenantAppService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 租户应用管理Service业务层处理
 *
 * @author yixiacoco
 * @date 2023-05-17
 */
@Service
public class SysTenantAppServiceImpl extends ServiceImpl<SysTenantAppMapper, SysTenantApp> implements ISysTenantAppService, TenantAppService {

    /**
     * 查询租户应用管理
     *
     * @param appid 主键
     * @return SysTenantAppVo
     */
    @Override
    public SysTenantAppVo queryById(Long appid) {
        return baseMapper.selectVoById(appid);
    }

    /**
     * 查询租户应用管理列表
     *
     * @param query 查询对象
     * @return SysTenantAppVo
     */
    @Override
    public TableDataInfo<SysTenantAppVo> queryPageList(SysTenantAppQuery query) {
        return PageQuery.of(() -> baseMapper.queryList(query));
    }

    /**
     * 查询租户应用管理列表
     *
     * @param query 查询对象
     * @return SysTenantAppVo
     */
    @Override
    public List<SysTenantAppVo> queryList(SysTenantAppQuery query) {
        return baseMapper.queryList(query);
    }

    /**
     * 根据新增业务对象插入租户应用管理
     *
     * @param bo 租户应用管理新增业务对象
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(SysTenantAppBo bo) {
        checkRepeat(bo);
        SysTenantApp add = MapstructUtils.convert(bo, SysTenantApp.class);
        boolean save = save(add);
        SysTenantApp tenantApp = getById(add.getAppid());
        // 添加新key到缓存中
        RedisUtils.putMapValue(GlobalConstants.TENANT_APP_KEY, tenantApp.getAppKey(), tenantApp.getTenantId());
        return save;
    }

    /**
     * 根据编辑业务对象修改租户应用管理
     *
     * @param bo 租户应用管理编辑业务对象
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(SysTenantAppBo bo) {
        checkRepeat(bo);
        SysTenantApp tenantApp = getById(bo.getAppid());
        SysTenantApp update = MapstructUtils.convert(bo, SysTenantApp.class);
        boolean b = updateById(update);
        // 删除原来的key
        RedisUtils.removeMapKey(GlobalConstants.TENANT_APP_KEY, tenantApp.getAppKey());
        // 添加新的key到缓存中
        RedisUtils.putMapValue(GlobalConstants.TENANT_APP_KEY, bo.getAppKey(), tenantApp.getTenantId());
        return b;
    }

    /**
     * 校验并批量删除租户应用管理信息
     *
     * @param ids 主键集合
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids) {
        List<SysTenantApp> apps = listByIds(ids);
        boolean remove = removeByIds(ids);
        // 删除缓存key
        List<String> keys = StreamUtils.toList(apps, SysTenantApp::getAppKey);
        RedisUtils.removeMapKeys(GlobalConstants.TENANT_APP_KEY, keys.toArray(String[]::new));
        return remove;
    }

    /**
     * 全局是否存在重复的key
     *
     * @param bo 业务对象
     */
    private void checkRepeat(SysTenantAppBo bo) {
        TenantHelper.ignore(() -> {
            boolean exists = lambdaQuery()
                .ne(bo.getAppid() != null, SysTenantApp::getAppid, bo.getAppid())
                .eq(SysTenantApp::getAppKey, bo.getAppKey())
                .exists();
            if (exists) {
                throw new ServiceException("AppKey已被占用");
            }
        });
    }

    /**
     * 查询appKey对应的租户id
     *
     * @param appKey 应用key
     * @return
     */
    @Override
    public String getTenantIdByAppKey(String appKey) {
        if (RedisUtils.hasKey(GlobalConstants.TENANT_APP_KEY)) {
            return RedisUtils.getMap(GlobalConstants.TENANT_APP_KEY, appKey);
        }
        // 如果缓存不存在
        List<SysTenantApp> apps = TenantHelper.ignore(() -> list());
        if (CollUtil.isNotEmpty(apps)) {
            Map<String, String> map = StreamUtils.toMap(apps, SysTenantApp::getAppKey, SysTenantApp::getTenantId);
            RedisUtils.setMap(GlobalConstants.TENANT_APP_KEY, map);
            return map.get(appKey);
        }
        return null;
    }
}
