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
     * @param fieldName  字段名称
     * @param url        url
     * @param ruleNames  限定使用规则，为空则不限制
     * @param join       字段与规则的连接符
     * @param useDefault 指定规则时，是否使用默认规则。 指定的规则不存在时，即使是false也将使用默认规则
     * @return key：字段名称 value：url
     */
    Map<String, String> getUrls(String fieldName, String url, String[] ruleNames, String join, boolean useDefault);
}
