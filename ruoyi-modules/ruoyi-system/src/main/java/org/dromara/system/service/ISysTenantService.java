package org.dromara.system.service;

import com.mybatisflex.core.service.IService;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.SysTenant;
import org.dromara.system.domain.bo.SysTenantBo;
import org.dromara.system.domain.query.SysTenantQuery;
import org.dromara.system.domain.vo.SysTenantVo;

import java.util.Collection;
import java.util.List;

/**
 * 租户Service接口
 *
 * @author Michelle.Chung
 */
public interface ISysTenantService extends IService<SysTenant> {

    /**
     * 查询租户
     */
    SysTenantVo queryById(Long id);

    /**
     * 基于租户ID查询租户
     */
    SysTenantVo queryByTenantId(String tenantId);

    /**
     * 查询租户列表
     */
    TableDataInfo<SysTenantVo> queryPageList(SysTenantQuery query);

    /**
     * 查询租户列表
     */
    List<SysTenantVo> queryList(SysTenantQuery query);

    /**
     * 新增租户
     */
    Boolean insertByBo(SysTenantBo bo);

    /**
     * 修改租户
     */
    Boolean updateByBo(SysTenantBo bo);

    /**
     * 修改租户状态
     */
    int updateTenantStatus(SysTenantBo bo);

    /**
     * 校验租户是否允许操作
     */
    void checkTenantAllowed(String tenantId);

    /**
     * 校验并批量删除租户信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 校验企业名称是否唯一
     */
    boolean checkCompanyNameUnique(SysTenantBo bo);

    /**
     * 校验账号余额
     */
    boolean checkAccountBalance(String tenantId);

    /**
     * 校验有效期
     */
    boolean checkExpireTime(String tenantId);

    /**
     * 同步租户套餐
     */
    Boolean syncTenantPackage(String tenantId, Long packageId);
}
