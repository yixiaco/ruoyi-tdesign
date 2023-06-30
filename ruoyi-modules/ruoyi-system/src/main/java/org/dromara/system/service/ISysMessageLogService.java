package org.dromara.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.SysMessageLog;
import org.dromara.system.domain.query.SysMessageLogQuery;
import org.dromara.system.domain.vo.SysMessageLogVo;

import java.util.Collection;
import java.util.List;

/**
 * 消息发送记录Service接口
 *
 * @author hexm
 * @date 2023-06-29
 */
public interface ISysMessageLogService extends IService<SysMessageLog> {

    /**
     * 查询消息发送记录
     *
     * @param messageLogId 主键
     * @return SysMessageLogVo
     */
    SysMessageLogVo queryById(Long messageLogId);

    /**
     * 查询消息发送记录列表
     *
     * @param query 查询对象
     * @return SysMessageLogVo
     */
    TableDataInfo<SysMessageLogVo> queryPageList(SysMessageLogQuery query);

    /**
     * 查询消息发送记录列表
     *
     * @param query 查询对象
     * @return SysMessageLogVo
     */
    List<SysMessageLogVo> queryList(SysMessageLogQuery query);

    /**
     * 新增消息发送记录
     *
     * @param sysMessageLog 消息发送记录新增业务对象
     * @return Boolean
     */
    Boolean insertByBo(SysMessageLog sysMessageLog);

    /**
     * 校验并批量删除消息发送记录信息
     *
     * @param ids 主键集合
     * @return Boolean
     */
    Boolean deleteWithValidByIds(Collection<Long> ids);

    /**
     * 清空消息记录
     */
    Boolean clear();
}
