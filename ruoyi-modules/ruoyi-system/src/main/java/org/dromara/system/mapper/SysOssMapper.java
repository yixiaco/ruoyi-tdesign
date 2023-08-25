package org.dromara.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysOss;
import org.dromara.system.domain.query.SysOssQuery;
import org.dromara.system.domain.vo.SysOssVo;

import java.util.List;

/**
 * 文件上传 数据层
 *
 * @author Lion Li
 */
public interface SysOssMapper extends BaseMapperPlus<SysOss, SysOssVo> {

    /**
     * 查询OSS对象存储列表
     *
     * @param query 查询对象
     * @return {@link SysOssVo}
     */
    List<SysOssVo> queryList(SysOssQuery query);

    /**
     * 查询OSS对象存储
     *
     * @param ossId 主键
     * @return
     */
    SysOssVo queryById(@Param("ossId") Long ossId);
}
