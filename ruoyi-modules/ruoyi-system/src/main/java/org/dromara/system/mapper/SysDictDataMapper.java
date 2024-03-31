package org.dromara.system.mapper;

import com.mybatisflex.core.query.QueryWrapper;
import org.dromara.common.mybatis.core.mapper.MyBaseMapperVo;
import org.dromara.system.domain.SysDictData;
import org.dromara.system.domain.query.SysDictDataQuery;
import org.dromara.system.domain.vo.SysDictDataVo;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author Lion Li
 */
public interface SysDictDataMapper extends MyBaseMapperVo<SysDictData, SysDictDataVo> {

    /**
     * 查询字典数据列表
     *
     * @param dictData
     * @return {@link SysDictData}
     */
    List<SysDictDataVo> queryList(SysDictDataQuery dictData);

    default List<SysDictDataVo> selectDictDataByType(String dictType) {
        return selectVoList(
            QueryWrapper.create()
                .eq(SysDictData::getDictType, dictType)
                .orderBy(SysDictData::getDictSort, true));
    }
}
