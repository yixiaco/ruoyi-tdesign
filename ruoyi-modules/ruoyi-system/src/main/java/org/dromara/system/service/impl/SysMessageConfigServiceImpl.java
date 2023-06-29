package org.dromara.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.SysMessageConfig;
import org.dromara.system.domain.SysMessageTemplate;
import org.dromara.system.domain.bo.SysMessageConfigBo;
import org.dromara.system.domain.query.SysMessageConfigQuery;
import org.dromara.system.domain.vo.SysMessageConfigVo;
import org.dromara.system.mapper.SysMessageConfigMapper;
import org.dromara.system.service.ISysMessageConfigService;
import org.dromara.system.service.ISysMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 消息配置Service业务层处理
 *
 * @author hexm
 * @date 2023-06-27
 */
@Service
public class SysMessageConfigServiceImpl extends ServiceImpl<SysMessageConfigMapper, SysMessageConfig> implements ISysMessageConfigService {

    @Autowired
    private ISysMessageTemplateService messageTemplateService;

    /**
     * 查询消息配置
     *
     * @param messageConfigId 主键
     * @return SysMessageConfigVo
     */
    @Override
    public SysMessageConfigVo queryById(Long messageConfigId) {
        return baseMapper.selectVoById(messageConfigId);
    }

    /**
     * 查询消息配置列表
     *
     * @param query 查询对象
     * @return SysMessageConfigVo
     */
    @Override
    public TableDataInfo<SysMessageConfigVo> queryPageList(SysMessageConfigQuery query) {
        return PageQuery.of(() -> baseMapper.queryList(query));
    }

    /**
     * 查询消息配置列表
     *
     * @param query 查询对象
     * @return SysMessageConfigVo
     */
    @Override
    public List<SysMessageConfigVo> queryList(SysMessageConfigQuery query) {
        return baseMapper.queryList(query);
    }

    /**
     * 根据新增业务对象插入消息配置
     *
     * @param bo 消息配置新增业务对象
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(SysMessageConfigBo bo) {
        SysMessageConfig add = MapstructUtils.convert(bo, SysMessageConfig.class);
        return save(add);
    }

    /**
     * 根据编辑业务对象修改消息配置
     *
     * @param bo 消息配置编辑业务对象
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(SysMessageConfigBo bo) {
        SysMessageConfig config = getById(bo.getMessageConfigId());
        if (!Objects.equals(config.getMessageType(), bo.getMessageType())) {
            boolean exists = messageTemplateService.lambdaQuery().eq(SysMessageTemplate::getMessageConfigId, bo.getMessageConfigId()).exists();
            if (exists) {
                throw new ServiceException("存在关联的消息模板，无法切换消息类型");
            }
        }
        SysMessageConfig update = MapstructUtils.convert(bo, SysMessageConfig.class);
        return updateById(update);
    }

    /**
     * 校验并批量删除消息配置信息
     *
     * @param ids 主键集合
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids) {
        boolean exists = messageTemplateService.lambdaQuery().in(SysMessageTemplate::getMessageConfigId, ids).exists();
        if (exists) {
            throw new ServiceException("存在关联的消息模板，取消关联后重试");
        }
        return removeByIds(ids);
    }
}
