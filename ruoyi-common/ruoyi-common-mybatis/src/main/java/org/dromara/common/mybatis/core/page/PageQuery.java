package org.dromara.common.mybatis.core.page;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.ServletUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.sql.SqlUtil;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 分页查询实体类
 *
 * @author Lion Li
 */

@Data
@Accessors(chain = true)
public class PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 分页大小
     */
    private Integer pageSize;

    /**
     * 当前页数
     */
    private Integer pageNum;

    /**
     * 排序列
     */
    private String orderByColumn;

    /**
     * 排序的方向desc或者asc
     */
    private String isAsc;

    /**
     * 查询时是否统计总数
     */
    private boolean isSearchCount = true;

    /**
     * 当前记录起始索引 默认值
     */
    public static final int DEFAULT_PAGE_NUM = 1;

    /**
     * 每页显示记录数 默认值 默认查全部
     */
    public static final int DEFAULT_PAGE_SIZE = Integer.MAX_VALUE;

    /**
     * 使用请求参数设置分页页码
     *
     * @return
     */
    public PageQuery defaultPageNum() {
        pageNum = getRequestPageNum();
        return this;
    }

    /**
     * 使用请求参数设置分页数
     *
     * @return
     */
    public PageQuery defaultPageSize() {
        pageSize = getRequestPageSize();
        return this;
    }

    /**
     * 使用请求参数设置排序字段名称
     *
     * @return
     */
    public PageQuery defaultOrderByColumn() {
        orderByColumn = getRequestOrderByColumn();
        return this;
    }

    /**
     * 使用请求参数设置排序顺序
     *
     * @return
     */
    public PageQuery defaultIsAsc() {
        isAsc = getIsAsc();
        return this;
    }

    /**
     * 查询时默认统计
     *
     * @return
     */
    public PageQuery defaultSearchCount() {
        isSearchCount = true;
        return this;
    }

    /**
     * 使用分页创建对象
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return
     */
    public static PageQuery of(Integer pageNum, Integer pageSize) {
        return of(pageNum, pageSize, true);
    }

    /**
     * 使用分页创建对象
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return
     */
    public static PageQuery of(Integer pageNum, Integer pageSize, boolean isSearchCount) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        pageQuery.setSearchCount(isSearchCount);
        return pageQuery;
    }

    public <T> Page<T> build() {
        Integer pageNum = ObjectUtil.defaultIfNull(getPageNum(), DEFAULT_PAGE_NUM);
        Integer pageSize = ObjectUtil.defaultIfNull(getPageSize(), DEFAULT_PAGE_SIZE);
        if (pageNum <= 0) {
            pageNum = DEFAULT_PAGE_NUM;
        }
        Page<T> page = new Page<>(pageNum, pageSize, isSearchCount);
        List<OrderItem> orderItems = buildOrderItem(orderByColumn, isAsc);
        if (CollUtil.isNotEmpty(orderItems)) {
            page.addOrder(orderItems);
        }
        return page;
    }

    /**
     * 执行方法分页查询
     *
     * @param supplier 查询方法
     * @param <T>
     * @return TableDataInfo
     */
    public <T> TableDataInfo<T> execute(Supplier<List<T>> supplier) {
        Page<T> page = build();
        if (CollUtil.isNotEmpty(page.orders())) {
            List<OrderItem> orderItems = page.orders();
            String orderBy = "";
            if (orderItems != null) {
                orderBy = orderItems
                    .stream()
                    .map(orderItem -> orderItem.getColumn() + (orderItem.isAsc() ? " asc" : " desc"))
                    .collect(Collectors.joining(","));
            }
            try (Closeable close = PageHelper.startPage(pageNum, pageSize, page.searchCount()).setOrderBy(orderBy)) {
                return wrap(supplier);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (Closeable close = PageHelper.startPage(pageNum, pageSize, page.searchCount())) {
                return wrap(supplier);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private <T> TableDataInfo<T> wrap(Supplier<List<T>> supplier) {
        PageInfo<T> pageInfo = new PageInfo<>(supplier.get());
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(HttpStatus.HTTP_OK);
        rspData.setMsg("查询成功");
        rspData.setRows(pageInfo.getList());
        rspData.setTotal(pageInfo.getTotal());
        rspData.setPageNum(pageInfo.getPageNum());
        rspData.setPageSize((long) pageInfo.getPageSize());
        return rspData;
    }

    /**
     * 获取请求中的分页页码
     *
     * @return
     */
    public static Integer getRequestPageNum() {
        return ServletUtils.getParameterToInt(PAGE_NUM, DEFAULT_PAGE_NUM);
    }

    /**
     * 获取请求中的分页数
     *
     * @return
     */
    public static Integer getRequestPageSize() {
        return ServletUtils.getParameterToInt(PAGE_SIZE, DEFAULT_PAGE_SIZE);
    }

    /**
     * 获取请求中的排序字段
     *
     * @return
     */
    public static String getRequestOrderByColumn() {
        return ServletUtils.getParameter(ORDER_BY_COLUMN);
    }

    /**
     * 获取请求中的排序顺序
     *
     * @return
     */
    public static String getRequestIsAsc() {
        return ServletUtils.getParameter(IS_ASC);
    }

    /**
     * 构建排序
     * <p>
     * 支持的用法如下:
     * {isAsc:"asc",orderByColumn:"id"} order by id asc
     * {isAsc:"asc",orderByColumn:"id,createTime"} order by id asc,create_time asc
     * {isAsc:"desc",orderByColumn:"id,createTime"} order by id desc,create_time desc
     * {isAsc:"asc,desc",orderByColumn:"id,createTime"} order by id asc,create_time desc
     */
    private static List<OrderItem> buildOrderItem(String orderByColumn, String isAsc) {
        if (StringUtils.isBlank(orderByColumn) || StringUtils.isBlank(isAsc)) {
            return null;
        }
        String orderBy = SqlUtil.escapeOrderBySql(orderByColumn);
        orderBy = StringUtils.toUnderScoreCase(orderBy);

        // 兼容前端排序类型
        isAsc = StringUtils.replaceEach(isAsc, new String[]{"ascending", "descending"}, new String[]{"asc", "desc"});

        String[] orderByArr = orderBy.split(StringUtils.SEPARATOR);
        String[] isAscArr = isAsc.split(StringUtils.SEPARATOR);
        if (isAscArr.length != 1 && isAscArr.length != orderByArr.length) {
            throw new ServiceException("排序参数有误");
        }

        List<OrderItem> list = new ArrayList<>();
        // 每个字段各自排序
        for (int i = 0; i < orderByArr.length; i++) {
            String orderByStr = StrUtil.toUnderlineCase(orderByArr[i]);
            String isAscStr = isAscArr.length == 1 ? isAscArr[0] : isAscArr[i];
            if ("asc".equals(isAscStr)) {
                list.add(OrderItem.asc(orderByStr));
            } else if ("desc".equals(isAscStr)) {
                list.add(OrderItem.desc(orderByStr));
            } else {
                throw new ServiceException("排序参数有误");
            }
        }
        return list;
    }

    /**
     * mybatis分页插件的使用
     *
     * @param supplier 查询方法
     * @param <T>
     * @return TableDataInfo
     */
    public static <T> TableDataInfo<T> of(Supplier<List<T>> supplier) {
        PageQuery pageQuery = new PageQuery();
        return pageQuery.defaultPageNum()
            .defaultPageSize()
            .defaultOrderByColumn()
            .defaultIsAsc()
            .defaultSearchCount()
            .execute(supplier);
    }

}
