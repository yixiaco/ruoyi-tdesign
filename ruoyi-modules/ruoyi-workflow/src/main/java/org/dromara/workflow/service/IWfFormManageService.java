package org.dromara.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.workflow.domain.WfFormManage;
import org.dromara.workflow.domain.bo.WfFormManageBo;
import org.dromara.workflow.domain.query.WfFormManageQuery;
import org.dromara.workflow.domain.vo.WfFormManageVo;

import java.util.Collection;
import java.util.List;

/**
 * 表单管理Service接口
 *
 * @author may
 * @date 2024-03-29
 */
public interface IWfFormManageService extends IService<WfFormManage> {

    /**
     * 查询表单管理
     *
     * @param id 主键
     * @return 结果
     */
    WfFormManageVo queryById(Long id);

    /**
     * 查询表单管理
     *
     * @param ids 主键
     * @return 结果
     */
    List<WfFormManageVo> queryByIds(List<Long> ids);

    /**
     * 分页查询表单管理列表
     *
     * @param query 查询对象
     * @return 表单管理分页列表
     */
    TableDataInfo<WfFormManageVo> queryPageList(WfFormManageQuery query);

    /**
     * 查询表单管理列表
     *
     * @return 结果
     */
    List<WfFormManageVo> selectList();
    /**
     * 查询表单管理列表
     *
     * @param query 查询对象
     * @return 结果
     */
    List<WfFormManageVo> queryList(WfFormManageQuery query);

    /**
     * 新增表单管理
     *
     * @param bo 参数
     * @return 结果
     */
    Boolean insertByBo(WfFormManageBo bo);

    /**
     * 修改表单管理
     *
     * @param bo 参数
     * @return 结果
     */
    Boolean updateByBo(WfFormManageBo bo);

    /**
     * 批量删除表单管理信息
     *
     * @param ids 主键
     * @return 结果
     */
    Boolean deleteByIds(Collection<Long> ids);
}
