package org.dromara.common.mybatis.core.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.core.util.ClassUtil;
import com.mybatisflex.core.util.SqlUtil;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.dromara.common.core.utils.MapstructUtils;
import org.springframework.core.GenericTypeResolver;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 自定义 Mapper 接口, 实现 自定义扩展
 *
 * @param <T> table 泛型
 * @param <V> vo 泛型
 * @author Lion Li
 * @since 2021-05-13
 */
@SuppressWarnings("unchecked")
public interface BaseMapperPlus<T, V> extends BaseMapper<T> {

    Log log = LogFactory.getLog(BaseMapperPlus.class);

    default Class<V> currentVoClass() {
        return (Class<V>) Objects.requireNonNull(GenericTypeResolver.resolveTypeArguments(this.getClass(), BaseMapperPlus.class))[1];
    }

    default Class<T> currentModelClass() {
        return (Class<T>) Objects.requireNonNull(GenericTypeResolver.resolveTypeArguments(this.getClass(), BaseMapperPlus.class))[0];
    }

    default List<T> selectList() {
        return this.selectListByQuery(QueryWrapper.create());
    }

    /**
     * 批量插入
     */
    default boolean insertBatch(Collection<T> entities) {
        return insertBatch(entities, DEFAULT_BATCH_SIZE);
    }

    /**
     * 批量更新
     */
    default boolean updateBatchById(Collection<T> entities) {
        return updateBatchById(entities, DEFAULT_BATCH_SIZE);
    }

    /**
     * 批量插入或更新
     */
    default boolean insertOrUpdateBatch(Collection<T> entities) {
        return insertOrUpdateBatch(entities, DEFAULT_BATCH_SIZE);
    }

    /**
     * 批量插入(包含限制条数)
     */
    default boolean insertBatch(Collection<T> entities, int batchSize) {
        Class<BaseMapper<T>> usefulClass = (Class<BaseMapper<T>>) ClassUtil.getUsefulClass(this.getClass());
        return SqlUtil.toBool(Db.executeBatch(entities, batchSize, usefulClass, BaseMapper::insertSelective));
    }

    /**
     * 批量更新(包含限制条数)
     */
    default boolean updateBatchById(Collection<T> entities, int batchSize) {
        Class<BaseMapper<T>> usefulClass = (Class<BaseMapper<T>>) ClassUtil.getUsefulClass(this.getClass());
        return SqlUtil.toBool(Db.executeBatch(entities, batchSize, usefulClass, BaseMapper::update));
    }

    /**
     * 批量插入或更新(包含限制条数)
     */
    default boolean insertOrUpdateBatch(Collection<T> entities, int batchSize) {
        Class<BaseMapper<T>> usefulClass = (Class<BaseMapper<T>>) ClassUtil.getUsefulClass(this.getClass());
        return SqlUtil.toBool(Db.executeBatch(entities, batchSize, usefulClass, BaseMapper::insertOrUpdateSelective));
    }

    /**
     * 根据 Wrapper 条件，判断是否存在记录
     *
     * @param queryWrapper 实体对象封装操作类
     * @return 是否存在记录
     */
    default boolean exists(QueryWrapper queryWrapper) {
        long count = this.selectCountByQuery(queryWrapper);
        return count > 0;
    }

    /**
     * 插入或更新(包含限制条数)
     */
    default boolean saveOrUpdate(T entity) {
        return SqlUtil.toBool(this.insertOrUpdate(entity, true));
    }

    default V selectVoById(Serializable id) {
        return selectVoById(id, this.currentVoClass());
    }

    /**
     * 根据 ID 查询
     */
    default <C> C selectVoById(Serializable id, Class<C> voClass) {
        T obj = this.selectOneById(id);
        if (ObjectUtil.isNull(obj)) {
            return null;
        }
        return MapstructUtils.convert(obj, voClass);
    }

    default List<V> selectVoBatchIds(Collection<? extends Serializable> idList) {
        return selectVoBatchIds(idList, this.currentVoClass());
    }

    /**
     * 查询（根据ID 批量查询）
     */
    default <C> List<C> selectVoBatchIds(Collection<? extends Serializable> idList, Class<C> voClass) {
        List<T> list = this.selectListByIds(idList);
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return MapstructUtils.convert(list, voClass);
    }

    default List<V> selectVoByMap(Map<String, Object> map) {
        return selectVoByMap(map, this.currentVoClass());
    }

    /**
     * 查询（根据 columnMap 条件）
     */
    default <C> List<C> selectVoByMap(Map<String, Object> map, Class<C> voClass) {
        List<T> list = this.selectListByQuery(QueryWrapper.create().where(map));
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return MapstructUtils.convert(list, voClass);
    }

    default V selectVoOne(QueryWrapper wrapper) {
        return selectVoOne(wrapper, this.currentVoClass());
    }

    /**
     * 根据 entity 条件，查询一条记录
     */
    default <C> C selectVoOne(QueryWrapper wrapper, Class<C> voClass) {
        T obj = this.selectOneByQuery(wrapper);
        if (ObjectUtil.isNull(obj)) {
            return null;
        }
        return MapstructUtils.convert(obj, voClass);
    }

    default List<V> selectVoList() {
        return selectVoList(QueryWrapper.create(), this.currentVoClass());
    }

    default List<V> selectVoList(QueryWrapper wrapper) {
        return selectVoList(wrapper, this.currentVoClass());
    }

    /**
     * 根据 entity 条件，查询全部记录
     */
    default <C> List<C> selectVoList(QueryWrapper wrapper, Class<C> voClass) {
        List<T> list = this.selectListByQuery(wrapper);
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return MapstructUtils.convert(list, voClass);
    }

    default <P extends Page<V>> P selectVoPage(Page<T> page, QueryWrapper wrapper) {
        return selectVoPage(page, wrapper, this.currentVoClass());
    }

    /**
     * 分页查询VO
     */
    default <C, P extends Page<C>> P selectVoPage(Page<T> page, QueryWrapper wrapper, Class<C> voClass) {
        Page<T> list = paginate(page, wrapper);
        Page<C> voPage = new Page<>(page.getPageNumber(), page.getPageSize(), page.getTotalRow());
        voPage.setRecords(MapstructUtils.convert(list.getRecords(), voClass));
        return (P) voPage;
    }

    default <C> List<C> selectObjs(QueryWrapper wrapper, Function<T, C> mapper) {
        return this.selectListByQuery(wrapper).stream().filter(Objects::nonNull).map(mapper).collect(Collectors.toList());
    }

    /**
     * 删除全部数据
     *
     * @return 删除条数
     */
    default int deleteAll() {
        return this.deleteByQuery(QueryWrapper.create());
    }

    /**
     * 链式查询。
     *
     * @return {@link QueryChain} 对象
     */
    default QueryChain<T> queryChain() {
        return QueryChain.of(this);
    }

    /**
     * 链式更新。
     *
     * @return {@link UpdateChain} 对象
     */
    default UpdateChain<T> updateChain() {
        return UpdateChain.create(this);
    }

}
