package org.dromara.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.SortQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.SysMessageLog;
import org.dromara.system.domain.query.SysMessageLogQuery;
import org.dromara.system.domain.vo.SysMessageLogVo;
import org.dromara.system.mapper.SysMessageLogMapper;
import org.dromara.system.service.ISysMessageLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 消息发送记录Service业务层处理
 *
 * @author hexm
 * @date 2023-06-29
 */
@Service
public class SysMessageLogServiceImpl extends ServiceImpl<SysMessageLogMapper, SysMessageLog> implements ISysMessageLogService {

    /**
     * 查询消息发送记录
     *
     * @param messageLogId 主键
     * @return SysMessageLogVo
     */
    @Override
    public SysMessageLogVo queryById(Long messageLogId) {
        return baseMapper.selectVoById(messageLogId);
    }

    /**
     * 查询消息发送记录列表
     *
     * @param query 查询对象
     * @return SysMessageLogVo
     */
    @Override
    public TableDataInfo<SysMessageLogVo> queryPageList(SysMessageLogQuery query) {
        return PageQuery.of(() -> baseMapper.queryList(query));
    }

    /**
     * 查询消息发送记录列表
     *
     * @param query 查询对象
     * @return SysMessageLogVo
     */
    @Override
    public List<SysMessageLogVo> queryList(SysMessageLogQuery query) {
        return SortQuery.of(() -> baseMapper.queryList(query));
    }

    /**
     * 根据新增业务对象插入消息发送记录
     *
     * @param sysMessageLog 消息发送记录新增业务对象
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(SysMessageLog sysMessageLog) {
        sysMessageLog.setLogTime(new Date());
        return save(sysMessageLog);
    }

    /**
     * 校验并批量删除消息发送记录信息
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
     * 清空消息记录
     */
    @Override
    public Boolean clear() {
        return remove(lambdaQuery().getWrapper());
    }
}
