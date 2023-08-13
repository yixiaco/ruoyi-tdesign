package org.dromara.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.SysOss;
import org.dromara.system.domain.bo.SysOssBo;
import org.dromara.system.domain.query.SysOssQuery;
import org.dromara.system.domain.vo.SysOssVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
     * @param ossId ossId
     * @return
     */
    SysOssVo getById(Long ossId);

    /**
     * 上传OSS对象存储
     *
     * @param file 文件
     * @param bo   业务对象
     */
    SysOssVo upload(MultipartFile file, SysOssBo bo);

    /**
     * 上传OSS对象存储
     *
     * @param file 文件
     * @param bo   业务对象
     */
    SysOssVo upload(File file, SysOssBo bo);

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
