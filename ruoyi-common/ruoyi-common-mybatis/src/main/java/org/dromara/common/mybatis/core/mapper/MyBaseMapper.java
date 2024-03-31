package org.dromara.common.mybatis.core.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.core.update.UpdateWrapper;
import com.mybatisflex.core.util.ClassUtil;
import com.mybatisflex.core.util.SqlUtil;
import com.mybatisflex.core.util.UpdateEntity;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.dromara.common.core.utils.MapstructUtils;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.ReflectionUtils;

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
 * @author hexm
 * @since 2024-03-30
 */
@SuppressWarnings("unchecked")
public interface MyBaseMapper<T> extends BaseMapper<T> {

    Log log = LogFactory.getLog(MyBaseMapper.class);

    default Class<T> currentModelClass() {
        return (Class<T>) Objects.requireNonNull(GenericTypeResolver.resolveTypeArguments(getClass(), MyBaseMapper.class))[0];
    }

    default List<T> selectList() {
        return selectListByQuery(QueryWrapper.create());
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
        Class<BaseMapper<T>> usefulClass = (Class<BaseMapper<T>>) ClassUtil.getUsefulClass(getClass());
        return SqlUtil.toBool(Db.executeBatch(entities, batchSize, usefulClass, BaseMapper::insertSelective));
    }

    /**
     * 批量更新(包含限制条数)
     */
    default boolean updateBatchById(Collection<T> entities, int batchSize) {
        Class<BaseMapper<T>> usefulClass = (Class<BaseMapper<T>>) ClassUtil.getUsefulClass(getClass());
        return SqlUtil.toBool(Db.executeBatch(entities, batchSize, usefulClass, BaseMapper::update));
    }

    /**
     * 批量插入或更新(包含限制条数)
     */
    default boolean insertOrUpdateBatch(Collection<T> entities, int batchSize) {
        Class<BaseMapper<T>> usefulClass = (Class<BaseMapper<T>>) ClassUtil.getUsefulClass(getClass());
        return SqlUtil.toBool(Db.executeBatch(entities, batchSize, usefulClass, BaseMapper::insertOrUpdateSelective));
    }

    /**
     * 根据 Wrapper 条件，判断是否存在记录
     *
     * @param queryWrapper 实体对象封装操作类
     * @return 是否存在记录
     */
    default boolean exists(QueryWrapper queryWrapper) {
        long count = selectCountByQuery(queryWrapper);
        return count > 0;
    }

    /**
     * 插入或更新(包含限制条数)
     */
    default boolean saveOrUpdate(T entity) {
        return SqlUtil.toBool(insertOrUpdate(entity, true));
    }

    /**
     * 根据 ID 查询
     */
    default <C> C selectVoById(Serializable id, Class<C> voClass) {
        T obj = selectOneById(id);
        if (ObjectUtil.isNull(obj)) {
            return null;
        }
        return MapstructUtils.convert(obj, voClass);
    }

    /**
     * 查询（根据ID 批量查询）
     */
    default <C> List<C> selectVoBatchIds(Collection<? extends Serializable> idList, Class<C> voClass) {
        List<T> list = selectListByIds(idList);
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return MapstructUtils.convert(list, voClass);
    }

    /**
     * 查询（根据 columnMap 条件）
     */
    default <C> List<C> selectVoByMap(Map<String, Object> map, Class<C> voClass) {
        List<T> list = selectListByQuery(QueryWrapper.create().where(map));
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return MapstructUtils.convert(list, voClass);
    }

    /**
     * 根据 entity 条件，查询一条记录
     */
    default <C> C selectVoOne(QueryWrapper wrapper, Class<C> voClass) {
        T obj = selectOneByQuery(wrapper);
        if (ObjectUtil.isNull(obj)) {
            return null;
        }
        return MapstructUtils.convert(obj, voClass);
    }

    /**
     * 根据 entity 条件，查询全部记录
     */
    default <C> List<C> selectVoList(QueryWrapper wrapper, Class<C> voClass) {
        List<T> list = selectListByQuery(wrapper);
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return MapstructUtils.convert(list, voClass);
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
        return selectListByQuery(wrapper).stream().filter(Objects::nonNull).map(mapper).collect(Collectors.toList());
    }

    /**
     * 删除全部数据
     *
     * @return 删除条数
     */
    default int deleteAll() {
        return deleteByQuery(QueryWrapper.create());
    }

    /**
     * 更新
     *
     * @param wrapper 更新操作对象
     * @return 更新数量
     */
    default int update(UpdateWrapper<T> wrapper) {
        return update(wrapper.toEntity());
    }

    default int update(UpdateWrapper<T> updateWrapper, QueryWrapper queryWrapper) {
        return updateByQuery(updateWrapper.toEntity(), queryWrapper);
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

    /**
     * 更新操作对象
     */
    default UpdateWrapper<T> getUpdateWrapper() {
        return UpdateWrapper.of(currentModelClass());
    }

    /**
     * 转换为更新操作对象
     */
    default UpdateWrapper<T> getUpdateWrapper(T entity) {
        return UpdateWrapper.of(entity);
    }

    /**
     * 更新对象，调用set方法会null时，不会被忽略
     */
    default T getUpdateEntity() {
        return UpdateEntity.of(currentModelClass());
    }

    /**
     * 更新对象，并设置主键
     * 更新对象调用set方法会null时，不会被忽略
     */
    default T getUpdateEntity(Object id) {
        return UpdateEntity.of(currentModelClass(), id);
    }

    /**
     * 转为更新对象
     * 初始化时属性为null会被过滤，转换后的更新对象调用set方法会null时，不会被忽略
     *
     * @param entity 对象
     */
    default T getUpdateEntityNotNull(T entity) {
        return UpdateEntity.ofNotNull(entity);
    }

}
