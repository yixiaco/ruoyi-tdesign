package com.ruoyi.system.controller.system;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.ObjectUtil;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.validate.QueryGroup;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.system.domain.dto.SysOssQuery;
import com.ruoyi.system.domain.vo.SysOssVo;
import com.ruoyi.system.service.ISysOssService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 文件上传 控制层
 *
 * @author Lion Li
 */
@Validated
@RestController
@RequestMapping("/system/oss")
public class SysOssController extends BaseController {

    @Autowired
    private ISysOssService iSysOssService;

    /**
     * 查询OSS对象存储列表
     */
    @SaCheckPermission("system:oss:list")
    @GetMapping("/list")
    public TableDataInfo<SysOssVo> list(@Validated(QueryGroup.class) SysOssQuery bo) {
        return iSysOssService.queryPageList(bo);
    }

    /**
     * 查询OSS对象基于id串
     *
     * @param ossIds OSS对象ID串
     */
    @SaCheckPermission("system:oss:list")
    @GetMapping("/listByIds/{ossIds}")
    public R<List<SysOssVo>> listByIds(@NotEmpty(message = "主键不能为空")
                                       @PathVariable Long[] ossIds) {
        List<SysOssVo> list = iSysOssService.listVoByIds(List.of(ossIds));
        return R.ok(list);
    }

    /**
     * 查询OSS对象基于url串
     *
     * @param urls OSS对象url串
     */
    @SaCheckPermission("system:oss:list")
    @GetMapping("/listByUrls")
    public R<List<SysOssVo>> listByUrls(@NotEmpty(message = "url不能为空") String urls) {
        String[] urlArray = URLDecoder.decode(String.valueOf(urls), StandardCharsets.UTF_8).split(",");
        List<SysOssVo> list = iSysOssService.listVoByUrls(List.of(urlArray));
        return R.ok(list);
    }

    /**
     * 上传OSS对象存储
     *
     * @param file 文件
     */
    @SaCheckPermission("system:oss:upload")
    @Log(title = "OSS对象存储", businessType = BusinessType.INSERT)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Map<String, String>> upload(@RequestPart("file") MultipartFile file) {
        if (ObjectUtil.isNull(file)) {
            throw new ServiceException("上传文件不能为空");
        }
        SysOssVo oss = iSysOssService.upload(file);
        return R.ok(Map.of(
            "url", oss.getUrl(),
            "fileName", oss.getOriginalName(),
            "ossId", oss.getOssId().toString()
        ));
    }

    /**
     * 下载OSS对象
     *
     * @param ossId OSS对象ID
     */
    @SaCheckPermission("system:oss:download")
    @GetMapping("/download/{ossId}")
    public void download(@PathVariable Long ossId, HttpServletResponse response) throws IOException {
        iSysOssService.download(ossId, response);
    }

    /**
     * 删除OSS对象存储
     *
     * @param ossIds OSS对象ID串
     */
    @SaCheckPermission("system:oss:remove")
    @Log(title = "OSS对象存储", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ossIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ossIds) {
        return toAjax(iSysOssService.deleteWithValidByIds(List.of(ossIds), true));
    }

}
