package org.dromara.system.domain.vo;

import org.dromara.system.domain.SysMessageKey;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import java.util.Date;
import java.io.Serial;
import java.io.Serializable;

/**
 * 消息常量视图对象 sys_message_key
 *
 * @author hexm
 * @date 2023-06-28
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SysMessageKey.class)
public class SysMessageKeyVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息key主键
     */
    @ExcelProperty(value = "消息key主键")
    private Long messageKeyId;

    /**
     * 消息名称
     */
    @ExcelProperty(value = "消息名称")
    private String name;

    /**
     * 消息key
     */
    @ExcelProperty(value = "消息key")
    private String messageKey;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

}
