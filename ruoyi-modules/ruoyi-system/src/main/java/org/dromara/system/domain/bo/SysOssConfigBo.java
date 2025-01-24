package org.dromara.system.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.system.domain.SysOssConfig;

/**
 * 对象存储配置业务对象 sys_oss_config
 *
 * @author Lion Li
 * @author 孤舟烟雨
 * @date 2021-08-13
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysOssConfig.class, reverseConvertGenerate = false)
public class SysOssConfigBo extends BaseEntity {

    /**
     * 主建
     */
    @NotNull(message = "主建不能为空", groups = {EditGroup.class})
    private Long ossConfigId;

    /**
     * 配置key
     */
    @NotBlank(message = "配置key不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 2, max = 100, message = "configKey长度必须介于{min}和{max} 之间", groups = {AddGroup.class, EditGroup.class})
    private String configKey;

    /**
     * accessKey
     */
    @NotBlank(message = "accessKey不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 2, max = 100, message = "accessKey长度必须介于{min}和{max} 之间", groups = {AddGroup.class, EditGroup.class})
    private String accessKey;

    /**
     * 秘钥
     */
    @NotBlank(message = "secretKey不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 2, max = 100, message = "secretKey长度必须介于{min}和{max} 之间", groups = {AddGroup.class, EditGroup.class})
    private String secretKey;

    /**
     * 桶名称
     */
    @NotBlank(message = "桶名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 2, max = 100, message = "bucketName长度必须介于{min}和{max}之间", groups = {AddGroup.class, EditGroup.class})
    private String bucketName;

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 访问站点
     */
    @NotBlank(message = "访问站点不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 2, max = 100, message = "endpoint长度必须介于{min}和{max}之间")
    private String endpoint;

    /**
     * 自定义域名
     */
    private String domain;

    /**
     * URL风格（0=虚拟托管风格，1=路径风格）
     *
     * @see https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/VirtualHosting.html
     */
    private String urlStyle = "0";

    /**
     * 是否https（Y=是,N=否）
     */
    @NotBlank(message = "是否https", groups = {AddGroup.class, EditGroup.class})
    private String isHttps;

    /**
     * 域
     */
    private String region;

    /**
     * 桶权限类型(0=private 1=public 2=custom)
     */
    @NotBlank(message = "桶权限类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String accessPolicy;

    /**
     * 是否默认（1=是,0=否）
     */
    @NotBlank(message = "是否默认不能为空", groups = {AddGroup.class, EditGroup.class})
    private String status;

    /**
     * 创建桶（1=是,0=否）
     */
    private Integer createBucket;

    /**
     * 扩展字段
     */
    private String ext1;

    /**
     * 备注
     */
    private String remark;

}
