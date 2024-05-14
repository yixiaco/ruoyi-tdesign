package org.dromara.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.SysTenantApp;
import org.dromara.system.domain.bo.SysTenantAppBo;
import org.dromara.system.domain.query.SysTenantAppQuery;
import org.dromara.system.domain.vo.SysTenantAppVo;

import java.util.Collection;
import java.util.List;

/**
 * 租户应用管理Service接口
 *
 * @author yixiacoco
 * @date 2023-05-17
 */
public interface ISysTenantAppService extends IService<SysTenantApp> {

    /**
     * 查询租户应用管理
     *
     * @param appid 主键
     * @return SysTenantAppVo
     */
    SysTenantAppVo queryById(Long appid);

    /**
     * 查询租户应用管理列表
     *
     * @param query 查询对象
     * @return SysTenantAppVo
     */
    TableDataInfo<SysTenantAppVo> queryPageList(SysTenantAppQuery query);

    /**
     * 查询租户应用管理列表
     *
     * @param query 查询对象
     * @return SysTenantAppVo
     */
    List<SysTenantAppVo> queryList(SysTenantAppQuery query);

    /**
     * 新增租户应用管理
     *
     * @param bo 租户应用管理新增业务对象
     * @return Boolean
     */
    Boolean insertByBo(SysTenantAppBo bo);

    /**
     * 修改租户应用管理
     *
     * @param bo 租户应用管理编辑业务对象
     * @return Boolean
     */
    Boolean updateByBo(SysTenantAppBo bo);

    /**
     * 校验并批量删除租户应用管理信息
     *
     * @param ids 主键集合
     * @return Boolean
     */
    Boolean deleteWithValidByIds(Collection<Long> ids);
}
