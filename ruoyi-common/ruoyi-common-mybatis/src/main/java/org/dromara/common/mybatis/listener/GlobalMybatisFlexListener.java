package org.dromara.common.mybatis.listener;

import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;
import com.mybatisflex.core.FlexGlobalConfig;
import org.dromara.common.core.domain.model.BaseUser;
import org.dromara.common.core.utils.ServletUtils;
import org.dromara.common.core.utils.ip.AddressUtils;
import org.dromara.common.mybatis.annotation.ColumnFill;
import org.dromara.common.mybatis.enums.DateType;
import org.dromara.common.mybatis.enums.FillType;
import org.dromara.common.satoken.context.SaSecurityContext;
import org.dromara.common.satoken.utils.LoginHelper;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.function.Supplier;

/**
 * 抽象数据填充
 *
 * @author hexm
 * @date 2024/03/29 16:02
 */
public class GlobalMybatisFlexListener implements InsertListener, UpdateListener {

    public static final String ZERO = "0";
    public static final String ONE = "1";

    /**
     * 新增操作的前置操作。
     *
     * @param entity 实体类
     */
    @Override
    public void onInsert(Object entity) {
        ReflectionUtils.doWithFields(entity.getClass(), field -> {
            if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                return;
            }
            ReflectionUtils.makeAccessible(field);
            // 插入时，如果字段不为null，则不执行插入操作
            if (field.get(entity) != null) {
                return;
            }
            ColumnFill annotation = AnnotatedElementUtils.getMergedAnnotation(field, ColumnFill.class);
            if (annotation != null && (annotation.fillType() == FillType.INSERT || annotation.fillType() == FillType.INSERT_UPDATE)) {
                process(entity, field, annotation);
            }
        });
    }

    /**
     * 更新操作的前置操作。
     *
     * @param entity 实体类
     */
    @Override
    public void onUpdate(Object entity) {
        ReflectionUtils.doWithFields(entity.getClass(), field -> {
            if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                return;
            }
            ColumnFill annotation = AnnotatedElementUtils.getMergedAnnotation(field, ColumnFill.class);
            if (annotation != null && (annotation.fillType() == FillType.UPDATE || annotation.fillType() == FillType.INSERT_UPDATE)) {
                process(entity, field, annotation);
            }
        });
    }

    /**
     * 新增操作的前置操作
     *
     * @param entity 实体类
     * @param field  字段
     * @param fill   填充注解信息
     */
    protected void process(Object entity, Field field, ColumnFill fill) {
        DateType dateType = fill.dateType();
        switch (dateType) {
            case DATE -> {
                setValue(entity, field, Date.class, Date::new);
                setValue(entity, field, Long.class, System::currentTimeMillis);
                setValue(entity, field, LocalDateTime.class, LocalDateTime::now);
                setValue(entity, field, LocalDate.class, LocalDate::now);
                setValue(entity, field, LocalTime.class, LocalTime::now);
                setValue(entity, field, Instant.class, Instant::now);
                setValue(entity, field, Timestamp.class, () -> Timestamp.from(Instant.now()));
            }
            case USER_ID -> {
                setValue(entity, field, String.class, () -> getLoginId() == null ? null : getLoginId().toString());
                setValue(entity, field, Long.class, GlobalMybatisFlexListener::getLoginId);
            }
            case USERNAME -> setValue(entity, field, String.class, GlobalMybatisFlexListener::getLoginUsername);
            case DEPT_ID -> setValue(entity, field, Long.class, GlobalMybatisFlexListener::getDeptId);
            case IP -> setValue(entity, field, String.class, ServletUtils::getClientIP);
            case IP_ADDRESS ->
                setValue(entity, field, String.class, () -> AddressUtils.getRealAddressByIP(ServletUtils.getClientIP()));
            case ZERO -> {
                setValue(entity, field, String.class, () -> ZERO);
                setValue(entity, field, Long.class, () -> 0L);
                setValue(entity, field, Integer.class, () -> 0);
                setValue(entity, field, Short.class, () -> (short) 0);
                setValue(entity, field, Byte.class, () -> (byte) 0);
                setValue(entity, field, Character.class, () -> '0');
            }
            case ONE -> {
                setValue(entity, field, String.class, () -> ONE);
                setValue(entity, field, Long.class, () -> 1L);
                setValue(entity, field, Integer.class, () -> 1);
                setValue(entity, field, Short.class, () -> (short) 1);
                setValue(entity, field, Byte.class, () -> (byte) 1);
                setValue(entity, field, Character.class, () -> '1');
            }
            case LOGIC_DELETE -> {
                Object logicDelete = FlexGlobalConfig.getDefaultConfig().getDeletedValueOfLogicDelete();
                setValue(entity, field, String.class, logicDelete::toString);
                setValue(entity, field, Long.class, () -> Long.parseLong(logicDelete.toString()));
                setValue(entity, field, Integer.class, () -> Integer.parseInt(logicDelete.toString()));
                setValue(entity, field, Short.class, () -> Short.parseShort(logicDelete.toString()));
                setValue(entity, field, Byte.class, () -> Byte.parseByte(logicDelete.toString()));
                setValue(entity, field, Character.class, () -> logicDelete.toString().charAt(0));
            }
            case LOGIC_NOT_DELETE -> {
                Object logicDelete = FlexGlobalConfig.getDefaultConfig().getNormalValueOfLogicDelete();
                setValue(entity, field, String.class, logicDelete::toString);
                setValue(entity, field, Long.class, () -> Long.parseLong(logicDelete.toString()));
                setValue(entity, field, Integer.class, () -> Integer.parseInt(logicDelete.toString()));
                setValue(entity, field, Short.class, () -> Short.parseShort(logicDelete.toString()));
                setValue(entity, field, Byte.class, () -> Byte.parseByte(logicDelete.toString()));
                setValue(entity, field, Character.class, () -> logicDelete.toString().charAt(0));
            }
        }
    }

    /**
     * 赋值操作
     *
     * @param entity      实体类
     * @param field       字段
     * @param targetClass 目标类型
     * @param getValue    获取值
     */
    protected static <T> void setValue(Object entity, Field field, Class<T> targetClass, Supplier<T> getValue) {
        if (field.getDeclaringClass().isAssignableFrom(targetClass)) {
            ReflectionUtils.setField(field, entity, getValue.get());
        }
    }

    /**
     * 获取部门id
     */
    protected static Long getDeptId() {
        return LoginHelper.getDeptId();
    }

    /**
     * 获取登录用户名
     */
    protected static String getLoginUsername() {
        return SaSecurityContext.getContentOptional().map(BaseUser::getUsername).orElse(null);
    }

    /**
     * 获取登录用户id
     */
    protected static Long getLoginId() {
        return SaSecurityContext.getContentOptional().map(BaseUser::getUserId).orElse(null);
    }
}
