package org.dromara.common.mybatis.core.mapper;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.core.GenericTypeResolver;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 自定义 Mapper 接口, 实现 Vo 自定义扩展
 *
 * @param <T> table 泛型
 * @param <V> vo 泛型
 * @author hexm
 * @since 2024-03-30
 */
@SuppressWarnings("unchecked")
public interface MyBaseMapperVo<T, V> extends MyBaseMapper<T> {

    Log log = LogFactory.getLog(MyBaseMapperVo.class);

    default Class<V> currentVoClass() {
        return (Class<V>) Objects.requireNonNull(GenericTypeResolver.resolveTypeArguments(getClass(), MyBaseMapperVo.class))[1];
    }

    default V selectVoById(Serializable id) {
        return selectVoById(id, currentVoClass());
    }

    default List<V> selectVoBatchIds(Collection<? extends Serializable> idList) {
        return selectVoBatchIds(idList, currentVoClass());
    }

    default List<V> selectVoByMap(Map<String, Object> map) {
        return selectVoByMap(map, currentVoClass());
    }

    default V selectVoOne(QueryWrapper wrapper) {
        return selectVoOne(wrapper, currentVoClass());
    }

    default List<V> selectVoList() {
        return selectVoList(QueryWrapper.create(), currentVoClass());
    }

    default List<V> selectVoList(QueryWrapper wrapper) {
        return selectVoList(wrapper, currentVoClass());
    }

    default <P extends Page<V>> P selectVoPage(Page<T> page, QueryWrapper wrapper) {
        return selectVoPage(page, wrapper, currentVoClass());
    }

}
