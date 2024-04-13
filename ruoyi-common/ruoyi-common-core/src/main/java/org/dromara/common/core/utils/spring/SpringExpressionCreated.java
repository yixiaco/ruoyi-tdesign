package org.dromara.common.core.utils.spring;

import lombok.Data;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.intellij.lang.annotations.Language;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * SpEL表达式创建
 * <p>
 * 文档：<a href="https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/expressions.html">Spring Expression Language (SpEL)</a>
 * <p>
 * example:
 * <pre>{@code
 *  User user = new User();
 *  user.setUsername("admin");
 *  Map<String, Object> variable = new HashMap<>();
 *  variable.put("var", "value");
 *  // 以下示例使用了四种用法
 *  // 使用模板表达式解析器解析，只有在'#{}'中的字符串才属于SpEl表达式，其他字符属于正常字符串
 *  SpringExpressionCreated.createStandardTemplate(user, variable).getValueString("#{username},#{#var},#{1+2},#{@bean.getProperty('property')}"); //admin,value,3,property
 *  // 使用默认的表达式解析器解析，所有字符串都属于SpEl表达式
 *  SpringExpressionCreated.createStandardTemplate(user, variable).getValueString("username + ',' + #var + ',' + (1 + 2) + ',' + @bean.getProperty('property')"); //admin,value,3,property
 *  // 使用简单上下文对象并不会注入Bean，因此不应再表达式中使用Bean对象
 *  SpringExpressionCreated.createSimpleTemplate(user, variable).getValueString("#{username},#{#var},#{1+2}"); //admin,value,3
 *  // 默认值，避免NullPointerException
 *  user.setUsername(null);
 *  SpringExpressionCreated.createStandardTemplate(user).getValueString("username ?: 'admin'"); //admin
 *  SpringExpressionCreated.createStandardTemplate(user).getValueString("username != null ? username : 'admin'"); //admin
 *  SpringExpressionCreated.createStandardTemplate(user).getValueString("username?.toString()"); //null
 * }
 * @author hexm
 * @date 2024/4/2
 * @since 1.3.0
 */
@Data
public class SpringExpressionCreated {

    /**
     * 默认解析器
     */
    private static final ExpressionParser DEFAULT_EXPRESSION_PARSER = new SpelExpressionParser(
        new SpelParserConfiguration(SpelCompilerMode.MIXED, null, true, false, Integer.MAX_VALUE)
    );
    public static final ParserContext PARSER_CONTEXT = new TemplateParserContext();
    public static final DefaultParameterNameDiscoverer DISCOVERER = new DefaultParameterNameDiscoverer();

    private ExpressionParser expressionParser = DEFAULT_EXPRESSION_PARSER;
    private ParserContext parserContext;
    private EvaluationContext evaluationContext;
    private Object rootObject;

    private SpringExpressionCreated() {
    }

    /**
     * 使用默认的模板表达式解析器，解析前缀'#{'和后缀'}'包含的SpEL表达式字符，创建解析表达式实例。
     *
     * <p>
     * example:
     * <pre>{@code
     * '#{objectFieldName}' // objectFieldName是根对象的属性值
     * '#{#var}' // var是Map中的key名称
     * }
     */
    public static SpringExpressionCreated createTemplate() {
        return createInstance(PARSER_CONTEXT, null, null);
    }

    /**
     * 使用默认的模板表达式解析器，解析前缀'#{'和后缀'}'包含的SpEL表达式字符，创建标准的上下文对象数据绑定支持Bean对象的调用的解析表达式实例。
     *
     * <p>
     * example:
     * <pre>{@code
     * '#{objectFieldName}' // objectFieldName是根对象的属性值
     * '#{#var}' // var是Map中的key名称
     * }
     */
    public static SpringExpressionCreated createStandardTemplate() {
        return createInstance(PARSER_CONTEXT, getStandardEvaluationContext(), null);
    }

    /**
     * 使用默认的模板表达式解析器，解析前缀'#{'和后缀'}'包含的SpEL表达式字符，创建标准的上下文对象数据绑定支持Bean对象的调用的解析表达式实例。
     * <p>
     * 切面对象拦截的方法形参与形参值将作为上下文对象中的变量，方法形参数量应与形参值数量一致，否则将抛出异常。
     *
     * <p>
     * example:
     * <pre>{@code
     * '#{objectFieldName}' // objectFieldName是根对象的属性值
     * '#{#var}' // var是Map中的key名称
     * }
     * @param point aop切面对象
     */
    public static SpringExpressionCreated createStandardTemplate(JoinPoint point) {
        return createInstance(PARSER_CONTEXT, getStandardEvaluationContext(getVariable(point)), null);
    }

