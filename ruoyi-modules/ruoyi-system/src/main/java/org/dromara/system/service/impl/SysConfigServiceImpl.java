package org.dromara.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.dromara.common.core.constant.CacheNames;
import org.dromara.common.core.constant.GlobalConstants;
import org.dromara.common.core.constant.UserConstants;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.service.ConfigService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.redis.utils.CacheUtils;
import org.dromara.common.tenant.annotation.IgnoreTenant;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.system.domain.SysConfig;
import org.dromara.system.domain.bo.SysConfigBo;
import org.dromara.system.domain.vo.SysConfigVo;
import org.dromara.system.mapper.SysConfigMapper;
import org.dromara.system.service.ISysConfigService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 参数配置 服务层实现
 *
 * @author Lion Li
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService, ConfigService {

    @Override
    public TableDataInfo<SysConfigVo> selectPageConfigList(SysConfigBo config) {
        return PageQuery.of(() -> baseMapper.queryList(config));
    }

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    @DS("master")
    public SysConfigVo selectConfigById(Long configId) {
        return baseMapper.selectVoById(configId);
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Cacheable(cacheNames = CacheNames.SYS_CONFIG, key = "#configKey")
    @Override
    public String selectConfigByKey(String configKey) {
        Optional<SysConfig> oneOpt = lambdaQuery()
            .eq(SysConfig::getConfigKey, configKey)
            .select(SysConfig::getConfigId, SysConfig::getConfigValue)
            .oneOpt();
        if (oneOpt.isPresent()) {
            return oneOpt.get().getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取注册开关
     *
     * @param tenantId 租户id
     * @return true开启，false关闭
     */
    @Override
    public boolean selectRegisterEnabled(String tenantId) {
        SysConfig retConfig = baseMapper.selectOne(new LambdaQueryWrapper<SysConfig>()
            .eq(SysConfig::getConfigKey, "sys.account.registerUser")
            .eq(TenantHelper.isEnable(), SysConfig::getTenantId, tenantId));
        if (ObjectUtil.isNull(retConfig)) {
            return false;
        }
        return Convert.toBool(retConfig.getConfigValue());
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<SysConfigVo> selectConfigList(SysConfigBo config) {
        return baseMapper.queryList(config);
    }

    /**
     * 新增参数配置
     *
     * @param bo 参数配置信息
     * @return 结果
     */
    @CachePut(cacheNames = CacheNames.SYS_CONFIG, key = "#bo.configKey")
    @Override
    public String insertConfig(SysConfigBo bo) {
        SysConfig config = MapstructUtils.convert(bo, SysConfig.class);
        int row = baseMapper.insert(config);
        if (row > 0) {
            return config.getConfigValue();
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 修改参数配置
     *
     * @param bo 参数配置信息
     * @return 结果
     */
    @CachePut(cacheNames = CacheNames.SYS_CONFIG, key = "#bo.configKey")
    @Override
    public String updateConfigs(SysConfigBo bo) {
        int row = 0;
        SysConfig config = MapstructUtils.convert(bo, SysConfig.class);
        if (config.getConfigId() != null) {
            SysConfig temp = baseMapper.selectById(config.getConfigId());
            if (!StringUtils.equals(temp.getConfigKey(), config.getConfigKey())) {
                CacheUtils.evict(CacheNames.SYS_CONFIG, temp.getConfigKey());
            }
            row = baseMapper.updateById(config);
        } else {
            row = baseMapper.update(config, new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getConfigKey, config.getConfigKey()));
        }
        if (row > 0) {
            return config.getConfigValue();
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteConfigByIds(Long[] configIds) {
        for (Long configId : configIds) {
            SysConfig config = baseMapper.selectById(configId);
            if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
                throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            CacheUtils.evict(CacheNames.SYS_CONFIG, config.getConfigKey());
        }
        baseMapper.deleteBatchIds(Arrays.asList(configIds));
    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetConfigCache() {
        CacheUtils.clear(CacheNames.SYS_CONFIG);
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public boolean checkConfigKeyUnique(SysConfigBo config) {
        long configId = ObjectUtil.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        SysConfig info = baseMapper.selectOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, config.getConfigKey()));
        if (ObjectUtil.isNotNull(info) && !Objects.equals(info.getConfigId(), configId)) {
            return false;
        }
        return true;
    }

    /**
     * 查询多个参数
     *
     * @param keys
     * @return
     */
    @Override
    public Map<String, SysConfigVo> queryConfigs(List<String> keys) {
        List<SysConfig> list = lambdaQuery().in(SysConfig::getConfigKey, keys).list();
        return StreamUtils.toMap(list, SysConfig::getConfigKey, sysConfig -> MapstructUtils.convert(sysConfig, SysConfigVo.class));
    }

    /**
     * 更新配置
     *
     * @param configs
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateConfigs(List<SysConfigBo> configs) {
        List<SysConfig> sysConfigs = MapstructUtils.convert(configs, SysConfig.class);
        // 全局配置
        List<SysConfig> globalConfigs = StreamUtils.filter(sysConfigs, sysConfig -> Objects.equals(1, sysConfig.getIsGlobal()));
        // 租户配置
        List<SysConfig> tenantConfigs = StreamUtils.filter(sysConfigs, sysConfig -> Objects.equals(0, sysConfig.getIsGlobal()));

        // 更新租户配置
        saveOrUpdateBatch(tenantConfigs);
        // 更新缓存
        for (SysConfig sysConfig : tenantConfigs) {
            CacheUtils.put(CacheNames.SYS_CONFIG, sysConfig.getConfigKey(), sysConfig.getConfigValue());
        }

        // 更新全局配置
        TenantHelper.ignore(() -> {
            saveOrUpdateBatch(globalConfigs);
            // 更新缓存
            for (SysConfig sysConfig : globalConfigs) {
                CacheUtils.put(GlobalConstants.GLOBAL_REDIS_KEY + CacheNames.SYS_CONFIG, sysConfig.getConfigKey(), sysConfig.getConfigValue());
            }
        });
    }

    /**
     * 根据参数 key 获取参数值
     *
     * @param configKey 参数 key
     * @return 参数值
     */
    @Override
    @Cacheable(cacheNames = CacheNames.SYS_CONFIG, key = "#configKey")
    public String getConfigValue(String configKey) {
        return selectConfigByKey(configKey);
    }

    /**
     * 根据参数 key 获取全局参数值
     *
     * @param configKey 参数 key
     * @return 参数值
     */
    @Override
    @IgnoreTenant
    @Cacheable(cacheNames = GlobalConstants.GLOBAL_REDIS_KEY + CacheNames.SYS_CONFIG, key = "#configKey")
    public String getGlobalConfigValue(String configKey) {
        Optional<SysConfig> oneOpt = lambdaQuery()
            .eq(SysConfig::getConfigKey, configKey)
            .select(SysConfig::getConfigId, SysConfig::getConfigValue)
            .eq(SysConfig::getIsGlobal, 1)
            .oneOpt();
        if (oneOpt.isPresent()) {
            return oneOpt.get().getConfigValue();
        }
        return StringUtils.EMPTY;
    }
}
