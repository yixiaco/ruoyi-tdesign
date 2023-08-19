package org.dromara.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.dromara.common.core.constant.CacheNames;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.service.OssService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.file.FileUtils;
import org.dromara.common.core.utils.spring.SpringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.oss.core.OssClient;
import org.dromara.common.oss.entity.UploadResult;
import org.dromara.common.oss.enumd.AccessPolicyType;
import org.dromara.common.oss.factory.OssFactory;
import org.dromara.common.redis.utils.CacheUtils;
import org.dromara.system.domain.SysOss;
import org.dromara.system.domain.SysOssCategory;
import org.dromara.system.domain.bo.SysOssBo;
import org.dromara.system.domain.query.SysOssQuery;
import org.dromara.system.domain.vo.SysOssVo;
import org.dromara.system.mapper.SysOssMapper;
import org.dromara.system.service.ISysOssCategoryService;
import org.dromara.system.service.ISysOssService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
public class SysOssServiceImpl extends ServiceImpl<SysOssMapper, SysOss> implements ISysOssService, OssService {

    @Autowired
    private ISysOssCategoryService categoryService;

    /**
     * 查询OSS对象存储
     *
     * @param ossId 主键
     * @return SysOssVo
     */
    @Override
    public SysOssVo queryById(Long ossId) {
        return baseMapper.selectVoById(ossId);
    }

    /**
     * 查询OSS对象存储列表
     *
     * @param query 查询对象
     * @return
     */
    @Override
    public TableDataInfo<SysOssVo> queryPageList(SysOssQuery query) {
        return PageQuery.of(() -> {
            List<SysOssVo> vos = baseMapper.queryList(query);
            for (SysOssVo vo : vos) {
                matchingUrl(vo);
            }
            return vos;
        });
    }

    /**
     * 查询OSS对象基于id串
     *
     * @param ossIds OSS对象ID串
     */
    @Override
    public List<SysOssVo> listVoByIds(Collection<Long> ossIds) {
        List<SysOssVo> list = CacheUtils.takeCache(CacheNames.SYS_OSS, ossIds, ids -> {
            List<SysOssVo> ossVos = baseMapper.selectVoList(lambdaQuery().in(SysOss::getOssId, ossIds).getWrapper());
            return StreamUtils.toIdentityMap(ossVos, SysOssVo::getOssId);
        });
        return StreamUtils.toList(list, this::matchingUrl);
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

    /**
     * 通过ossId查询对应的url
     *
     * @param ossIds ossId串逗号分隔
     * @return
     */
    @Override
    public String selectUrlByIds(String ossIds) {
        List<Long> ids = StringUtils.splitTo(ossIds, Convert::toLong);
        List<SysOssVo> ossVos = listVoByIds(ids);
        return StreamUtils.join(ossVos, SysOssVo::getUrl);
    }

    /**
     * 通过id获取oss对象
     *
     * @param ossId ossId
     * @return
     */
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
        OssClient storage = OssFactory.instance(sysOss.getService());
        try (InputStream inputStream = storage.getObjectContent(sysOss.getUrl())) {
            int available = inputStream.available();
            IoUtil.copy(inputStream, response.getOutputStream(), available);
            response.setContentLength(available);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 上传OSS对象存储
     *
     * @param file 文件
     * @param bo   业务对象
     * @return
     */
    @Override
    public SysOssVo upload(MultipartFile file, SysOssBo bo) {
        String originalFilename = file.getOriginalFilename();
        String suffix = StringUtils.substring(originalFilename, originalFilename.lastIndexOf('.'), originalFilename.length());
        OssClient storage = OssFactory.instance();
        UploadResult uploadResult;
        try {
            uploadResult = storage.uploadSuffix(file.getBytes(), suffix, file.getContentType());
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
        // 保存文件信息
        return buildResultEntity(originalFilename, suffix, file.getContentType(), storage.getConfigKey(), uploadResult, file.getSize(), bo);
    }

    /**
     * 上传OSS对象存储
     *
     * @param file 文件
     * @param bo   业务对象
     * @return
     */
    @Override
    public SysOssVo upload(File file, SysOssBo bo) {
        String originalFileName = file.getName();
        String suffix = StringUtils.substring(originalFileName, originalFileName.lastIndexOf("."), originalFileName.length());
        OssClient storage = OssFactory.instance();
        UploadResult uploadResult = storage.uploadSuffix(file, suffix);
        String mimeType = FileUtil.getMimeType(file.getAbsolutePath());
        // 保存文件信息
        return buildResultEntity(originalFileName, suffix, mimeType, storage.getConfigKey(), uploadResult, file.length(), bo);
    }

    @NotNull
    private SysOssVo buildResultEntity(String originalFilename, String suffix, String contentType, String service, UploadResult uploadResult, Long size, SysOssBo bo) {
        SysOss oss = new SysOss();
        oss.setUrl(uploadResult.getUrl());
        oss.setFileSuffix(suffix);
        oss.setFileName(uploadResult.getFilename());
        oss.setOriginalName(originalFilename);
        oss.setService(service);
        oss.setSize(size);
        oss.setContentType(contentType);
        oss.setUserType(bo.getUserTypeEnum().getUserType());
        oss.setCreateBy(bo.getCreateBy());
        oss.setIsLock(bo.getIsLock());
        oss.setIsList(bo.getIsList());
        oss.setOssCategoryId(bo.getOssCategoryId());
        baseMapper.insert(oss);
        SysOssVo sysOssVo = MapstructUtils.convert(oss, SysOssVo.class);
        return this.matchingUrl(sysOssVo);
    }

    /**
     * 根据编辑业务对象修改OSS对象存储
     *
     * @param bo OSS对象存储编辑业务对象
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(SysOssBo bo) {
        SysOss oss = new SysOss();
        checkCategory(bo.getOssCategoryId());
        oss.setOssId(bo.getOssId());
        oss.setOriginalName(bo.getOriginalName());
        oss.setOssCategoryId(bo.getOssCategoryId());
        oss.setIsLock(bo.getIsLock());
        return updateById(oss);
    }

    /**
     * 检查分类是否存在
     *
     * @param ossCategoryId 分类id
     */
    private void checkCategory(Long ossCategoryId) {
        if (!ossCategoryId.equals(0L)) {
            boolean exists = categoryService.lambdaQuery()
                .eq(SysOssCategory::getOssCategoryId, ossCategoryId)
                .exists();
            if (!exists) {
                throw new ServiceException("分类不存在");
            }
        }
    }

    /**
     * 删除OSS对象存储
     *
     * @param ids     OSS对象ID串
     * @param isValid 校验
     * @return
     */
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

    /**
     * 移动到分类
     *
     * @param categoryId 分类id
     * @param ossIds     主键id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void move(Long categoryId, List<Long> ossIds) {
        checkCategory(categoryId);
        List<SysOss> list = ossIds.stream().map(id -> {
            SysOss oss = new SysOss();
            oss.setOssId(id);
            oss.setOssCategoryId(categoryId);
            return oss;
        }).toList();
        updateBatchById(list);
    }
}
