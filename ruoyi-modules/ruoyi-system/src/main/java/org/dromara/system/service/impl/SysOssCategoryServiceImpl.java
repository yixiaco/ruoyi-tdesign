package org.dromara.system.service.impl;

import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.dromara.system.domain.SysOss;
import org.dromara.system.service.ISysOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.dromara.system.domain.SysOssCategory;
import org.dromara.system.domain.bo.SysOssCategoryBo;
import org.dromara.system.domain.query.SysOssCategoryQuery;
import org.dromara.system.domain.vo.SysOssCategoryVo;
import org.dromara.system.mapper.SysOssCategoryMapper;
import org.dromara.system.service.ISysOssCategoryService;

import java.util.Collection;
import java.util.List;

/**
 * OSS分类Service业务层处理
 *
 * @author hexm
 * @date 2023-08-14
 */
@Service
public class SysOssCategoryServiceImpl extends ServiceImpl<SysOssCategoryMapper, SysOssCategory> implements ISysOssCategoryService {

    @Autowired
    private ISysOssService ossService;

    /**
     * 查询OSS分类
     *
     * @param ossCategoryId 主键
     * @return SysOssCategoryVo
     */
    @Override
    public SysOssCategoryVo queryById(Long ossCategoryId) {
        return baseMapper.selectVoById(ossCategoryId);
    }

    /**
     * 查询OSS分类列表
     *
     * @param query 查询对象
     * @return SysOssCategoryVo
     */
    @Override
    public List<SysOssCategoryVo> queryList(SysOssCategoryQuery query) {
        return baseMapper.queryList(query);
    }

    /**
     * 根据新增业务对象插入OSS分类
     *
     * @param bo OSS分类新增业务对象
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(SysOssCategoryBo bo) {
        SysOssCategory add = MapstructUtils.convert(bo, SysOssCategory.class);
        return save(add);
    }

    /**
     * 根据编辑业务对象修改OSS分类
     *
     * @param bo OSS分类编辑业务对象
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(SysOssCategoryBo bo) {
        SysOssCategory update = MapstructUtils.convert(bo, SysOssCategory.class);
        return updateById(update);
    }

    /**
     * 校验并批量删除OSS分类信息
     *
     * @param ids 主键集合
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids) {
        boolean exists = ossService.lambdaQuery().in(SysOss::getOssCategoryId, ids).exists();
        if (exists) {
            throw new ServiceException("无法删除非空分类");
        }
        exists = lambdaQuery().in(SysOssCategory::getParentId, ids).exists();
        if (exists) {
            throw new ServiceException("请先删除子分类");
        }
        return removeByIds(ids);
    }
}
