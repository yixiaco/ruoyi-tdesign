package org.dromara.system.service;

import com.mybatisflex.core.service.IService;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.SysApp;
import org.dromara.system.domain.bo.SysAppBo;
import org.dromara.system.domain.query.SysAppQuery;
import org.dromara.system.domain.vo.SysAppVo;

import java.util.Collection;
import java.util.List;

/**
 * 应用管理Service接口
 *
 * @author yixiacoco
 * @date 2023-05-17
 */
public interface ISysAppService extends IService<SysApp> {

    /**
     * 查询应用管理
     *
     * @param appid 主键
     * @return SysAppVo
     */
    SysAppVo queryById(Long appid);

    /**
     * 查询应用管理列表
     *
     * @param query 查询对象
     * @return SysAppVo
     */
    TableDataInfo<SysAppVo> queryPageList(SysAppQuery query);

    /**
     * 查询应用管理列表
     *
     * @param query 查询对象
     * @return SysAppVo
     */
    List<SysAppVo> queryList(SysAppQuery query);

    /**
     * 新增应用管理
     *
     * @param bo 应用管理新增业务对象
     * @return Boolean
     */
    Boolean insertByBo(SysAppBo bo);

    /**
     * 修改应用管理
     *
     * @param bo 应用管理编辑业务对象
     * @return Boolean
     */
    Boolean updateByBo(SysAppBo bo);

    /**
     * 校验并批量删除应用管理信息
     *
     * @param ids 主键集合
     * @return Boolean
     */
    Boolean deleteWithValidByIds(Collection<Long> ids);
}
