package com.ruoyi.system.mapper;

import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.system.domain.SysOss;
import com.ruoyi.system.domain.bo.SysOssBo;
import com.ruoyi.system.domain.dto.SysOssQuery;
import com.ruoyi.system.domain.vo.SysOssVo;

import java.util.List;

/**
 * 文件上传 数据层
 *
 * @author Lion Li
 */
public interface SysOssMapper extends BaseMapperPlus<SysOssMapper, SysOss, SysOssVo> {

    /**
     * 查询OSS对象存储列表
     *
     * @param query 查询对象
     * @return {@link SysOssVo}
     */
    List<SysOssVo> queryList(SysOssQuery query);
}
