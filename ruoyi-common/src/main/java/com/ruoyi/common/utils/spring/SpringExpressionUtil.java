package com.ruoyi.common.utils.spring;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * EL表达式工具
 *
 * @author hexm
 * @date 2023/02/13 11:25
 */
public class SpringExpressionUtil {

    /**
     * spel 解析器
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();
    private static final ParserContext PARSER_CONTEXT = new TemplateParserContext();

    /**
     * 解析AOP表达式
     *
     * @param expressionString 表达式
     * @param point            织入点
     * @return
     */
    public static String parseAspectExpression(String expressionString, JoinPoint point) {
        return parseClassExpression(expressionString, ((MethodSignature) point.getSignature()).getMethod(), point.getArgs());
    }

    /**
     * 解析方法
     *
     * @param expressionString 表达式
     * @param method           方法对象
     * @param args             方法形参值
     * @return
     */
    public static String parseClassExpression(String expressionString, Method method, Object[] args) {
        //获取方法参数名列表
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNameArr = discoverer.getParameterNames(method);
        Map<String, Object> variable = new HashMap<>();
        if (paramNameArr != null) {
            for (int i = 0; i < paramNameArr.length; i++) {
                String param = paramNameArr[i];
                variable.put(param, args[i]);
            }
        }
        return parseExpression(expressionString, variable);
    }

    /**
     * 解析EL表达式
     *
     * @param expressionString 表达式
     * @param variable         变量
     * @return
     */
    public static String parseExpression(String expressionString, Map<String, Object> variable) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        BeanResolver beanResolver = new BeanFactoryResolver(SpringUtils.getBeanFactory());
        context.setBeanResolver(beanResolver);
        context.setVariables(variable);
        return PARSER.parseExpression(expressionString, PARSER_CONTEXT).getValue(context, String.class);
    }
}
