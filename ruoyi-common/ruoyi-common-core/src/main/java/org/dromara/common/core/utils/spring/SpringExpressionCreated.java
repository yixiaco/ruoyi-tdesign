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
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * SpEL表达式创建
 * <p>
 * 文档：<a href="https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/expressions.html">Spring Expression Language (SpEL)</a>
 *
 * @author hexm
 * @date 2024/4/2
 */
@Data
public class SpringExpressionCreated {

    /**
     * spel 解析器
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();
    public static final ParserContext PARSER_CONTEXT = new TemplateParserContext();
    public static final DefaultParameterNameDiscoverer DISCOVERER = new DefaultParameterNameDiscoverer();

    private ExpressionParser expressionParser = PARSER;
    private ParserContext parserContext;
    private EvaluationContext evaluationContext;
    private Object rootObject;

    private SpringExpressionCreated() {
    }

    public static SpringExpressionCreated createTemplate() {
        return createInstance(PARSER_CONTEXT, null, null);
    }

    public static SpringExpressionCreated createStandardTemplate() {
        return createInstance(PARSER_CONTEXT, getStandardEvaluationContext(), null);
    }

    public static SpringExpressionCreated createStandardTemplate(JoinPoint point) {
        return createInstance(PARSER_CONTEXT, getStandardEvaluationContext(getVariable(point)), null);
    }

    public static SpringExpressionCreated createSimpleTemplate(JoinPoint point) {
        return createInstance(PARSER_CONTEXT, getSimpleEvaluationContext(getVariable(point)), null);
    }

    public static SpringExpressionCreated createStandardTemplate(Method method, Object[] args) {
        return createInstance(PARSER_CONTEXT, getStandardEvaluationContext(getVariable(method, args)), null);
    }

    public static SpringExpressionCreated createSimpleTemplate(Method method, Object[] args) {
        return createInstance(PARSER_CONTEXT, getSimpleEvaluationContext(getVariable(method, args)), null);
    }

    public static SpringExpressionCreated createStandardTemplate(Map<String, Object> variable) {
        return createInstance(PARSER_CONTEXT, null, getStandardEvaluationContext(variable));
    }

    public static SpringExpressionCreated createSimpleTemplate(Map<String, Object> variable) {
        return createInstance(PARSER_CONTEXT, null, getSimpleEvaluationContext(variable));
    }

    public static SpringExpressionCreated createStandardTemplate(Object rootObject) {
        return createInstance(PARSER_CONTEXT, getStandardEvaluationContext(), rootObject);
    }

    public static SpringExpressionCreated createSimpleTemplate(Object rootObject) {
        return createInstance(PARSER_CONTEXT, getSimpleEvaluationContext(), rootObject);
    }

    public static SpringExpressionCreated createStandardTemplate(Object rootObject, Map<String, Object> variable) {
        return createInstance(PARSER_CONTEXT, getStandardEvaluationContext(variable), rootObject);
    }

    public static SpringExpressionCreated createSimpleTemplate(Object rootObject, Map<String, Object> variable) {
        return createInstance(PARSER_CONTEXT, getSimpleEvaluationContext(variable), rootObject);
    }

    public static SpringExpressionCreated createTemplate(EvaluationContext evaluationContext) {
        return createInstance(PARSER_CONTEXT, evaluationContext, null);
    }

    public static SpringExpressionCreated createTemplate(EvaluationContext evaluationContext, Object rootObject) {
        return createInstance(PARSER_CONTEXT, evaluationContext, rootObject);
    }

    public static SpringExpressionCreated createStandard() {
        return createInstance(null, getStandardEvaluationContext(), null);
    }

    public static SpringExpressionCreated createStandard(JoinPoint point) {
        return createInstance(null, getStandardEvaluationContext(getVariable(point)), null);
    }

    public static SpringExpressionCreated createSimple(JoinPoint point) {
        return createInstance(null, getSimpleEvaluationContext(getVariable(point)), null);
    }

    public static SpringExpressionCreated createStandard(ParserContext parserContext, JoinPoint point) {
        return createInstance(parserContext, getStandardEvaluationContext(getVariable(point)), null);
    }

    public static SpringExpressionCreated createSimple(ParserContext parserContext, JoinPoint point) {
        return createInstance(parserContext, getSimpleEvaluationContext(getVariable(point)), null);
    }

    public static SpringExpressionCreated createStandard(Method method, Object[] args) {
        return createInstance(null, getStandardEvaluationContext(getVariable(method, args)), null);
    }

    public static SpringExpressionCreated createSimple(Method method, Object[] args) {
        return createInstance(null, getSimpleEvaluationContext(getVariable(method, args)), null);
    }

    public static SpringExpressionCreated createStandard(ParserContext parserContext, Method method, Object[] args) {
        return createInstance(parserContext, getStandardEvaluationContext(getVariable(method, args)), null);
    }

    public static SpringExpressionCreated createSimple(ParserContext parserContext, Method method, Object[] args) {
        return createInstance(parserContext, getSimpleEvaluationContext(getVariable(method, args)), null);
    }

    public static SpringExpressionCreated createStandard(Map<String, Object> variable) {
        return createInstance(null, getStandardEvaluationContext(variable), null);
    }

    public static SpringExpressionCreated createSimple(Map<String, Object> variable) {
        return createInstance(null, getSimpleEvaluationContext(variable), null);
    }

    public static SpringExpressionCreated createStandard(Object rootObject) {
        return createInstance(null, getStandardEvaluationContext(), rootObject);
    }

    public static SpringExpressionCreated createSimple(Object rootObject) {
        return createInstance(null, getSimpleEvaluationContext(), rootObject);
    }

    public static SpringExpressionCreated createStandard(ParserContext parserContext, Object rootObject) {
        return createInstance(parserContext, getStandardEvaluationContext(), rootObject);
    }

    public static SpringExpressionCreated createSimple(ParserContext parserContext, Object rootObject) {
        return createInstance(parserContext, getSimpleEvaluationContext(), rootObject);
    }

    public static SpringExpressionCreated createStandard(Object rootObject, Map<String, Object> variable) {
        return createInstance(null, getStandardEvaluationContext(variable), rootObject);
    }

    public static SpringExpressionCreated createSimple(Object rootObject, Map<String, Object> variable) {
        return createInstance(null, getSimpleEvaluationContext(variable), rootObject);
    }

    public static SpringExpressionCreated createStandard(ParserContext parserContext, Map<String, Object> variable) {
        return createInstance(parserContext, getStandardEvaluationContext(variable), null);
    }

    public static SpringExpressionCreated createSimple(ParserContext parserContext, Map<String, Object> variable) {
        return createInstance(parserContext, getSimpleEvaluationContext(variable), null);
    }

    public static SpringExpressionCreated createStandard(ParserContext parserContext, Object rootObject, Map<String, Object> variable) {
        return createInstance(parserContext, getStandardEvaluationContext(variable), rootObject);
    }

    public static SpringExpressionCreated createSimple(ParserContext parserContext, Object rootObject, Map<String, Object> variable) {
        return createInstance(parserContext, getSimpleEvaluationContext(variable), rootObject);
    }

    public static SpringExpressionCreated createInstance() {
        return createInstance(null, null, null);
    }

    public static SpringExpressionCreated createInstance(Object rootObject) {
        return createInstance(null, null, rootObject);
    }

    public static SpringExpressionCreated createInstance(EvaluationContext evaluationContext) {
        return createInstance(null, evaluationContext, null);
    }

    public static SpringExpressionCreated createInstance(EvaluationContext evaluationContext, Object rootObject) {
        return createInstance(null, evaluationContext, rootObject);
    }

    public static SpringExpressionCreated createInstance(ParserContext parserContext) {
        return createInstance(parserContext, null, null);
    }

    public static SpringExpressionCreated createInstance(ParserContext parserContext, Object rootObject) {
        return createInstance(parserContext, null, rootObject);
    }

    public static SpringExpressionCreated createInstance(ParserContext parserContext, EvaluationContext evaluationContext) {
        return createInstance(parserContext, evaluationContext, null);
    }

    public static SpringExpressionCreated createInstance(ParserContext parserContext, EvaluationContext evaluationContext, Object rootObject) {
        SpringExpressionCreated created = new SpringExpressionCreated();
        created.setEvaluationContext(evaluationContext);
        created.setParserContext(parserContext);
        created.setRootObject(rootObject);
        return created;
    }

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

    public static StandardEvaluationContext getStandardEvaluationContext(Map<String, Object> variable) {
        StandardEvaluationContext context = getStandardEvaluationContext();
        if (variable != null) {
            context.setVariables(variable);
        }
        return context;
    }

    public static SimpleEvaluationContext getSimpleEvaluationContext() {
        return SimpleEvaluationContext.forReadOnlyDataBinding().withInstanceMethods().build();
    }

    public static SimpleEvaluationContext getSimpleEvaluationContext(Map<String, Object> variable) {
        SimpleEvaluationContext context = getSimpleEvaluationContext();
        if (variable != null) {
            variable.forEach(context::setVariable);
        }
        return context;
    }

    public static Map<String, Object> getVariable(JoinPoint point) {
        return getVariable(((MethodSignature) point.getSignature()).getMethod(), point.getArgs());
    }

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

    public Expression getExpression(@Language("SpEL") String expressionString) {
        if (parserContext == null) {
            return expressionParser.parseExpression(expressionString);
        }
        return expressionParser.parseExpression(expressionString, parserContext);
    }

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

    public Character getValueCharacter(@Language("SpEL") String expressionString) {
        return getValue(expressionString, Character.class);
    }

    public String getValueString(@Language("SpEL") String expressionString) {
        return getValue(expressionString, String.class);
    }

    public Boolean getValueBoolean(@Language("SpEL") String expressionString) {
        return getValue(expressionString, Boolean.class);
    }

    public Byte getValueByte(@Language("SpEL") String expressionString) {
        return getValue(expressionString, Byte.class);
    }

    public Short getValueShort(@Language("SpEL") String expressionString) {
        return getValue(expressionString, Short.class);
    }

    public Integer getValueInteger(@Language("SpEL") String expressionString) {
        return getValue(expressionString, Integer.class);
    }

    public Long getValueLong(@Language("SpEL") String expressionString) {
        return getValue(expressionString, Long.class);
    }

    public Double getValueDouble(@Language("SpEL") String expressionString) {
        return getValue(expressionString, Double.class);
    }

    public Float getValueFloat(@Language("SpEL") String expressionString) {
        return getValue(expressionString, Float.class);
    }

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
