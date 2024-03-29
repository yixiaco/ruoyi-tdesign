package org.dromara.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.dromara.common.core.constant.GlobalConstants;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.service.AppService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.system.domain.SysApp;
import org.dromara.system.domain.bo.SysAppBo;
import org.dromara.system.domain.query.SysAppQuery;
import org.dromara.system.domain.vo.SysAppVo;
import org.dromara.system.mapper.SysAppMapper;
import org.dromara.system.service.ISysAppService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 应用管理Service业务层处理
 *
 * @author yixiacoco
 * @date 2023-05-17
 */
@Service
public class SysAppServiceImpl extends ServiceImpl<SysAppMapper, SysApp> implements ISysAppService, AppService {

    /**
     * 查询应用管理
     *
     * @param appid 主键
     * @return SysAppVo
     */
    @Override
    public SysAppVo queryById(Long appid) {
        return mapper.selectOneWithRelationsByIdAs(appid, SysAppVo.class);
    }

    /**
     * 查询应用管理列表
     *
     * @param query 查询对象
     * @return SysAppVo
     */
    @Override
    public TableDataInfo<SysAppVo> queryPageList(SysAppQuery query) {
        return PageQuery.of(() -> mapper.queryList(query));
    }

    /**
     * 查询应用管理列表
     *
     * @param query 查询对象
     * @return SysAppVo
     */
    @Override
    public List<SysAppVo> queryList(SysAppQuery query) {
        return mapper.queryList(query);
    }

    /**
     * 根据新增业务对象插入应用管理
     *
     * @param bo 应用管理新增业务对象
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(SysAppBo bo) {
        checkRepeat(bo);
        SysApp add = MapstructUtils.convert(bo, SysApp.class);
        boolean save = save(add);
        SysApp sysApp = getById(add.getAppid());
        // 添加新key到缓存中
        RedisUtils.putMapValue(GlobalConstants.APP_KEY, sysApp.getAppKey(), sysApp.getTenantId());
        return save;
    }

    /**
     * 根据编辑业务对象修改应用管理
     *
     * @param bo 应用管理编辑业务对象
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(SysAppBo bo) {
        checkRepeat(bo);
        SysApp sysApp = getById(bo.getAppid());
        SysApp update = MapstructUtils.convert(bo, SysApp.class);
        boolean b = updateById(update);
        // 删除原来的key
        RedisUtils.removeMapKey(GlobalConstants.APP_KEY, sysApp.getAppKey());
        // 添加新的key到缓存中
        RedisUtils.putMapValue(GlobalConstants.APP_KEY, bo.getAppKey(), sysApp.getTenantId());
        return b;
    }

    /**
     * 校验并批量删除应用管理信息
     *
     * @param ids 主键集合
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids) {
        List<SysApp> apps = listByIds(ids);
        boolean remove = removeByIds(ids);
        // 删除缓存key
        List<String> keys = StreamUtils.toList(apps, SysApp::getAppKey);
        RedisUtils.removeMapKeys(GlobalConstants.APP_KEY, keys.stream().toArray(String[]::new));
        return remove;
    }

    /**
     * 全局是否存在重复的key
     *
     * @param bo 业务对象
     */
    private void checkRepeat(SysAppBo bo) {
        TenantHelper.ignore(() -> {
            boolean exists = queryChain()
                .ne(SysApp::getAppid, bo.getAppid(), bo.getAppid() != null)
                .eq(SysApp::getAppKey, bo.getAppKey())
                .exists();
            if (exists) {
                throw new ServiceException("AppKey已被占用");
            }
        });
    }

    /**
     * 查询appKey对应的租户id
     *
     * @param appKey
     * @return
     */
    @Override
    public String getTenantIdByAppKey(String appKey) {
        if (RedisUtils.hasKey(GlobalConstants.APP_KEY)) {
            return RedisUtils.getMap(GlobalConstants.APP_KEY, appKey);
        }
        // 如果缓存不存在
        List<SysApp> apps = TenantHelper.ignore(() -> list());
        if (CollUtil.isNotEmpty(apps)) {
            Map<String, String> map = StreamUtils.toMap(apps, SysApp::getAppKey, SysApp::getTenantId);
            RedisUtils.setMap(GlobalConstants.APP_KEY, map);
            return map.get(appKey);
        }
        return null;
    }
}
