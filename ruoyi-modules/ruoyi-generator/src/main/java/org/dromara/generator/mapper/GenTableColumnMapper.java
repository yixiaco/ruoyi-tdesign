package org.dromara.generator.mapper;

import com.mybatisflex.annotation.UseDataSource;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.MyBaseMapper;
import org.dromara.generator.domain.GenTableColumn;

import java.util.List;

/**
 * 业务字段 数据层
 *
 * @author Lion Li
 */
public interface GenTableColumnMapper extends MyBaseMapper<GenTableColumn> {
    /**
     * 根据表名称查询列信息
     *
     * @param tableName 表名称
     * @param dataName  数据源名称
     * @return 列信息
     */
    @UseDataSource("#dataName")
    List<GenTableColumn> selectDbTableColumnsByName(@Param("tableName") String tableName, String dataName);

}