    /**
     * 使用默认的模板表达式解析器，解析前缀'#{'和后缀'}'包含的SpEL表达式字符，创建简单的上下文对象数据绑定的解析表达式实例。
     * <p>
     * 切面对象拦截的方法形参与形参值将作为上下文对象中的变量，方法形参数量应与形参值数量一致，否则将抛出异常。
     *
     * <p>
     * example:
     * <pre>{@code
     * '#{objectFieldName}' // objectFieldName是根对象的属性值
     * '#{#var}' // var是Map中的key名称
     * }
     * @param point aop切面对象
     */
    public static SpringExpressionCreated createSimpleTemplate(JoinPoint point) {
        return createInstance(PARSER_CONTEXT, getSimpleEvaluationContext(getVariable(point)), null);
    }

    /**
     * 使用默认的模板表达式解析器，解析前缀'#{'和后缀'}'包含的SpEL表达式字符，创建标准的上下文对象数据绑定支持Bean对象的调用的解析表达式实例。
     * <p>
     * 方法形参与形参值将作为上下文对象中的变量，方法形参数量应与形参值数量一致，否则将抛出异常。
     *
     * <p>
     * example:
     * <pre>{@code
     * '#{objectFieldName}' // objectFieldName是根对象的属性值
     * '#{#var}' // var是Map中的key名称
     * }
     * @param method 方法对象
     * @param args   方法形参值
     */
    public static SpringExpressionCreated createStandardTemplate(Method method, Object[] args) {
        return createInstance(PARSER_CONTEXT, getStandardEvaluationContext(getVariable(method, args)), null);
    }

    /**
     * 使用默认的模板表达式解析器，解析前缀'#{'和后缀'}'包含的SpEL表达式字符，创建简单的上下文对象数据绑定的解析表达式实例。
     * <p>
     * 方法形参与形参值将作为上下文对象中的变量，方法形参数量应与形参值数量一致，否则将抛出异常。
     *
     * <p>
     * example:
     * <pre>{@code
     * '#{objectFieldName}' // objectFieldName是根对象的属性值
     * '#{#var}' // var是Map中的key名称
     * }
     * @param method 方法对象
     * @param args   方法形参值
     */
    public static SpringExpressionCreated createSimpleTemplate(Method method, Object[] args) {
        return createInstance(PARSER_CONTEXT, getSimpleEvaluationContext(getVariable(method, args)), null);
    }

    /**
     * 使用默认的模板表达式解析器，解析前缀'#{'和后缀'}'包含的SpEL表达式字符，创建标准的上下文对象数据绑定支持Bean对象的调用的解析表达式实例。
     *
     * <p>
     * example:
     * <pre>{@code
     * '#{objectFieldName}' // objectFieldName是根对象的属性值
     * '#{#var}' // var是Map中的key名称
     * }
     * @param variable      上下文对象中的变量
     */
    public static SpringExpressionCreated createStandardTemplate(Map<String, Object> variable) {
        return createInstance(PARSER_CONTEXT, getStandardEvaluationContext(variable), null);
    }

    /**
     * 使用默认的模板表达式解析器，解析前缀'#{'和后缀'}'包含的SpEL表达式字符，创建简单的上下文对象数据绑定的解析表达式实例。
     *
     * <p>
     * example:
     * <pre>{@code
     * '#{objectFieldName}' // objectFieldName是根对象的属性值
     * '#{#var}' // var是Map中的key名称
     * }
     * @param variable      上下文对象中的变量
     */
    public static SpringExpressionCreated createSimpleTemplate(Map<String, Object> variable) {
        return createInstance(PARSER_CONTEXT, getSimpleEvaluationContext(variable), null);
    }

    /**
     * 使用默认的模板表达式解析器，解析前缀'#{'和后缀'}'包含的SpEL表达式字符，创建标准的上下文对象数据绑定支持Bean对象的调用的解析表达式实例。
     *
     * <p>
     * example:
     * <pre>{@code
     * '#{objectFieldName}' // objectFieldName是根对象的属性值
     * '#{#var}' // var是Map中的key名称
     * }
     * @param rootObject        根对象
     */
    public static SpringExpressionCreated createStandardTemplate(Object rootObject) {
        return createInstance(PARSER_CONTEXT, getStandardEvaluationContext(), rootObject);
    }


