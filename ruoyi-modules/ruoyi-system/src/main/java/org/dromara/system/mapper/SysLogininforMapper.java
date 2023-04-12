package org.dromara.system.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysLogininfor;
import org.dromara.system.domain.bo.SysLogininforBo;
import org.dromara.system.domain.vo.SysLogininforVo;

import java.util.List;

/**
 * 系统访问日志情况信息 数据层
 *
 * @author Lion Li
 */
public interface SysLogininforMapper extends BaseMapperPlus<SysLogininfor, SysLogininforVo> {

    /**
     * 查询系统访问记录列表
     *
     * @param logininfor
     * @return {@link SysLogininfor}
     */
    List<SysLogininforVo> queryList(SysLogininforBo logininfor);
}
