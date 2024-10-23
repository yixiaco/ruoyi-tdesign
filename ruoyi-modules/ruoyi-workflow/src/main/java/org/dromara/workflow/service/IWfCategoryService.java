package org.dromara.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.workflow.domain.WfCategory;
import org.dromara.workflow.domain.bo.WfCategoryBo;
import org.dromara.workflow.domain.query.WfCategoryQuery;
import org.dromara.workflow.domain.vo.WfCategoryVo;

import java.util.Collection;
import java.util.List;

/**
 * 流程分类Service接口
 *
 * @author may
 * @date 2023-06-28
 */
public interface IWfCategoryService extends IService<WfCategory> {

    /**
     * 查询流程分类
     *
     * @param id 主键
     * @return WfCategoryVo
     */
    WfCategoryVo queryById(Long id);

    /**
     * 查询流程分类列表
     *
     * @param query 查询对象
     * @return WfCategoryVo
     */
    List<WfCategoryVo> queryList(WfCategoryQuery query);

    /**
     * 新增流程分类
     *
     * @param bo 流程分类新增业务对象
     * @return Boolean
     */
    Boolean insertByBo(WfCategoryBo bo);

    /**
     * 修改流程分类
     *
     * @param bo 流程分类编辑业务对象
     * @return Boolean
     */
    Boolean updateByBo(WfCategoryBo bo);

    /**
     * 校验并批量删除流程分类信息
     *
     * @param ids 主键集合
     * @return Boolean
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 按照类别编码查询
     *
     * @param categoryCode 分类比吗
     * @return 结果
     */
    WfCategory queryByCategoryCode(String categoryCode);
}
