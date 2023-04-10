package com.ruoyi.system.mapper;

import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.domain.bo.SysLogininforBo;
import com.ruoyi.system.domain.vo.SysLogininforVo;

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
