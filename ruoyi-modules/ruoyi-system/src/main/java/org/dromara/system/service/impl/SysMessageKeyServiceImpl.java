package org.dromara.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.SortQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.SysMessageKey;
import org.dromara.system.domain.SysMessageTemplate;
import org.dromara.system.domain.bo.SysMessageKeyBo;
import org.dromara.system.domain.query.SysMessageKeyQuery;
import org.dromara.system.domain.vo.SysMessageKeyVo;
import org.dromara.system.mapper.SysMessageKeyMapper;
import org.dromara.system.service.ISysMessageKeyService;
import org.dromara.system.service.ISysMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 消息常量Service业务层处理
 *
 * @author hexm
 * @date 2023-06-28
 */
@Service
public class SysMessageKeyServiceImpl extends ServiceImpl<SysMessageKeyMapper, SysMessageKey> implements ISysMessageKeyService {

    @Autowired
    private ISysMessageTemplateService messageTemplateService;

    /**
     * 查询消息常量
     *
     * @param messageKeyId 主键
     * @return SysMessageKeyVo
     */
    @Override
    public SysMessageKeyVo queryById(Long messageKeyId) {
        return baseMapper.selectVoById(messageKeyId);
    }

    /**
     * 查询消息常量列表
     *
     * @param query 查询对象
     * @return SysMessageKeyVo
     */
    @Override
    public TableDataInfo<SysMessageKeyVo> queryPageList(SysMessageKeyQuery query) {
        return PageQuery.of(() -> baseMapper.queryList(query));
    }

    /**
     * 查询消息常量列表
     *
     * @param query 查询对象
     * @return SysMessageKeyVo
     */
    @Override
    public List<SysMessageKeyVo> queryList(SysMessageKeyQuery query) {
        return SortQuery.of(() -> baseMapper.queryList(query));
    }

    /**
     * 根据新增业务对象插入消息常量
     *
     * @param bo 消息常量新增业务对象
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(SysMessageKeyBo bo) {
        checkRepeat(bo);
        SysMessageKey add = MapstructUtils.convert(bo, SysMessageKey.class);
        return save(add);
    }

    /**
     * 根据编辑业务对象修改消息常量
     *
     * @param bo 消息常量编辑业务对象
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(SysMessageKeyBo bo) {
        checkRepeat(bo);
        String messageKey = getKeyById(bo.getMessageKeyId());
        if (!Objects.equals(messageKey, bo.getMessageKey())) {
            messageTemplateService.updateMessageKey(bo.getMessageKeyId(), bo.getMessageKey());
        }
        SysMessageKey update = MapstructUtils.convert(bo, SysMessageKey.class);
        return updateById(update);
    }

    /**
     * 校验并批量删除消息常量信息
     *
     * @param ids 主键集合
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids) {
        List<SysMessageKey> list = lambdaQuery().in(SysMessageKey::getMessageKeyId, ids).select(SysMessageKey::getMessageKey).list();
        List<String> keys = StreamUtils.toList(list, SysMessageKey::getMessageKey);
        boolean exists = messageTemplateService.lambdaQuery().in(SysMessageTemplate::getMessageKey, keys).exists();
        if (exists) {
            throw new ServiceException("存在关联的消息模板，无法删除");
        }
        return removeByIds(ids);
    }

    /**
     * 检查重复
     *
     * @param bo 业务对象
     */
    private void checkRepeat(SysMessageKeyBo bo) {
        boolean exists = lambdaQuery()
            .ne(bo.getMessageKeyId() != null, SysMessageKey::getMessageKeyId, bo.getMessageKeyId())
            .eq(SysMessageKey::getMessageKey, bo.getMessageKey())
            .exists();
        if (exists) {
            throw new ServiceException("已存在相同的KEY");
        }
    }

    /**
     * 查询消息key
     *
     * @param messageKeyId 消息常量id
     * @return 消息key
     */
    @Override
    public String getKeyById(Long messageKeyId) {
        return lambdaQuery()
            .eq(SysMessageKey::getMessageKeyId, messageKeyId)
            .select(SysMessageKey::getMessageKey)
            .oneOpt().map(SysMessageKey::getMessageKey).orElse(null);
    }
}
