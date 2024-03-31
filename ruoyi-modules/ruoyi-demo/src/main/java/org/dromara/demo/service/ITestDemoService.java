package org.dromara.demo.service;

import com.mybatisflex.core.service.IService;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.demo.domain.TestDemo;
import org.dromara.demo.domain.bo.TestDemoBo;
import org.dromara.demo.domain.vo.TestDemoVo;

import java.util.Collection;
import java.util.List;

/**
 * 测试单表Service接口
 *
 * @author Lion Li
 * @date 2021-07-26
 */
public interface ITestDemoService extends IService<TestDemo> {

    /**
     * 查询单个
     *
     * @return
     */
    TestDemoVo queryById(Long id);

    /**
     * 查询列表
     */
    TableDataInfo<TestDemoVo> queryPageList(TestDemoBo bo, PageQuery pageQuery);

    /**
     * 自定义分页查询
     */
    TableDataInfo<TestDemoVo> customPageList(TestDemoBo bo, PageQuery pageQuery);

    /**
     * 查询列表
     */
    List<TestDemoVo> queryList(TestDemoBo bo);

    /**
     * 根据新增业务对象插入测试单表
     *
     * @param bo 测试单表新增业务对象
     * @return
     */
    Boolean insertByBo(TestDemoBo bo);

    /**
     * 根据编辑业务对象修改测试单表
     *
     * @param bo 测试单表编辑业务对象
     * @return
     */
    Boolean updateByBo(TestDemoBo bo);

    /**
     * 校验并删除数据
     *
     * @param ids     主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 批量保存
     */
    Boolean saveBatch(List<TestDemo> list);
}