    /**
     * 使用默认的模板表达式解析器，解析前缀'#{'和后缀'}'包含的SpEL表达式字符，创建简单的上下文对象数据绑定的解析表达式实例。
     *
     * <p>
     * example:
     * <pre>{@code
     * '#{objectFieldName}' // objectFieldName是根对象的属性值
     * '#{#var}' // var是Map中的key名称
     * }
     * @param rootObject        根对象
     */
    public static SpringExpressionCreated createSimpleTemplate(Object rootObject) {
        return createInstance(PARSER_CONTEXT, getSimpleEvaluationContext(), rootObject);
    }

    /**
     * 使用默认的模板表达式解析器，解析前缀'#{'和后缀'}'包含的SpEL表达式字符，创建标准的上下文对象数据绑定支持Bean对象的调用的解析表达式实例。
     *
     * <p>
     * example:
     * <pre>{@code
     * '#{objectFieldName}' // objectFieldName是根对象的属性值
     * '#{#var}' // var是Map中的key名称
     * }
     * @param rootObject        根对象
     * @param variable      上下文对象中的变量
     */
    public static SpringExpressionCreated createStandardTemplate(Object rootObject, Map<String, Object> variable) {
        return createInstance(PARSER_CONTEXT, getStandardEvaluationContext(variable), rootObject);
    }

    /**
     * 使用默认的模板表达式解析器，解析前缀'#{'和后缀'}'包含的SpEL表达式字符，创建解析表达式实例。
     *
     * <p>
     * example:
     * <pre>{@code
     * '#{objectFieldName}' // objectFieldName是根对象的属性值
     * '#{#var}' // var是Map中的key名称
     * }
     * @param rootObject        根对象
     * @param variable      上下文对象中的变量
     */
    public static SpringExpressionCreated createSimpleTemplate(Object rootObject, Map<String, Object> variable) {
        return createInstance(PARSER_CONTEXT, getSimpleEvaluationContext(variable), rootObject);
    }

    /**
     * 使用默认的模板表达式解析器，解析前缀'#{'和后缀'}'包含的SpEL表达式字符，创建解析表达式实例。
     *
     * <p>
     * example:
     * <pre>{@code
     * '#{objectFieldName}' // objectFieldName是根对象的属性值
     * '#{#var}' // var是Map中的key名称
     * }
     * @param evaluationContext 上下文对象
     */
    public static SpringExpressionCreated createTemplate(EvaluationContext evaluationContext) {
        return createInstance(PARSER_CONTEXT, evaluationContext, null);
    }

    /**
     * 使用默认的模板表达式解析器，解析前缀'#{'和后缀'}'包含的SpEL表达式字符，创建解析表达式实例。
     *
     * <p>
     * example:
     * <pre>{@code
     * '#{objectFieldName}' // objectFieldName是根对象的属性值
     * '#{#var}' // var是Map中的key名称
     * }
     * @param evaluationContext 上下文对象
     * @param rootObject        根对象
     */
    public static SpringExpressionCreated createTemplate(EvaluationContext evaluationContext, Object rootObject) {
        return createInstance(PARSER_CONTEXT, evaluationContext, rootObject);
    }

    /**
     * 使用标准的上下文对象数据绑定支持Bean对象的调用，创建解析表达式实例。
     */
    public static SpringExpressionCreated createStandard() {
        return createInstance(null, getStandardEvaluationContext(), null);
    }

    /**
     * 使用标准的上下文对象数据绑定支持Bean对象的调用，并将切面对象转换注入的方法形参与形参值为上下文中的变量，创建解析表达式实例。
     *
     * @param point aop切面对象
     */
    public static SpringExpressionCreated createStandard(JoinPoint point) {
        return createInstance(null, getStandardEvaluationContext(getVariable(point)), null);
    }

    /**
     * 使用简单的上下文对象数据绑定并将切面对象转换注入的方法形参与形参值为上下文中的变量，创建解析表达式实例。
     *
     * @param point aop切面对象
     */
    public static SpringExpressionCreated createSimple(JoinPoint point) {
        return createInstance(null, getSimpleEvaluationContext(getVariable(point)), null);
    }

