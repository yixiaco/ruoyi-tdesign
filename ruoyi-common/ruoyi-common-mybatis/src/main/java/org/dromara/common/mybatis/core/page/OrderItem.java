package org.dromara.common.mybatis.core.page;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 排序元素载体
 *
 * @author HCL
 * Create at 2019/5/27
 */
@Getter
@Setter
public class OrderItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字符串去除空白内容
     *
     * <ul> <li>'"<>&*+=#-; sql注入黑名单</li> <li>\n 回车</li> <li>\t 水平制表符</li> <li>\s 空格</li> <li>\r 换行</li> </ul>
     */
    private static final Pattern REPLACE_BLANK = Pattern.compile("'|\"|<|>|&|\\*|\\+|=|#|-|;|\\s*|\t|\r|\n");

    /**
     * 需要进行排序的字段
     */
    private String column;
    /**
     * 是否正序排列，默认 true
     */
    private boolean asc = true;

    public static OrderItem asc(String column) {
        return build(column, true);
    }

    public static OrderItem desc(String column) {
        return build(column, false);
    }

    public static List<OrderItem> ascs(String... columns) {
        return Arrays.stream(columns).map(OrderItem::asc).collect(Collectors.toList());
    }

    public static List<OrderItem> descs(String... columns) {
        return Arrays.stream(columns).map(OrderItem::desc).collect(Collectors.toList());
    }

    private static OrderItem build(String column, boolean asc) {
        return new OrderItem().setColumn(column).setAsc(asc);
    }

    public OrderItem setColumn(String column) {
        Matcher matcher = REPLACE_BLANK.matcher(column);
        this.column = matcher.replaceAll("");
        return this;
    }

    public OrderItem setAsc(boolean asc) {
        this.asc = asc;
        return this;
    }
}
