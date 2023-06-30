package org.dromara.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.dromara.common.core.enums.NormalDisableEnum;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.SysMessageKey;
import org.dromara.system.domain.SysMessageTemplate;
import org.dromara.system.domain.bo.SysMessageTemplateBo;
import org.dromara.system.domain.query.SysMessageTemplateQuery;
import org.dromara.system.domain.vo.SysMessageTemplateVo;
import org.dromara.system.mapper.SysMessageTemplateMapper;
import org.dromara.system.service.ISysMessageKeyService;
import org.dromara.system.service.ISysMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 消息模板Service业务层处理
 *
 * @author hexm
 * @date 2023-06-28
 */
@Service
public class SysMessageTemplateServiceImpl extends ServiceImpl<SysMessageTemplateMapper, SysMessageTemplate> implements ISysMessageTemplateService {

    @Autowired
    private ISysMessageKeyService messageKeyService;

    /**
     * 查询消息模板
     *
     * @param messageTemplateId 主键
     * @return SysMessageTemplateVo
     */
    @Override
    public SysMessageTemplateVo queryById(Long messageTemplateId) {
        return baseMapper.selectVoById(messageTemplateId);
    }

    /**
     * 查询消息模板列表
     *
     * @param query 查询对象
     * @return SysMessageTemplateVo
     */
    @Override
    public TableDataInfo<SysMessageTemplateVo> queryPageList(SysMessageTemplateQuery query) {
        return PageQuery.of(() -> baseMapper.queryList(query));
    }

    /**
     * 查询消息模板列表
     *
     * @param query 查询对象
     * @return SysMessageTemplateVo
     */
    @Override
    public List<SysMessageTemplateVo> queryList(SysMessageTemplateQuery query) {
        return baseMapper.queryList(query);
    }

    /**
     * 根据新增业务对象插入消息模板
     *
     * @param bo 消息模板新增业务对象
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(SysMessageTemplateBo bo) {
        checkRepeat(bo);
        String messageKey = messageKeyService.getKeyById(bo.getMessageKeyId());
        if (messageKey == null) {
            throw new ServiceException("消息常量不存在");
        }
        SysMessageTemplate add = MapstructUtils.convert(bo, SysMessageTemplate.class);
        add.setMessageKey(messageKey);
        return save(add);
    }

    /**
     * 根据编辑业务对象修改消息模板
     *
     * @param bo 消息模板编辑业务对象
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(SysMessageTemplateBo bo) {
        checkRepeat(bo);
        String messageKey = messageKeyService.getKeyById(bo.getMessageKeyId());
        if (messageKey == null) {
            throw new ServiceException("消息常量不存在");
        }
        SysMessageTemplate update = MapstructUtils.convert(bo, SysMessageTemplate.class);
        update.setMessageKey(messageKey);
        return updateById(update);
    }

    /**
     * 校验并批量删除消息模板信息
     *
     * @param ids 主键集合
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids) {
        return removeByIds(ids);
    }

    /**
     * 检查重复，检查消息类型、状态、消息key
     *
     * @param bo 业务对象
     */
    private void checkRepeat(SysMessageTemplateBo bo) {
        boolean exists = lambdaQuery()
            .ne(bo.getMessageTemplateId() != null, SysMessageTemplate::getMessageTemplateId, bo.getMessageTemplateId())
            .eq(SysMessageTemplate::getMessageKeyId, bo.getMessageKeyId())
            .eq(SysMessageTemplate::getMessageType, bo.getMessageType())
            .eq(SysMessageTemplate::getStatus, NormalDisableEnum.NORMAL.getCode())
            .exists();
        if (exists) {
            throw new ServiceException("该消息类型下存在相同的消息Key");
        }
    }

    /**
     * 更新消息key
     *
     * @param messageKeyId 消息key主键
     * @param messageKey   消息key
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMessageKey(Long messageKeyId, String messageKey) {
        lambdaUpdate()
            .eq(SysMessageTemplate::getMessageKeyId, messageKeyId)
            .set(SysMessageTemplate::getMessageKey, messageKey)
            .update();
    }
}
