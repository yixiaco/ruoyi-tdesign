package org.dromara.common.core.utils;

import io.github.linpeilie.Converter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.common.core.utils.spring.SpringUtils;

import java.util.List;
import java.util.Map;

/**
 * Mapstruct 工具类
 * <p>参考文档：<a href="https://mapstruct.plus/introduction/quick-start.html">mapstruct-plus</a></p>
 *
 *
 * @author Michelle.Chung
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapstructUtils {

    private final static Converter CONVERTER = SpringUtils.getBean(Converter.class);

    /**
     * 将 T 类型对象，转换为 desc 类型的对象并返回
     *
     * @param source 数据来源实体
     * @param desc   描述对象 转换后的对象
     * @return desc
     */
    public static <T, V> V convert(T source, Class<V> desc) {
        if (source == null || desc == null) {
            return null;
        }
        return CONVERTER.convert(source, desc);
    }

    /**
     * 将 T 类型对象，按照配置的映射字段规则，给 desc 类型的对象赋值并返回 desc 对象
     *
     * @param source 数据来源实体
     * @param desc   转换后的对象
     * @return desc
     */
    public static <T, V> V convert(T source, V desc) {
        if (source == null || desc == null) {
            return null;
        }
        return CONVERTER.convert(source, desc);
    }

    /**
     * 将 T 类型的集合，转换为 desc 类型的集合并返回
     *
     * @param sourceList 数据来源实体列表
     * @param desc       描述对象 转换后的对象
     * @return desc
     */
    public static <T, V> List<V> convert(List<T> sourceList, Class<V> desc) {
        if (sourceList == null) {
            return null;
        }
        return CONVERTER.convert(sourceList, desc);
    }

    /**
     * 将 Map 转换为 beanClass 类型的集合并返回
     *
     * @param map       数据来源
     * @param beanClass bean类
     * @return bean对象
     */
    public static <T> T convert(Map<String, Object> map, Class<T> beanClass) {
        if (map == null || beanClass == null) {
            return null;
        }
        return CONVERTER.convert(map, beanClass);
    }

}
