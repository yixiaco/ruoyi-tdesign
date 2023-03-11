package com.ruoyi.system.service.impl;

import cn.dev33.satoken.context.SaHolder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.CacheNames;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.domain.SysDictType;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.service.DictService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StreamUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.redis.CacheUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.ruoyi.system.mapper.SysDictTypeMapper;
import com.ruoyi.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典 业务层处理
 *
 * @author Lion Li
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService, DictService {

    @Autowired
    private SysDictDataMapper dictDataMapper;

    @Override
    public TableDataInfo<SysDictType> selectPageDictTypeList(SysDictType dictType) {
        return PageQuery.of(() -> baseMapper.queryList(dictType));
    }

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictType> selectDictTypeList(SysDictType dictType) {
        return baseMapper.queryList(dictType);
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictType> selectDictTypeAll() {
        return baseMapper.selectList();
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Cacheable(cacheNames = CacheNames.SYS_DICT, key = "#dictType")
    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(dictType);
        if (CollUtil.isNotEmpty(dictDatas)) {
            return dictDatas;
        }
        return null;
    }

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    @Override
    public SysDictType selectDictTypeById(Long dictId) {
        return baseMapper.selectById(dictId);
    }

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    @Cacheable(cacheNames = CacheNames.SYS_DICT, key = "#dictType")
    @Override
    public SysDictType selectDictTypeByType(String dictType) {
        return baseMapper.selectById(new LambdaQueryWrapper<SysDictType>().eq(SysDictType::getDictType, dictType));
    }

    /**
     * 批量删除字典类型信息
     *
     * @param dictIds 需要删除的字典ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictTypeByIds(Long[] dictIds) {
        for (Long dictId : dictIds) {
            SysDictType dictType = selectDictTypeById(dictId);
            if (dictDataMapper.exists(new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getDictType, dictType.getDictType()))) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
            CacheUtils.evict(CacheNames.SYS_DICT, dictType.getDictType());
        }
        baseMapper.deleteBatchIds(Arrays.asList(dictIds));
    }

    /**
     * 加载字典缓存数据
     */
    @Override
    public void loadingDictCache() {
        List<SysDictData> dictDataList = dictDataMapper.selectList(
            new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getStatus, UserConstants.DICT_NORMAL));
        Map<String, List<SysDictData>> dictDataMap = StreamUtils.groupByKey(dictDataList, SysDictData::getDictType);
        dictDataMap.forEach((k,v) -> {
            List<SysDictData> dictList = StreamUtils.sorted(v, Comparator.comparing(SysDictData::getDictSort));
            CacheUtils.put(CacheNames.SYS_DICT, k, dictList);
        });
    }

    /**
     * 清空字典缓存数据
     */
    @Override
    public void clearDictCache() {
        CacheUtils.clear(CacheNames.SYS_DICT);
    }

    /**
     * 重置字典缓存数据
     */
    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    /**
     * 新增保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @CachePut(cacheNames = CacheNames.SYS_DICT, key = "#dict.dictType")
    @Override
    public List<SysDictData> insertDictType(SysDictType dict) {
        int row = baseMapper.insert(dict);
        if (row > 0) {
            return new ArrayList<>();
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @CachePut(cacheNames = CacheNames.SYS_DICT, key = "#dict.dictType")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysDictData> updateDictType(SysDictType dict) {
        SysDictType oldDict = baseMapper.selectById(dict.getDictId());
        dictDataMapper.update(null, new LambdaUpdateWrapper<SysDictData>()
            .set(SysDictData::getDictType, dict.getDictType())
            .eq(SysDictData::getDictType, oldDict.getDictType()));
        int row = baseMapper.updateById(dict);
        if (row > 0) {
            CacheUtils.evict(CacheNames.SYS_DICT, oldDict.getDictType());
            return dictDataMapper.selectDictDataByType(dict.getDictType());
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dict 字典类型
     * @return 结果
     */
    @Override
    public String checkDictTypeUnique(SysDictType dict) {
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<SysDictType>()
            .eq(SysDictType::getDictType, dict.getDictType())
            .ne(ObjectUtil.isNotNull(dict.getDictId()), SysDictType::getDictId, dict.getDictId()));
        if (exist) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @param separator 分隔符
     * @return 字典标签
     */
    @SuppressWarnings("unchecked cast")
    @Override
    public String getDictLabel(String dictType, String dictValue, String separator) {
        // 优先从本地缓存获取
        List<SysDictData> datas = (List<SysDictData>) SaHolder.getStorage().get(CacheConstants.SYS_DICT_KEY + dictType);
        if (ObjectUtil.isNull(datas)) {
            datas = SpringUtils.getAopProxy(this).selectDictDataByType(dictType);
            SaHolder.getStorage().set(CacheConstants.SYS_DICT_KEY + dictType, datas);
        }

        Map<String, String> map = StreamUtils.toMap(datas, SysDictData::getDictValue, SysDictData::getDictLabel);
        if (StringUtils.containsAny(dictValue, separator)) {
            return Arrays.stream(dictValue.split(separator))
                .map(v -> map.getOrDefault(v, StringUtils.EMPTY))
                .collect(Collectors.joining(separator));
        } else {
            return map.getOrDefault(dictValue, StringUtils.EMPTY);
        }
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @param separator 分隔符
     * @return 字典值
     */
    @SuppressWarnings("unchecked cast")
    @Override
    public String getDictValue(String dictType, String dictLabel, String separator) {
        // 优先从本地缓存获取
        List<SysDictData> datas = (List<SysDictData>) SaHolder.getStorage().get(CacheConstants.SYS_DICT_KEY + dictType);
        if (ObjectUtil.isNull(datas)) {
            datas = SpringUtils.getAopProxy(this).selectDictDataByType(dictType);
            SaHolder.getStorage().set(CacheConstants.SYS_DICT_KEY + dictType, datas);
        }

        Map<String, String> map = StreamUtils.toMap(datas, SysDictData::getDictLabel, SysDictData::getDictValue);
        if (StringUtils.containsAny(dictLabel, separator)) {
            return Arrays.stream(dictLabel.split(separator))
                .map(l -> map.getOrDefault(l, StringUtils.EMPTY))
                .collect(Collectors.joining(separator));
        } else {
            return map.getOrDefault(dictLabel, StringUtils.EMPTY);
        }
    }

}
