package org.dromara.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysPost;
import org.dromara.system.domain.query.SysPostQuery;
import org.dromara.system.domain.vo.SysPostVo;

import java.util.List;

/**
 * 岗位信息 数据层
 *
 * @author Lion Li
 */
public interface SysPostMapper extends BaseMapperPlus<SysPost, SysPostVo> {

    /**
     * 查询用户所属岗位组
     *
     * @param userId 用户ID
     * @return 结果
     */
    List<SysPostVo> selectPostsByUserId(@Param("userId") Long userId);

    /**
     * 查询岗位信息列表
     *
     * @param query 查询对象
     * @return {@link SysPost}
     */
    List<SysPostVo> queryList(SysPostQuery query);
}
