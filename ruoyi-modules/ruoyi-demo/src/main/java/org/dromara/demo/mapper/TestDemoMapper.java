package org.dromara.demo.mapper;

import com.mybatisflex.core.FlexConsts;
import com.mybatisflex.core.provider.EntitySqlProvider;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.common.mybatis.core.mapper.MyBaseMapperVo;
import org.dromara.demo.domain.TestDemo;
import org.dromara.demo.domain.vo.TestDemoVo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 测试单表Mapper接口
 *
 * @author Lion Li
 * @date 2021-07-26
 */
public interface TestDemoMapper extends MyBaseMapperVo<TestDemo, TestDemoVo> {

    @DataPermission({
        @DataColumn(key = "deptName", value = "dept_id"),
        @DataColumn(key = "userName", value = "user_id")
    })
    @SelectProvider(type = EntitySqlProvider.class, method = "selectListByQuery")
    List<TestDemoVo> customList(@Param(FlexConsts.QUERY) QueryWrapper queryWrapper);

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "dept_id"),
        @DataColumn(key = "userName", value = "user_id")
    })
    @SelectProvider(type = EntitySqlProvider.class, method = "selectListByQuery")
    List<TestDemo> selectListByQuery(@Param(FlexConsts.QUERY) QueryWrapper queryWrapper);

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "dept_id"),
        @DataColumn(key = "userName", value = "user_id")
    })
    @UpdateProvider(type = EntitySqlProvider.class, method = "update")
    int update(@Param(FlexConsts.ENTITY) TestDemo entity);

    @Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "dept_id"),
        @DataColumn(key = "userName", value = "user_id")
    })
    @DeleteProvider(type = EntitySqlProvider.class, method = "deleteBatchByIds")
    int deleteBatchByIds(@Param(FlexConsts.PRIMARY_VALUE) Collection<? extends Serializable> ids);
}
