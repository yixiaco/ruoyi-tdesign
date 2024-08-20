package org.dromara.system.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

/**
 * 敏感词测试结果vo
 *
 * @author hexm
 * @date 2024/8/20
 */
@Data
public class SysSensitiveWordTestVo implements Serializable {

    /** 是否包含敏感词 */
    private boolean containsSensitiveWord;
    /** 敏感词替换后的内容 */
    private String sensitiveWordReplace;
    /** 包含的敏感词 */
    private Collection<String> sensitiveWords;
}
