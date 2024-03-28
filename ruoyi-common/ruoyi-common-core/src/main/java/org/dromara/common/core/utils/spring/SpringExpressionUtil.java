package org.dromara.common.core.utils.spring;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.intellij.lang.annotations.Language;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * EL表达式解析工具
 * <p>
 * 文档：<a href="https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/expressions.html">Spring Expression Language (SpEL)</a>
 *
 * @author hexm
 * @date 2023/02/13 11:25
 */
public class SpringExpressionUtil {

    /**
     * spel 解析器
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();
    public static final ParserContext PARSER_CONTEXT = new TemplateParserContext();
    public static final DefaultParameterNameDiscoverer DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * 使用字符串模板解析AOP表达式，使用默认的“#{”前缀和“}”后缀的字符表达式解析
     * <p>
     * example:
     * <pre>{@code
     *      SpringExpressionUtil.parseAspectTemplateExpression("template: #{#var}", point);
     * }</pre>
     *
     * @param expressionString 表达式
     * @param point            织入点
     * @return
     */
    public static String parseAspectTemplateExpression(@Language("SpEL") String expressionString, JoinPoint point) {
        return parseClassTemplateExpression(expressionString, ((MethodSignature) point.getSignature()).getMethod(), point.getArgs());
    }

    /**
     * 解析AOP表达式
     * <p>
     * example:
     * <pre>{@code
     *      SpringExpressionUtil.parseAspectExpression("#var", point);
     * }</pre>
     *
     * @param expressionString 表达式
     * @param point            织入点
     * @return
     */
    public static String parseAspectExpression(@Language("SpEL") String expressionString, JoinPoint point) {
        return parseClassExpression(expressionString, ((MethodSignature) point.getSignature()).getMethod(), point.getArgs());
    }

    /**
     * 使用自定义的{@link ParserContext}解析AOP表达式
     * <p>
     * example:
     * <pre>{@code
     *      SpringExpressionUtil.parseAspectExpression("#{#var}", SpringExpressionUtil.PARSER_CONTEXT, point);
     * }</pre>
     *
     * @param expressionString 表达式
     * @param parserContext    提供给表达式分析器的输入，设置表达式解析的前后缀
     * @param point            织入点
     * @return
     */
    public static String parseAspectExpression(@Language("SpEL") String expressionString, ParserContext parserContext, JoinPoint point) {
        return parseClassExpression(expressionString, parserContext, ((MethodSignature) point.getSignature()).getMethod(), point.getArgs());
    }

    /**
     * 使用字符串模板解析EL表达式，变量名称从方法形参中获取，变量值使用args注入，使用默认的“#{”前缀和“}”后缀的字符表达式解析
     * <p>
     * example:
     * <pre>{@code
     *      SpringExpressionUtil.parseClassTemplateExpression("#{#var}", method, args);
     * }</pre>
     *
     * @param expressionString 表达式
     * @param method           方法对象
     * @param args             方法形参值
     * @return
     */
    public static String parseClassTemplateExpression(@Language("SpEL") String expressionString, Method method, Object[] args) {
        return parseClassExpression(expressionString, PARSER_CONTEXT, method, args);
    }

    /**
     * 解析EL表达式，变量名称从方法形参中获取，变量值使用args注入
     * <p>
     * example:
     * <pre>{@code
     *      SpringExpressionUtil.parseClassExpression("#var", method, args);
     * }</pre>
     *
     * @param expressionString 表达式
     * @param method           方法对象
     * @param args             方法形参值
     * @return
     */
    public static String parseClassExpression(@Language("SpEL") String expressionString, Method method, Object[] args) {
        return parseClassExpression(expressionString, null, method, args);
    }

    /**
     * 使用自定义的{@link ParserContext}解析EL表达式，变量名称从方法形参中获取，变量值使用args注入
     * <p>
     * example:
     * <pre>{@code
     *      SpringExpressionUtil.parseClassExpression("#{#var}", SpringExpressionUtil.PARSER_CONTEXT, method, args);
     * }</pre>
     *
     * @param expressionString 表达式
     * @param parserContext    提供给表达式分析器的输入，设置表达式解析的前后缀
     * @param method           方法对象
     * @param args             方法形参值
     * @return
     */
    public static String parseClassExpression(@Language("SpEL") String expressionString, ParserContext parserContext, Method method, Object[] args) {
        //获取方法参数名列表
        String[] paramNameArr = DISCOVERER.getParameterNames(method);
        Map<String, Object> variable;
        if (paramNameArr != null) {
            variable = new HashMap<>(paramNameArr.length);
            for (int i = 0; i < paramNameArr.length; i++) {
                String param = paramNameArr[i];
                variable.put(param, args[i]);
            }
        } else {
            variable = new HashMap<>(0);
        }
        return parseExpression(expressionString, parserContext, variable);
    }

