package org.dromara.common.mybatis.core.page;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.ServletUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.sql.SqlUtil;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 排序查询实体类
 *
 * @author YiXiacoco
 */
@Data
@Accessors(chain = true)
public class SortQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 格式安全正则
     */
    private static final Pattern SAFE_SORT = Pattern.compile("^(?:[a-zA-Z]+\\.)?[a-zA-Z0-9_]+$");

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 排序列
     */
    private String orderByColumn;

    /**
     * 排序的方向desc或者asc
     */
    private String isAsc;

    /**
     * 使用请求参数设置排序字段名称
     *
     * @return
     */
    public SortQuery defaultOrderByColumn() {
        orderByColumn = getRequestOrderByColumn();
        return this;
    }

    /**
     * 使用请求参数设置排序顺序
     *
     * @return
     */
    public SortQuery defaultIsAsc() {
        isAsc = getRequestIsAsc();
        return this;
    }

    /**
     * 使用排序创建对象
     *
     * @return
     */
    public static SortQuery of() {
        SortQuery sortQuery = new SortQuery();
        return sortQuery
            .defaultOrderByColumn()
            .defaultIsAsc();
    }

    public List<OrderItem> build() {
        return buildOrderItem(orderByColumn, isAsc);
    }

    /**
     * 获取排序字符
     *
     * @param orders 排序对象
     * @return
     */
    public static String getOrderBy(List<OrderItem> orders) {
        return orders
            .stream()
            .map(orderItem -> orderItem.getColumn() + (orderItem.isAsc() ? " asc" : " desc"))
            .collect(Collectors.joining(","));
    }

    /**
     * 执行方法排序查询
     *
     * @param supplier 查询方法
     * @param <T>
     * @return TableDataInfo
     */
    public <T> List<T> execute(Supplier<List<T>> supplier) {
        List<OrderItem> orders = build();
        if (CollUtil.isNotEmpty(orders)) {
            String orderBy = getOrderBy(orders);
            PageHelper.orderBy(orderBy);
            try {
                return supplier.get();
            } finally {
                PageHelper.clearPage();
            }
        } else {
            return supplier.get();
        }
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
    public static List<OrderItem> buildOrderItem(String orderByColumn, String isAsc) {
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
            // 检查是否包含特殊字符
            String sortBy = orderByArr[i];
            boolean check = sortSafeCheck(sortBy);
            if (!check) {
                throw new ServiceException("非法的排序字段名称");
            }
            String orderByStr = StrUtil.toUnderlineCase(sortBy);
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
     * 排序字段安全检查
     *
     * @param sortBy 排序字段
     * @return
     */
    private static boolean sortSafeCheck(String sortBy) {
        return SAFE_SORT.matcher(sortBy).matches();
    }

    /**
     * mybatis排序插件的使用
     *
     * @param supplier 查询方法
     * @param <T>
     * @return TableDataInfo
     */
    public static <T> List<T> of(Supplier<List<T>> supplier) {
        return of().execute(supplier);
    }

}
