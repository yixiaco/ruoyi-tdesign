package com.ruoyi.common.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;

import java.util.List;
import java.util.Map;

/**
 * bean深拷贝工具(基于 cglib 性能优异)
 * <p>
 * 重点 cglib 不支持 拷贝到链式对象
 * 例如: 源对象 拷贝到 目标(链式对象)
 * 请区分好`浅拷贝`和`深拷贝`再做使用
 *
 * @author Lion Li
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanCopyUtils {

    /**
     * 单对象基于class创建拷贝
     *
     * @param source 数据来源实体
     * @param desc   描述对象 转换后的对象
     * @return desc
     */
    public static <T, V> V toBean(T source, Class<V> desc) {
        final V target = ReflectUtil.newInstanceIfPossible(desc);
        BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     * 单对象基于class创建拷贝
     *
     * @param source 数据来源实体
     * @param desc   描述对象 转换后的对象
     * @return desc
     */
    public static <T, V> V copy(T source, Class<V> desc) {
        if (ObjectUtil.isNull(source)) {
            return null;
        }
        if (ObjectUtil.isNull(desc)) {
            return null;
        }
        final V target = ReflectUtil.newInstanceIfPossible(desc);
        return copy(source, target);
    }

    /**
     * 单对象基于对象创建拷贝
     *
     * @param source 数据来源实体
     * @param desc   转换后的对象
     * @return desc
     */
    public static <T, V> V copy(T source, V desc) {
        if (ObjectUtil.isNull(source)) {
            return null;
        }
        if (ObjectUtil.isNull(desc)) {
            return null;
        }
        BeanUtils.copyProperties(source, desc);
        return desc;
    }

    /**
     * 单对象基于对象创建拷贝
     *
     * @param source   数据来源实体
     * @param desc     转换后的对象
     * @param editable 将属性设置限制为的类（或接口）
     * @return desc
     */
    public static <T, V> V copy(T source, V desc, Class<?> editable) {
        if (ObjectUtil.isNull(source)) {
            return null;
        }
        if (ObjectUtil.isNull(desc)) {
            return null;
        }
        BeanUtils.copyProperties(source, desc, editable);
        return desc;
    }

    /**
     * 单对象基于对象创建拷贝
     *
     * @param source           数据来源实体
     * @param desc             转换后的对象
     * @param ignoreProperties 要忽略的属性名称数组
     * @return desc
     */
    public static <T, V> V copy(T source, V desc, String... ignoreProperties) {
        if (ObjectUtil.isNull(source)) {
            return null;
        }
        if (ObjectUtil.isNull(desc)) {
            return null;
        }
        BeanUtils.copyProperties(source, desc, ignoreProperties);
        return desc;
    }

    /**
     * 列表对象基于class创建拷贝
     *
     * @param sourceList 数据来源实体列表
     * @param desc       描述对象 转换后的对象
     * @return desc
     */
    public static <T, V> List<V> copyList(List<T> sourceList, Class<V> desc) {
        if (ObjectUtil.isNull(sourceList)) {
            return null;
        }
        if (CollUtil.isEmpty(sourceList)) {
            return CollUtil.newArrayList();
        }
        return StreamUtils.toList(sourceList, source -> {
            V target = ReflectUtil.newInstanceIfPossible(desc);
            copy(source, target);
            return target;
        });
    }

    /**
     * bean拷贝到map
     *
     * @param bean 数据来源实体
     * @return map对象
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, Object> copyToMap(T bean) {
        if (ObjectUtil.isNull(bean)) {
            return null;
        }
        return BeanMap.create(bean);
    }

    /**
     * map拷贝到bean
     *
     * @param map       数据来源
     * @param beanClass bean类
     * @return bean对象
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) {
        if (MapUtil.isEmpty(map)) {
            return null;
        }
        if (ObjectUtil.isNull(beanClass)) {
            return null;
        }
        T bean = ReflectUtil.newInstanceIfPossible(beanClass);
        return mapToBean(map, bean);
    }

    /**
     * map拷贝到bean
     *
     * @param map  数据来源
     * @param bean bean对象
     * @return bean对象
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        if (MapUtil.isEmpty(map)) {
            return null;
        }
        if (ObjectUtil.isNull(bean)) {
            return null;
        }
        BeanMap.create(bean).putAll(map);
        return bean;
    }

}
