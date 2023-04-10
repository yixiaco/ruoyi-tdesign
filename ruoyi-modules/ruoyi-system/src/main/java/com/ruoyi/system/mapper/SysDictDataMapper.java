package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.constant.UserConstants;
import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.domain.bo.SysDictDataBo;
import com.ruoyi.system.domain.vo.SysDictDataVo;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author Lion Li
 */
public interface SysDictDataMapper extends BaseMapperPlus<SysDictData, SysDictDataVo> {

    /**
     * 查询字典数据列表
     *
     * @param dictData
     * @return {@link SysDictData}
     */
    List<SysDictDataVo> queryList(SysDictDataBo dictData);

    default List<SysDictDataVo> selectDictDataByType(String dictType) {
        return selectVoList(
            new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getStatus, UserConstants.DICT_NORMAL)
                .eq(SysDictData::getDictType, dictType)
                .orderByAsc(SysDictData::getDictSort));
    }
}
