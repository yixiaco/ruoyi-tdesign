package org.dromara.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.SysMessageKey;
import org.dromara.system.domain.bo.SysMessageKeyBo;
import org.dromara.system.domain.query.SysMessageKeyQuery;
import org.dromara.system.domain.vo.SysMessageKeyVo;

import java.util.Collection;
import java.util.List;

/**
 * 消息常量Service接口
 *
 * @author hexm
 * @date 2023-06-28
 */
public interface ISysMessageKeyService extends IService<SysMessageKey> {

    /**
     * 查询消息常量
     *
     * @param messageKeyId 主键
     * @return SysMessageKeyVo
     */
    SysMessageKeyVo queryById(Long messageKeyId);

    /**
     * 查询消息常量列表
     *
     * @param query 查询对象
     * @return SysMessageKeyVo
     */
    TableDataInfo<SysMessageKeyVo> queryPageList(SysMessageKeyQuery query);

    /**
     * 查询消息常量列表
     *
     * @param query 查询对象
     * @return SysMessageKeyVo
     */
    List<SysMessageKeyVo> queryList(SysMessageKeyQuery query);

    /**
     * 新增消息常量
     *
     * @param bo 消息常量新增业务对象
     * @return Boolean
     */
    Boolean insertByBo(SysMessageKeyBo bo);

    /**
     * 修改消息常量
     *
     * @param bo 消息常量编辑业务对象
     * @return Boolean
     */
    Boolean updateByBo(SysMessageKeyBo bo);

    /**
     * 校验并批量删除消息常量信息
     *
     * @param ids 主键集合
     * @return Boolean
     */
    Boolean deleteWithValidByIds(Collection<Long> ids);

    /**
     * 查询消息key
     *
     * @param messageKeyId 消息常量id
     * @return 消息key
     */
    String getKeyById(Long messageKeyId);
}
