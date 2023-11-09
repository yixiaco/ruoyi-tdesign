package org.dromara.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.common.core.enums.UserType;
import org.dromara.system.domain.SysOssCategory;
import org.dromara.system.domain.bo.SysOssCategoryBo;
import org.dromara.system.domain.query.SysOssCategoryQuery;
import org.dromara.system.domain.vo.SysOssCategoryVo;

import java.util.Collection;
import java.util.List;

/**
 * OSS分类Service接口
 *
 * @author hexm
 * @date 2023-08-14
 */
public interface ISysOssCategoryService extends IService<SysOssCategory> {

    /**
     * 查询OSS分类
     *
     * @param query 查询对象
     * @return SysOssCategoryVo
     */
    SysOssCategoryVo query(SysOssCategoryQuery query);

    /**
     * 查询OSS分类列表
     *
     * @param query 查询对象
     * @return SysOssCategoryVo
     */
    List<SysOssCategoryVo> queryList(SysOssCategoryQuery query);

    /**
     * 新增OSS分类
     *
     * @param bo OSS分类新增业务对象
     * @return Boolean
     */
    Boolean insertByBo(SysOssCategoryBo bo);

    /**
     * 修改OSS分类
     *
     * @param bo OSS分类编辑业务对象
     * @return Boolean
     */
    Boolean updateByBo(SysOssCategoryBo bo);

    /**
     * 校验并批量删除OSS分类信息
     *
     * @param ids      主键集合
     * @param userType 用户类型
     * @param userId   用户id
     * @return Boolean
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, UserType userType, Long userId);

    /**
     * 是否存在分类id
     *
     * @param ossCategoryId 分类id
     * @param userType      用户类型
     * @param userId        用户id
     * @return
     */
    boolean hasId(Long ossCategoryId, UserType userType, Long userId);
}