    /**
     * 使用标准的上下文对象数据绑定支持Bean对象的调用，并将切面对象转换注入的方法形参与形参值为上下文中的变量，创建解析表达式实例。
     *
     * @param parserContext 表达式分析器
     * @param point         aop切面对象
     */
    public static SpringExpressionCreated createStandard(ParserContext parserContext, JoinPoint point) {
        return createInstance(parserContext, getStandardEvaluationContext(getVariable(point)), null);
    }

    /**
     * 使用简单的上下文对象数据绑定并将切面对象转换注入的方法形参与形参值为上下文中的变量，创建解析表达式实例。
     *
     * @param parserContext 表达式分析器
     * @param point         aop切面对象
     */
    public static SpringExpressionCreated createSimple(ParserContext parserContext, JoinPoint point) {
        return createInstance(parserContext, getSimpleEvaluationContext(getVariable(point)), null);
    }

    /**
     * 使用标准的上下文对象数据绑定支持Bean对象的调用，并将方法形参与参数值转换为上下文中的变量，创建解析表达式实例。
     * 方法形参数量应与形参值数量一致，否则将抛出异常。
     *
     * @param method 方法对象
     * @param args   方法形参值
     */
    public static SpringExpressionCreated createStandard(Method method, Object[] args) {
        return createInstance(null, getStandardEvaluationContext(getVariable(method, args)), null);
    }

    /**
     * 使用简单的上下文对象数据绑定并将方法形参与参数值转换为上下文中的变量，创建解析表达式实例。
     * 方法形参数量应与形参值数量一致，否则将抛出异常。
     *
     * @param method 方法对象
     * @param args   方法形参值
     */
    public static SpringExpressionCreated createSimple(Method method, Object[] args) {
        return createInstance(null, getSimpleEvaluationContext(getVariable(method, args)), null);
    }

    /**
     * 使用标准的上下文对象数据绑定支持Bean对象的调用，并将方法形参与参数值转换为上下文中的变量，创建解析表达式实例。
     * 方法形参数量应与形参值数量一致，否则将抛出异常。
     *
     * @param parserContext 表达式分析器
     * @param method        方法对象
     * @param args          方法形参值
     */
    public static SpringExpressionCreated createStandard(ParserContext parserContext, Method method, Object[] args) {
        return createInstance(parserContext, getStandardEvaluationContext(getVariable(method, args)), null);
    }

    /**
     * 使用简单的上下文对象数据绑定并将方法形参与参数值转换为上下文中的变量，创建解析表达式实例。
     * 方法形参数量应与形参值数量一致，否则将抛出异常。
     *
     * @param parserContext 表达式分析器
     * @param method        方法对象
     * @param args          方法形参值
     */
    public static SpringExpressionCreated createSimple(ParserContext parserContext, Method method, Object[] args) {
        return createInstance(parserContext, getSimpleEvaluationContext(getVariable(method, args)), null);
    }

    /**
     * 使用标准的上下文对象数据绑定支持Bean对象的调用，创建解析表达式实例。
     *
     * @param variable 上下文对象中的变量
     */
    public static SpringExpressionCreated createStandard(Map<String, Object> variable) {
        return createInstance(null, getStandardEvaluationContext(variable), null);
    }

    /**
     * 使用简单的上下文对象数据绑定，创建解析表达式实例。
     *
     * @param variable 上下文对象中的变量
     */
    public static SpringExpressionCreated createSimple(Map<String, Object> variable) {
        return createInstance(null, getSimpleEvaluationContext(variable), null);
    }

    /**
     * 使用标准的上下文对象数据绑定支持Bean对象的调用，创建解析表达式实例。
     *
     * @param rootObject 根对象
     */
    public static SpringExpressionCreated createStandard(Object rootObject) {
        return createInstance(null, getStandardEvaluationContext(), rootObject);
    }

    /**
     * 使用简单的上下文对象数据绑定，创建解析表达式实例。
     *
     * @param rootObject 根对象
     */
    public static SpringExpressionCreated createSimple(Object rootObject) {
        return createInstance(null, getSimpleEvaluationContext(), rootObject);
    }

    /**
     * 使用标准的上下文对象数据绑定支持Bean对象的调用，创建解析表达式实例。
     *
     * @param parserContext 表达式分析器
     * @param rootObject    根对象
     */
    public static SpringExpressionCreated createStandard(ParserContext parserContext, Object rootObject) {
        return createInstance(parserContext, getStandardEvaluationContext(), rootObject);
    }