    /**
     * 使用字符串模板解析EL表达式，使用默认的“#{”前缀和“}”后缀的字符表达式解析
     * <p>
     * example:
     * <pre>{@code
     *      Map<String, Object> variable = new HashMap<>();
     *      variable.put("var", "value");
     *      SpringExpressionUtil.parseTemplateExpression("#{#var}", variable); //value
     * }</pre>
     *
     * @param expressionString 表达式
     * @param variable         变量
     * @return
     */
    public static String parseTemplateExpression(@Language("SpEL") String expressionString, Map<String, Object> variable) {
        return parseExpression(expressionString, PARSER_CONTEXT, variable);
    }

    /**
     * 解析EL表达式
     * <p>
     * example:
     * <pre>{@code
     *      Map<String, Object> variable = new HashMap<>();
     *      variable.put("var", "value");
     *      SpringExpressionUtil.parseExpression("#var", variable); //value
     * }</pre>
     *
     * @param expressionString 表达式
     * @param variable         变量
     * @return
     */
    public static String parseExpression(@Language("SpEL") String expressionString, Map<String, Object> variable) {
        return parseExpression(expressionString, null, null, variable);
    }

    /**
     * 使用自定义的{@link ParserContext}解析EL表达式
     * <p>
     * example:
     * <pre>{@code
     *      Map<String, Object> variable = new HashMap<>();
     *      variable.put("var", "value");
     *      SpringExpressionUtil.parseExpression("#{#var}", SpringExpressionUtil.PARSER_CONTEXT, variable); //value
     * }</pre>
     *
     * @param expressionString 表达式
     * @param parserContext    提供给表达式分析器的输入，设置表达式解析的前后缀
     * @param variable         变量
     * @return
     */
    public static String parseExpression(@Language("SpEL") String expressionString, ParserContext parserContext, Map<String, Object> variable) {
        return parseExpression(expressionString, parserContext, null, variable);
    }

    /**
     * 使用字符串模板解析EL表达式，对象属性和方法将作为根对象，使用默认的“#{”前缀和“}”后缀的字符表达式解析
     * <p>
     * example:
     * <pre>{@code
     *      User user = new User();
     *      user.setUsername("admin");
     *      SpringExpressionUtil.parseTemplateExpression("#{username}", user); //admin
     * }</pre>
     *
     * @param expressionString 表达式
     * @param rootObject       根对象
     * @return
     */
    public static String parseTemplateExpression(@Language("SpEL") String expressionString, Object rootObject) {
        return parseExpression(expressionString, PARSER_CONTEXT, rootObject);
    }

    /**
     * 解析EL表达式，对象属性和方法将作为根对象
     * <p>
     * example:
     * <pre>{@code
     *      User user = new User();
     *      user.setUsername("admin");
     *      SpringExpressionUtil.parseExpression("username", user); //admin
     * }</pre>
     *
     * @param expressionString 表达式
     * @param rootObject       根对象
     * @return
     */
    public static String parseExpression(@Language("SpEL") String expressionString, Object rootObject) {
        return parseExpression(expressionString, null, rootObject);
    }

    /**
     * 使用自定义的{@link ParserContext}解析EL表达式，对象属性和方法将作为根对象
     * <p>
     * example:
     * <pre>{@code
     *      User user = new User();
     *      user.setUsername("admin");
     *      SpringExpressionUtil.parseExpression("#{username}", SpringExpressionUtil.PARSER_CONTEXT, user); //admin
     * }</pre>
     *
     * @param expressionString 表达式
     * @param parserContext    提供给表达式分析器的输入，设置表达式解析的前后缀
     * @param rootObject       根对象
     * @return
     */
    public static String parseExpression(@Language("SpEL") String expressionString, ParserContext parserContext, Object rootObject) {
        return parseExpression(expressionString, parserContext, rootObject, null);
    }

    /**
     * 使用字符串模板解析EL表达式，对象属性和方法将作为根对象，使用默认的“#{”前缀和“}”后缀的字符表达式解析
     * <p>
     * example:
     * <pre>{@code
     *      User user = new User();
     *      user.setUsername("admin");
     *      Map<String, Object> variable = new HashMap<>();
     *      variable.put("var", "value");
     *      SpringExpressionUtil.parseTemplateExpression("#{username},#{#var}", user, variable); //admin,value
     * }</pre>
     *
     * @param expressionString 表达式
     * @param rootObject       根对象
     * @param variable         变量
     * @return
     */
    public static String parseTemplateExpression(@Language("SpEL") String expressionString, Object rootObject, Map<String, Object> variable) {
        return parseExpression(expressionString, PARSER_CONTEXT, rootObject, variable);
    }

