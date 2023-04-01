package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.system.domain.SysOss;
import com.ruoyi.system.domain.dto.SysOssQuery;
import com.ruoyi.system.domain.vo.SysOssVo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 文件上传 服务层
 *
 * @author Lion Li
 */
public interface ISysOssService extends IService<SysOss> {

    /**
     * 查询OSS对象存储列表
     */
    TableDataInfo<SysOssVo> queryPageList(SysOssQuery query);

    /**
     * 查询OSS对象基于id串
     *
     * @param ossIds OSS对象ID串
     */
    List<SysOssVo> listVoByIds(Collection<Long> ossIds);

    /**
     * 通过id获取oss对象
     *
     * @param ossId
     * @return
     */
    SysOssVo getById(Long ossId);

    /**
     * 上传OSS对象存储
     *
     * @param file 文件
     */
    SysOssVo upload(MultipartFile file);

    /**
     * 下载OSS对象
     *
     * @param ossId OSS对象ID
     */
    void download(Long ossId, HttpServletResponse response) throws IOException;

    /**
     * 删除OSS对象存储
     *
     * @param ids     OSS对象ID串
     * @param isValid 校验
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 查询OSS对象基于url串
     *
     * @param urls OSS对象url串
     * @return
     */
    List<SysOssVo> listVoByUrls(List<String> urls);
}
