package org.dromara.common.core.service;

import java.util.Map;

/**
 * oss规则服务
 *
 * @author hexm
 * @date 2023/05/05 16:06
 */
public interface OssRuleService {

    /**
     * 返回多字段url
     *
     * @param fieldName 字段名称
     * @param url       url
     * @param useRules  限定使用规则，为空则不限制
     * @return
     */
    Map<String, String> getUrls(String fieldName, String url, String[] useRules);
}
