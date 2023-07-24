package org.dromara.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.SysMessageConfig;
import org.dromara.system.domain.bo.SysMessageConfigBo;
import org.dromara.system.domain.query.SysMessageConfigQuery;
import org.dromara.system.domain.vo.SysMessageConfigVo;

import java.util.Collection;
import java.util.List;

/**
 * 消息配置Service接口
 *
 * @author hexm
 * @date 2023-06-27
 */
public interface ISysMessageConfigService extends IService<SysMessageConfig> {

    /**
     * 查询消息配置
     *
     * @param messageConfigId 主键
     * @return SysMessageConfigVo
     */
    SysMessageConfigVo queryById(Long messageConfigId);

    /**
     * 查询消息配置列表
     *
     * @param query 查询对象
     * @return SysMessageConfigVo
     */
    TableDataInfo<SysMessageConfigVo> queryPageList(SysMessageConfigQuery query);

    /**
     * 查询消息配置列表
     *
     * @param query 查询对象
     * @return SysMessageConfigVo
     */
    List<SysMessageConfigVo> queryList(SysMessageConfigQuery query);

    /**
     * 新增消息配置
     *
     * @param bo 消息配置新增业务对象
     * @return Boolean
     */
    Boolean insertByBo(SysMessageConfigBo bo);

    /**
     * 修改消息配置
     *
     * @param bo 消息配置编辑业务对象
     * @return Boolean
     */
    Boolean updateByBo(SysMessageConfigBo bo);

    /**
     * 校验并批量删除消息配置信息
     *
     * @param ids 主键集合
     * @return Boolean
     */
    Boolean deleteWithValidByIds(Collection<Long> ids);

    /**
     * 获取消息配置缓存
     *
     * @param messageConfigId 消息配置id
     */
    SysMessageConfig getCacheById(Long messageConfigId);

    /**
     * 删除缓存
     */
    void removeCache();
}