    /**
     * 使用简单的上下文对象数据绑定，创建解析表达式实例。
     *
     * @param parserContext 表达式分析器
     * @param rootObject    根对象
     */
    public static SpringExpressionCreated createSimple(ParserContext parserContext, Object rootObject) {
        return createInstance(parserContext, getSimpleEvaluationContext(), rootObject);
    }

    /**
     * 使用标准的上下文对象数据绑定支持Bean对象的调用，创建解析表达式实例。
     *
     * @param rootObject 根对象
     * @param variable   上下文对象中的变量
     */
    public static SpringExpressionCreated createStandard(Object rootObject, Map<String, Object> variable) {
        return createInstance(null, getStandardEvaluationContext(variable), rootObject);
    }

    /**
     * 使用简单的上下文对象数据绑定，创建解析表达式实例。
     *
     * @param rootObject 根对象
     * @param variable   上下文对象中的变量
     */
    public static SpringExpressionCreated createSimple(Object rootObject, Map<String, Object> variable) {
        return createInstance(null, getSimpleEvaluationContext(variable), rootObject);
    }

    /**
     * 使用标准的上下文对象数据绑定支持Bean对象的调用，创建解析表达式实例。
     *
     * @param parserContext 表达式分析器
     * @param variable      上下文对象中的变量
     */
    public static SpringExpressionCreated createStandard(ParserContext parserContext, Map<String, Object> variable) {
        return createInstance(parserContext, getStandardEvaluationContext(variable), null);
    }

    /**
     * 使用简单的上下文对象数据绑定，创建解析表达式实例。
     *
     * @param parserContext 表达式分析器
     * @param variable      上下文对象中的变量
     */
    public static SpringExpressionCreated createSimple(ParserContext parserContext, Map<String, Object> variable) {
        return createInstance(parserContext, getSimpleEvaluationContext(variable), null);
    }

    /**
     * 使用标准的上下文对象数据绑定支持Bean对象的调用，创建解析表达式实例。
     *
     * @param parserContext 表达式分析器
     * @param rootObject    根对象
     * @param variable      上下文对象中的变量
     */
    public static SpringExpressionCreated createStandard(ParserContext parserContext, Object rootObject, Map<String, Object> variable) {
        return createInstance(parserContext, getStandardEvaluationContext(variable), rootObject);
    }

    /**
     * 使用简单的上下文对象数据绑定，创建解析表达式实例。
     *
     * @param parserContext 表达式分析器
     * @param rootObject    根对象
     * @param variable      上下文对象中的变量
     */
    public static SpringExpressionCreated createSimple(ParserContext parserContext, Object rootObject, Map<String, Object> variable) {
        return createInstance(parserContext, getSimpleEvaluationContext(variable), rootObject);
    }

    /**
     * 使用默认的解析器，创建解析表达式实例。
     */
    public static SpringExpressionCreated createInstance() {
        return createInstance(null, null, null);
    }

    /**
     * 使用默认的解析器，创建解析表达式实例。
     *
     * @param rootObject 根对象
     */
    public static SpringExpressionCreated createInstance(Object rootObject) {
        return createInstance(null, null, rootObject);
    }

    /**
     * 使用默认的解析器，创建解析表达式实例。
     *
     * @param evaluationContext 上下文对象
     */
    public static SpringExpressionCreated createInstance(EvaluationContext evaluationContext) {
        return createInstance(null, evaluationContext, null);
    }

    /**
     * 使用默认的解析器，创建解析表达式实例。
     *
     * @param evaluationContext 上下文对象
     * @param rootObject        根对象
     */
    public static SpringExpressionCreated createInstance(EvaluationContext evaluationContext, Object rootObject) {
        return createInstance(null, evaluationContext, rootObject);
    }

    /**
     * 创建解析表达式实例。
     *
     * @param parserContext 表达式分析器
     */
    public static SpringExpressionCreated createInstance(ParserContext parserContext) {
        return createInstance(parserContext, null, null);
    }

    /**
     * 创建解析表达式实例。
     *
     * @param parserContext 表达式分析器
     * @param rootObject    根对象
     */
    public static SpringExpressionCreated createInstance(ParserContext parserContext, Object rootObject) {
        return createInstance(parserContext, null, rootObject);
    }

