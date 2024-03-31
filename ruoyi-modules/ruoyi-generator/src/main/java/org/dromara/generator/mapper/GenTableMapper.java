package org.dromara.generator.mapper;

import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.generator.domain.GenTable;
import org.dromara.generator.domain.query.GenTableQuery;
import org.dromara.generator.domain.vo.GenTableVo;

import java.util.List;

/**
 * 业务 数据层
 *
 * @author Lion Li
 */
public interface GenTableMapper extends BaseMapper<GenTable> {

    /**
     * 查询代码生成业务列表
     *
     * @param query 查询对象
     * @return {@link GenTableVo}
     */
    List<GenTableVo> queryList(GenTableQuery query);

    /**
     * 查询数据库列表
     *
     * @param query 查询条件
     * @return 数据库表集合
     */
    List<GenTableVo> selectDbTableList(GenTableQuery query);

    /**
     * 查询数据库列表
     *
     * @param tableNames 表名称组
     * @return 数据库表集合
     */
    List<GenTableVo> selectDbTableListByNames(String[] tableNames);

    /**
     * 查询所有表信息
     *
     * @return 表信息集合
     */
    List<GenTableVo> selectGenTableAll();

    /**
     * 查询表ID业务信息
     *
     * @param tableId 业务ID
     * @return 业务信息
     */
    GenTableVo selectGenTableById(@Param("tableId") Long tableId);

    /**
     * 查询表名称业务信息
     *
     * @param tableName 表名称
     * @return 业务信息
     */
    GenTableVo selectGenTableByName(@Param("tableName") String tableName);

    /**
     * 查询表名列表
     *
     * @param dataName 数据源
     * @return
     */
    List<String> selectTableNameList(@Param("dataName") String dataName);
}
