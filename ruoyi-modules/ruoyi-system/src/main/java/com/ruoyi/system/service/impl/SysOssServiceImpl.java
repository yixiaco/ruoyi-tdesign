package com.ruoyi.system.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.constant.CacheNames;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.BeanCopyUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.file.FileUtils;
import com.ruoyi.common.core.utils.spring.SpringUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.oss.core.OssClient;
import com.ruoyi.common.oss.entity.UploadResult;
import com.ruoyi.common.oss.enumd.AccessPolicyType;
import com.ruoyi.common.oss.factory.OssFactory;
import com.ruoyi.system.domain.SysOss;
import com.ruoyi.system.domain.dto.SysOssQuery;
import com.ruoyi.system.domain.vo.SysOssVo;
import com.ruoyi.system.mapper.SysOssMapper;
import com.ruoyi.system.service.ISysOssService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * 文件上传 服务层实现
 *
 * @author Lion Li
 */
@Service
public class SysOssServiceImpl extends ServiceImpl<SysOssMapper, SysOss> implements ISysOssService {

    @Override
    public TableDataInfo<SysOssVo> queryPageList(SysOssQuery query) {
        return PageQuery.of(() -> baseMapper.queryList(query));
    }

    @Override
    public List<SysOssVo> listVoByIds(Collection<Long> ossIds) {
        List<SysOssVo> list = baseMapper.selectVoList(lambdaQuery().in(SysOss::getOssId, ossIds).getWrapper());
        for (SysOssVo vo : list) {
            matchingUrl(vo);
        }
        return list;
    }

    /**
     * 查询OSS对象基于url串
     *
     * @param urls OSS对象url串
     * @return
     */
    @Override
    public List<SysOssVo> listVoByUrls(List<String> urls) {
        List<SysOssVo> ossVos = baseMapper.selectVoList(lambdaQuery().in(SysOss::getUrl, urls).getWrapper());
        for (SysOssVo ossVo : ossVos) {
            matchingUrl(ossVo);
        }
        return ossVos;
    }

    @Cacheable(cacheNames = CacheNames.SYS_OSS, key = "#ossId")
    @Override
    public SysOssVo getById(Long ossId) {
        return baseMapper.selectVoById(ossId);
    }

    @Override
    public void download(Long ossId, HttpServletResponse response) throws IOException {
        SysOssVo sysOss = SpringUtils.getAopProxy(this).getById(ossId);
        if (ObjectUtil.isNull(sysOss)) {
            throw new ServiceException("文件数据不存在!");
        }
        FileUtils.setAttachmentResponseHeader(response, sysOss.getOriginalName());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE + "; charset=UTF-8");
        OssClient storage = OssFactory.instance();
        try (InputStream inputStream = storage.getObjectContent(sysOss.getUrl())) {
            int available = inputStream.available();
            IoUtil.copy(inputStream, response.getOutputStream(), available);
            response.setContentLength(available);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public SysOssVo upload(MultipartFile file) {
        String originalfileName = file.getOriginalFilename();
        String suffix = StringUtils.substring(originalfileName, originalfileName.lastIndexOf("."), originalfileName.length());
        OssClient storage = OssFactory.instance();
        UploadResult uploadResult;
        try {
            uploadResult = storage.uploadSuffix(file.getBytes(), suffix, file.getContentType());
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
        // 保存文件信息
        SysOss oss = new SysOss();
        oss.setUrl(uploadResult.getUrl());
        oss.setFileSuffix(suffix);
        oss.setFileName(uploadResult.getFilename());
        oss.setOriginalName(originalfileName);
        oss.setService(storage.getConfigKey());
        oss.setSize(file.getSize());
        baseMapper.insert(oss);
        SysOssVo sysOssVo = new SysOssVo();
        BeanCopyUtils.copy(oss, sysOssVo);
        return this.matchingUrl(sysOssVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // 做一些业务上的校验,判断是否需要校验
        }
        List<SysOss> list = baseMapper.selectBatchIds(ids);
        for (SysOss sysOss : list) {
            OssClient storage = OssFactory.instance(sysOss.getService());
            storage.delete(sysOss.getUrl());
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 匹配Url
     *
     * @param oss OSS对象
     * @return oss 匹配Url的OSS对象
     */
    private SysOssVo matchingUrl(SysOssVo oss) {
        OssClient storage = OssFactory.instance(oss.getService());
        // 仅修改桶类型为 private 的URL，临时URL时长为120s
        if (AccessPolicyType.PRIVATE == storage.getAccessPolicy()) {
            oss.setUrl(storage.getPrivateUrl(oss.getFileName(), 120));
        }
        return oss;
    }
}
