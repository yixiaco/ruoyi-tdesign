package com.ruoyi.common.core.web.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * Tree基类
 *
 * @author Lion Li
 */

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class TreeEntity<T> extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 父节点名称
     */
    @TableField(exist = false)
    private String parentName;

    /**
     * 子节点
     */
    @TableField(exist = false)
    private List<T> children = new ArrayList<>();
}