    /**
     * 创建解析表达式实例。
     *
     * @param parserContext     表达式分析器
     * @param evaluationContext 上下文对象
     */
    public static SpringExpressionCreated createInstance(ParserContext parserContext, EvaluationContext evaluationContext) {
        return createInstance(parserContext, evaluationContext, null);
    }

    /**
     * 创建解析表达式实例。
     *
     * @param parserContext     表达式分析器
     * @param evaluationContext 上下文对象
     * @param rootObject        根对象
     */
    public static SpringExpressionCreated createInstance(ParserContext parserContext, EvaluationContext evaluationContext, Object rootObject) {
        SpringExpressionCreated created = new SpringExpressionCreated();
        created.setEvaluationContext(evaluationContext);
        created.setParserContext(parserContext);
        created.setRootObject(rootObject);
        return created;
    }

    /**
     * 功能强大且高度可 EvaluationContext 配置的实现。
     * 此上下文使用所有适用策略的标准实现，基于反射来解析属性、方法和字段。
     */
    public static StandardEvaluationContext getStandardEvaluationContext() {
        StandardEvaluationContext context = new StandardEvaluationContext();
        try {
            ListableBeanFactory factory = SpringUtils.getBeanFactory();
            BeanResolver beanResolver = new BeanFactoryResolver(factory);
            context.setBeanResolver(beanResolver);
        } catch (Exception ignore) {
        }
        return context;
    }

    /**
     * 功能强大且高度可 EvaluationContext 配置的实现。
     * 此上下文使用所有适用策略的标准实现，基于反射来解析属性、方法和字段。
     *
     * @param variable 上下文中的变量
     */
    public static StandardEvaluationContext getStandardEvaluationContext(Map<String, Object> variable) {
        StandardEvaluationContext context = getStandardEvaluationContext();
        if (variable != null) {
            context.setVariables(variable);
        }
        return context;
    }

    /**
     * 其基本实现 EvaluationContext 侧重于基本 SpEL 功能和自定义选项的子集，针对简单的条件评估和特定的数据绑定方案。
     * <p>
     * 在许多情况下，SpEL 语言的完整范围不是必需的，应该有意义地限制。示例包括但不限于数据绑定表达式、基于属性的筛选器等。
     * 为此， SimpleEvaluationContext 它被定制为仅支持 SpEL 语言语法的子集，例如排除对 Java 类型、构造函数和 Bean 引用的引用
     */
    public static SimpleEvaluationContext getSimpleEvaluationContext() {
        return SimpleEvaluationContext.forReadWriteDataBinding().withInstanceMethods().build();
    }

    /**
     * 其基本实现 EvaluationContext 侧重于基本 SpEL 功能和自定义选项的子集，针对简单的条件评估和特定的数据绑定方案。
     * <p>
     * 在许多情况下，SpEL 语言的完整范围不是必需的，应该有意义地限制。示例包括但不限于数据绑定表达式、基于属性的筛选器等。
     * 为此， SimpleEvaluationContext 它被定制为仅支持 SpEL 语言语法的子集，例如排除对 Java 类型、构造函数和 Bean 引用的引用
     *
     * @param variable 上下文中的变量
     */
    public static SimpleEvaluationContext getSimpleEvaluationContext(Map<String, Object> variable) {
        SimpleEvaluationContext context = getSimpleEvaluationContext();
        if (variable != null) {
            variable.forEach(context::setVariable);
        }
        return context;
    }

    /**
     * 一个快速方法，提供Aop拦截方法注入的形参与形参值转为Map对象。
     *
     * @param point aop切面对象
     */
    public static Map<String, Object> getVariable(JoinPoint point) {
        return getVariable(((MethodSignature) point.getSignature()).getMethod(), point.getArgs());
    }

    /**
     * 将方法形参与形参值转换为Map对象。方法形参数量应与形参值数量一致，否则将抛出异常。
     *
     * @param method 方法对象
     * @param args   方法形参值
     */
    public static Map<String, Object> getVariable(Method method, Object[] args) {
        // 获取方法参数名列表
        String[] paramNameArr = DISCOVERER.getParameterNames(method);
        if (paramNameArr != null) {
            Map<String, Object> params = new HashMap<>(paramNameArr.length);
            for (int i = 0; i < paramNameArr.length; i++) {
                String param = paramNameArr[i];
                params.put(param, args[i]);
            }
            return params;
        }
        return new HashMap<>(0);
    }