    /**
     * 解析EL表达式，对象属性和方法将作为根对象，同时注入map参数
     * <p>
     * example:
     * <pre>{@code
     *      User user = new User();
     *      user.setUsername("admin");
     *      Map<String, Object> variable = new HashMap<>();
     *      variable.put("var", "value");
     *      SpringExpressionUtil.parseExpression("username + ',' + #var", user, variable); //admin,value
     * }</pre>
     *
     * @param expressionString 表达式
     * @param rootObject       根对象
     * @param variable         变量
     * @return
     */
    public static String parseExpression(@Language("SpEL") String expressionString, Object rootObject, Map<String, Object> variable) {
        return parseExpression(expressionString, null, rootObject, variable);
    }

    /**
     * 使用自定义的{@link ParserContext}解析EL表达式，对象属性和方法将作为根对象，同时注入map参数
     * <p>
     * example:
     * <pre>{@code
     *      User user = new User();
     *      user.setUsername("admin");
     *      Map<String, Object> variable = new HashMap<>();
     *      variable.put("var", "value");
     *      SpringExpressionUtil.parseExpression("#{username},#{#var}", SpringExpressionUtil.PARSER_CONTEXT, user, variable); //admin,value
     * }</pre>
     *
     * @param expressionString 表达式
     * @param parserContext    提供给表达式分析器的输入，设置表达式解析的前后缀
     * @param rootObject       根对象
     * @param variable         变量
     * @return
     */
    public static String parseExpression(@Language("SpEL") String expressionString, ParserContext parserContext, Object rootObject, Map<String, Object> variable) {
        return parseExpression(expressionString, parserContext, rootObject, variable, String.class);
    }

    /**
     * 使用自定义的{@link ParserContext}解析EL表达式，对象属性和方法将作为根对象，同时注入map参数
     * <p>
     * example:
     * <pre>{@code
     *      User user = new User();
     *      user.setUsername("admin");
     *      Map<String, Object> variable = new HashMap<>();
     *      variable.put("var", "value");
     *      SpringExpressionUtil.parseExpression("#{username},#{#var}", SpringExpressionUtil.PARSER_CONTEXT, user, variable, String.class); //admin,value
     * }</pre>
     *
     * @param expressionString  表达式
     * @param parserContext     提供给表达式分析器的输入，设置表达式解析的前后缀
     * @param rootObject        根对象
     * @param variable          变量
     * @param desiredResultType 调用方希望结果的类
     * @return
     */
    public static <T> T parseExpression(@Language("SpEL") String expressionString,
                                        ParserContext parserContext,
                                        Object rootObject,
                                        Map<String, Object> variable,
                                        Class<T> desiredResultType) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        try {
            ListableBeanFactory factory = SpringUtils.getBeanFactory();
            BeanResolver beanResolver = new BeanFactoryResolver(factory);
            context.setBeanResolver(beanResolver);
        } catch (Exception ignore) {
        }
        if (variable != null) {
            context.setVariables(variable);
        }
        return parseExpression(expressionString, context, parserContext, rootObject, desiredResultType);
    }

    /**
     * 使用自定义的{@link ParserContext}解析EL表达式，对象属性和方法将作为根对象，同时注入map参数
     * <p>
     * example:
     * <pre>{@code
     *      User user = new User();
     *      user.setUsername("admin");
     *      SimpleEvaluationContext evaluationContext = SimpleEvaluationContext.forReadOnlyDataBinding().withInstanceMethods().build();
     *      evaluationContext.setVariable("var", "value");
     *      SpringExpressionUtil.parseExpression("#{username},#{#var}", evaluationContext, SpringExpressionUtil.PARSER_CONTEXT, user, String.class); //admin,value
     * }</pre>
     *
     * @param expressionString  表达式
     * @param evaluationContext 提供上下文变量
     * @param parserContext     提供给表达式分析器的输入，设置表达式解析的前后缀
     * @param rootObject        根对象
     * @param desiredResultType 调用方希望结果的类
     * @return
     */
    public static <T> T parseExpression(@Language("SpEL") String expressionString,
                                        EvaluationContext evaluationContext,
                                        ParserContext parserContext,
                                        Object rootObject,
                                        Class<T> desiredResultType) {
        Expression expression = PARSER.parseExpression(expressionString, parserContext);
        if (rootObject == null) {
            return expression.getValue(evaluationContext, desiredResultType);
        }
        return expression.getValue(evaluationContext, rootObject, desiredResultType);
    }
}
