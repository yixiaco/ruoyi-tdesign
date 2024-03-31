package org.dromara.demo.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.SqlUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.demo.domain.TestDemo;
import org.dromara.demo.domain.bo.TestDemoBo;
import org.dromara.demo.domain.vo.TestDemoVo;
import org.dromara.demo.mapper.TestDemoMapper;
import org.dromara.demo.service.ITestDemoService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 测试单表Service业务层处理
 *
 * @author Lion Li
 * @date 2021-07-26
 */
@Service
public class TestDemoServiceImpl extends ServiceImpl<TestDemoMapper, TestDemo> implements ITestDemoService {

    @Override
    public TestDemoVo queryById(Long id) {
        return mapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<TestDemoVo> queryPageList(TestDemoBo bo, PageQuery pageQuery) {
        QueryWrapper lqw = buildQueryWrapper(bo);
        return PageQuery.of(() -> mapper.selectVoList(lqw));
    }

    /**
     * 自定义分页查询
     */
    @Override
    public TableDataInfo<TestDemoVo> customPageList(TestDemoBo bo, PageQuery pageQuery) {
        QueryWrapper lqw = buildQueryWrapper(bo);
        return PageQuery.of(() -> mapper.customList(lqw));
    }

    @Override
    public List<TestDemoVo> queryList(TestDemoBo bo) {
        return mapper.selectVoList(buildQueryWrapper(bo));
    }

    private QueryWrapper buildQueryWrapper(TestDemoBo bo) {
        Map<String, Object> params = bo.getParams();
        return QueryWrapper.create()
            .like(TestDemo::getTestKey, bo.getTestKey(), StringUtils.isNotBlank(bo.getTestKey()))
            .eq(TestDemo::getValue, bo.getValue(), StringUtils.isNotBlank(bo.getValue()))
            .between(TestDemo::getCreateTime,
                params.get("beginCreateTime"),
                params.get("endCreateTime"),
                params.get("beginCreateTime") != null && params.get("endCreateTime") != null)
            .orderBy(TestDemo::getId, true);
    }

    @Override
    public Boolean insertByBo(TestDemoBo bo) {
        TestDemo add = MapstructUtils.convert(bo, TestDemo.class);
        validEntityBeforeSave(add);
        boolean flag = mapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(TestDemoBo bo) {
        TestDemo update = MapstructUtils.convert(bo, TestDemo.class);
        validEntityBeforeSave(update);
        return mapper.update(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(TestDemo entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return mapper.deleteBatchByIds(ids) > 0;
    }

    @Override
    public Boolean saveBatch(List<TestDemo> list) {
        return SqlUtil.toBool(mapper.insertBatch(list));
    }
}