    /**
     * 使用默认解析器
     */
    public void useDefaultExpressionParse() {
        this.expressionParser = DEFAULT_EXPRESSION_PARSER;
    }

    /**
     * 使用自定义配置的解析器
     *
     * @param compilerMode           使用此配置对象的解析器应使用的编译器模式
     * @param autoGrowNullReferences 如果 null 则使用元素类型的默认构造函数创建元素
     * @param autoGrowCollections    如果集合应该自动增长，如果元素类型没有默认构造函数，null将被添加到数组或列表中
     * @param maximumAutoGrowSize    集合可以自动增长的最大大小
     */
    public void setExpressionParse(@Nullable SpelCompilerMode compilerMode, boolean autoGrowNullReferences, boolean autoGrowCollections, int maximumAutoGrowSize) {
        this.expressionParser = new SpelExpressionParser(new SpelParserConfiguration(compilerMode, null, autoGrowNullReferences, autoGrowCollections, maximumAutoGrowSize));
    }

    /**
     * 使用自定义配置的解析器
     *
     * @param autoGrowNullReferences 如果 null 则使用元素类型的默认构造函数创建元素
     * @param autoGrowCollections    如果集合应该自动增长，如果元素类型没有默认构造函数，null将被添加到数组或列表中
     */
    public void setExpressionParse(boolean autoGrowNullReferences, boolean autoGrowCollections) {
        this.expressionParser = new SpelExpressionParser(new SpelParserConfiguration(autoGrowNullReferences, autoGrowCollections));
    }

    /**
     * 分析表达式字符串并返回可用于重复计算的 Expression 对象
     *
     * @param expressionString 表达式
     */
    public Expression getExpression(@Language("SpEL") String expressionString) {
        if (parserContext == null) {
            return expressionParser.parseExpression(expressionString);
        }
        return expressionParser.parseExpression(expressionString, parserContext);
    }

    /**
     * 获取表达式执行后的值
     *
     * @param expressionString 表达式
     */
    @Nullable
    public Object getValue(@Language("SpEL") String expressionString) {
        Expression expression = getExpression(expressionString);
        if (evaluationContext != null && rootObject != null) {
            return expression.getValue(evaluationContext, rootObject);
        } else if (evaluationContext != null) {
            return expression.getValue(evaluationContext);
        } else if (rootObject != null) {
            return expression.getValue(rootObject);
        }
        return expression.getValue();
    }

    /**
     * 获取表达式执行后的值。如果评估结果与预期结果类型不匹配（并且无法转换为预期结果类型），则将引发异常。
     *
     * @param expressionString  表达式
     * @param desiredResultType 预期结果类型
     */
    @Nullable
    public <T> T getValue(@Language("SpEL") String expressionString, @Nullable Class<T> desiredResultType) {
        Expression expression = getExpression(expressionString);
        if (evaluationContext != null && rootObject != null) {
            return expression.getValue(evaluationContext, rootObject, desiredResultType);
        } else if (evaluationContext != null) {
            return expression.getValue(evaluationContext, desiredResultType);
        } else if (rootObject != null) {
            return expression.getValue(rootObject, desiredResultType);
        }
        return expression.getValue(desiredResultType);
    }

    /**
     * 获取表达式执行后的值，返回char类型值。
     *
     * @param expressionString 表达式
     */
    public Character getValueChar(@Language("SpEL") String expressionString) {
        return getValue(expressionString, Character.class);
    }

    /**
     * 获取表达式执行后的值，返回String类型值。
     *
     * @param expressionString 表达式
     */
    public String getValueString(@Language("SpEL") String expressionString) {
        return getValue(expressionString, String.class);
    }

    /**
     * 获取表达式执行后的值，返回Boolean类型值。
     *
     * @param expressionString 表达式
     */
    public Boolean getValueBoolean(@Language("SpEL") String expressionString) {
        return getValue(expressionString, Boolean.class);
    }

    /**
     * 获取表达式执行后的值，返回Byte类型值。
     *
     * @param expressionString 表达式
     */
    public Byte getValueByte(@Language("SpEL") String expressionString) {
        return getValue(expressionString, Byte.class);
    }

    /**
     * 获取表达式执行后的值，返回Short类型值。
     *
     * @param expressionString 表达式
     */
    public Short getValueShort(@Language("SpEL") String expressionString) {
        return getValue(expressionString, Short.class);
    }

