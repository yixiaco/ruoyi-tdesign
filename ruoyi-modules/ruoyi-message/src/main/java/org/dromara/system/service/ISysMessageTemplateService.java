package org.dromara.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.SysMessageTemplate;
import org.dromara.system.domain.bo.SysMessageTemplateBo;
import org.dromara.system.domain.query.SysMessageTemplateQuery;
import org.dromara.system.domain.vo.SysMessageTemplateVo;

import java.util.Collection;
import java.util.List;

/**
 * 消息模板Service接口
 *
 * @author hexm
 * @date 2023-06-28
 */
public interface ISysMessageTemplateService extends IService<SysMessageTemplate> {

    /**
     * 查询消息模板
     *
     * @param messageTemplateId 主键
     * @return SysMessageTemplateVo
     */
    SysMessageTemplateVo queryById(Long messageTemplateId);

    /**
     * 查询消息模板列表
     *
     * @param query 查询对象
     * @return SysMessageTemplateVo
     */
    TableDataInfo<SysMessageTemplateVo> queryPageList(SysMessageTemplateQuery query);

    /**
     * 查询消息模板列表
     *
     * @param query 查询对象
     * @return SysMessageTemplateVo
     */
    List<SysMessageTemplateVo> queryList(SysMessageTemplateQuery query);

    /**
     * 新增消息模板
     *
     * @param bo 消息模板新增业务对象
     * @return Boolean
     */
    Boolean insertByBo(SysMessageTemplateBo bo);

    /**
     * 修改消息模板
     *
     * @param bo 消息模板编辑业务对象
     * @return Boolean
     */
    Boolean updateByBo(SysMessageTemplateBo bo);

    /**
     * 校验并批量删除消息模板信息
     *
     * @param ids 主键集合
     * @return Boolean
     */
    Boolean deleteWithValidByIds(Collection<Long> ids);

    /**
     * 更新消息key
     *
     * @param messageKeyId 消息key主键
     * @param messageKey   消息key
     */
    void updateMessageKey(Long messageKeyId, String messageKey);

    /**
     * 从缓存中获取消息模板
     *
     * @param messageType 消息类型
     * @param messageKey  消息key
     * @return
     */
    SysMessageTemplate getCache(String messageType, String messageKey);

    /**
     * 从缓存中获取消息模板
     *
     * @param messageTemplateId 消息模板id
     * @return
     */
    SysMessageTemplate getCacheById(Long messageTemplateId);

    /**
     * 删除缓存
     */
    void removeCache();
}
