package org.dromara.common.mybatis.core.page;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
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
import java.util.StringJoiner;
import java.util.function.Supplier;
import java.util.regex.Pattern;

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
    private static final Pattern SAFE_SORT = Pattern.compile("^([a-zA-Z0-9_]+\\.)?[a-zA-Z0-9_]+$");

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 排序
     */
    private final List<OrderItem> orderItems = new ArrayList<>();

    /**
     * 清除排序条件
     */
    public SortQuery clear() {
        orderItems.clear();
        return this;
    }

    /**
     * 使用请求参数设置排序
     */
    public SortQuery defaultOrder() {
        orderItems.clear();
        String orderByColumn = getRequestOrderByColumn();
        String isAsc = getRequestIsAsc();
        List<OrderItem> itemList = buildOrderItem(orderByColumn, isAsc);
        if (itemList != null) {
            orderItems.addAll(itemList);
        }
        return this;
    }

    /**
     * 添加排序字段
     *
     * @param orderByColumn 排序字段
     */
    public SortQuery addOrderByColumn(String orderByColumn) {
        if (StrUtil.isNotBlank(orderByColumn)) {
            this.orderItems.add(OrderItem.asc(orderByColumn));
        }
        return this;
    }

    /**
     * 添加排序字段
     *
     * @param orderByColumn 排序字段
     * @param isAsc         是否升序
     */
    public SortQuery addOrderByColumn(String orderByColumn, boolean isAsc) {
        if (StrUtil.isNotBlank(orderByColumn)) {
            this.orderItems.add(isAsc ? OrderItem.asc(orderByColumn) : OrderItem.desc(orderByColumn));
        }
        return this;
    }

    /**
     * 使用默认的请求参数排序创建对象
     */
    public static SortQuery of() {
        SortQuery sortQuery = new SortQuery();
        return sortQuery.defaultOrder();
    }

    /**
     * 使用升序排序创建对象
     */
    public static SortQuery of(String orderByColumn) {
        SortQuery sortQuery = new SortQuery();
        return sortQuery.addOrderByColumn(orderByColumn);
    }

    /**
     * 使用排序创建对象
     */
    public static SortQuery of(String orderByColumn, boolean isAsc) {
        SortQuery sortQuery = new SortQuery();
        return sortQuery.addOrderByColumn(orderByColumn, isAsc);
    }

    /**
     * 获取排序字符
     *
     * @param orders 排序对象
     * @return
     */
    public static String getOrderBy(List<OrderItem> orders) {
        StringJoiner joiner = new StringJoiner(",");
        for (OrderItem order : orders) {
            joiner.add(order.getColumn() + (order.isAsc() ? " asc" : " desc"));
        }
        return joiner.toString();
    }

    /**
     * 执行方法排序查询
     *
     * @param supplier 查询方法
     * @param <T>
     * @return TableDataInfo
     */
    public <T> List<T> execute(Supplier<List<T>> supplier) {
        if (CollUtil.isNotEmpty(orderItems)) {
            String orderBy = getOrderBy(orderItems);
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
