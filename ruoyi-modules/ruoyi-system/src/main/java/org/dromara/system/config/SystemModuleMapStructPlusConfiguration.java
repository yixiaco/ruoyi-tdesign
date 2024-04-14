package org.dromara.system.config;

import io.github.linpeilie.annotations.MapperConfig;

/**
 * 解决MapStructPlus在对象属性是List复制时发生转换报错
 * example:
 * <pre>{@code
 * public class A {
 *     private List<A> children = new ArrayList<>();
 * }
 *
 * @AutoMapper(target = A.class)
 * public class B {
 *     // 这里会从A自动转为B类型
 *     private List<B> children = new ArrayList<>();
 * }
 * }</pre>
 */
@MapperConfig(adapterClassName = "SystemModuleConvertMapperAdapter",
    adapterPackage = "io.github.linpeilie.adapter",
    mapAdapterClassName = "SystemModuleMapConvertMapperAdapter")
public class SystemModuleMapStructPlusConfiguration {
}