    /**
     * 获取表达式执行后的值，返回Integer类型值。
     *
     * @param expressionString 表达式
     */
    public Integer getValueInt(@Language("SpEL") String expressionString) {
        return getValue(expressionString, Integer.class);
    }

    /**
     * 获取表达式执行后的值，返回Long类型值。
     *
     * @param expressionString 表达式
     */
    public Long getValueLong(@Language("SpEL") String expressionString) {
        return getValue(expressionString, Long.class);
    }

    /**
     * 获取表达式执行后的值，返回Double类型值。
     *
     * @param expressionString 表达式
     */
    public Double getValueDouble(@Language("SpEL") String expressionString) {
        return getValue(expressionString, Double.class);
    }

    /**
     * 获取表达式执行后的值，返回Float类型值。
     *
     * @param expressionString 表达式
     */
    public Float getValueFloat(@Language("SpEL") String expressionString) {
        return getValue(expressionString, Float.class);
    }

    /**
     * 表达式是否正确
     *
     * @param expressionString 表达式
     */
    public boolean isExpressionValid(@Language("SpEL") String expressionString) {
        try {
            getValue(expressionString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 表达式是否与比较的值相等
     *
     * @param expressionString 表达式
     * @param value            比较的值
     */
    @SuppressWarnings("unchecked cast")
    public <T> boolean equalsValue(@Language("SpEL") String expressionString, T value) {
        try {
            T expressionValue = getValue(expressionString, (Class<T>) value.getClass());
            return Objects.equals(expressionValue, value);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 返回类型是否能转为预期结果类型
     *
     * @param expressionString  表达式
     * @param desiredResultType 预期结果类型
     */
    public <T> boolean isValueType(@Language("SpEL") String expressionString, @Nullable Class<T> desiredResultType) {
        try {
            getValue(expressionString, desiredResultType);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 返回可传递给 {@link #setValue(String, Object)} 给定上下文的方法的最常规类型
     *
     * @param expressionString 表达式
     */
    @Nullable
    public Class<?> getValueType(@Language("SpEL") String expressionString) {
        Expression expression = getExpression(expressionString);
        if (evaluationContext != null && rootObject != null) {
            return expression.getValueType(evaluationContext, rootObject);
        } else if (evaluationContext != null) {
            return expression.getValueType(evaluationContext);
        } else if (rootObject != null) {
            return expression.getValueType(rootObject);
        }
        return expression.getValueType();
    }

    /**
     * 返回可以使用默认上下文传递给 {@link #setValue(String, Object)} 方法的最常规类型的描述符。
     *
     * @param expressionString 表达式
     */
    @Nullable
    public TypeDescriptor getValueTypeDescriptor(@Language("SpEL") String expressionString) {
        Expression expression = getExpression(expressionString);
        if (evaluationContext != null && rootObject != null) {
            return expression.getValueTypeDescriptor(evaluationContext, rootObject);
        } else if (evaluationContext != null) {
            return expression.getValueTypeDescriptor(evaluationContext);
        } else if (rootObject != null) {
            return expression.getValueTypeDescriptor(rootObject);
        }
        return expression.getValueTypeDescriptor();
    }

    /**
     * 确定是否可以写入此表达式，即可以调用 {@link #setValue(String, Object)}。
     *
     * @param expressionString 表达式
     */
    public boolean isWritable(@Language("SpEL") String expressionString) {
        Expression expression = getExpression(expressionString);
        if (evaluationContext != null && rootObject != null) {
            return expression.isWritable(evaluationContext, rootObject);
        } else if (evaluationContext != null) {
            return expression.isWritable(evaluationContext);
        } else if (rootObject != null) {
            return expression.isWritable(rootObject);
        }
        return false;
    }

    /**
     * 在提供的上下文中将此表达式设置为提供的值。
     *
     * @param expressionString 表达式
     */
    public void setValue(@Language("SpEL") String expressionString, @Nullable Object value) {
        Expression expression = getExpression(expressionString);
        if (evaluationContext != null && rootObject != null) {
            expression.setValue(evaluationContext, rootObject, value);
        } else if (evaluationContext != null) {
            expression.setValue(evaluationContext, value);
        } else if (rootObject != null) {
            expression.setValue(rootObject, value);
        }
    }
}
