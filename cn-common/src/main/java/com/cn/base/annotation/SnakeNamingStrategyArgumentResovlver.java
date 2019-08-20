package com.cn.base.annotation;

import com.cn.base.annotation.SnakeNaming;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * desc  ${desc}
 *
 * @author dengyu
 * Date  2018/7/24
 */
public class SnakeNamingStrategyArgumentResovlver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(SnakeNaming.class);
    }


    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String name = ModelFactory.getNameForParameter(parameter);
        Object attribute = (mavContainer.containsAttribute(name) ? mavContainer.getModel().get(name) : createAttribute(parameter));
        WebDataBinder binder = binderFactory.createBinder(webRequest, attribute, name);
        if (binder.getTarget() != null) {
            if (!mavContainer.isBindingDisabled(name)) {
                bindRequestParameters(binder, webRequest);
            }
            validateIfApplicable(binder, parameter);
            if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
                throw new BindException(binder.getBindingResult());
            }
        }
        Map<String, Object> bindingResultModel = binder.getBindingResult().getModel();
        mavContainer.removeAttributes(bindingResultModel);
        mavContainer.addAllAttributes(bindingResultModel);
        return binder.convertIfNecessary(binder.getTarget(), parameter.getParameterType(), parameter);
    }


    /**
     * Whether to raise a fatal bind exception on validation errors. * @param binder the data binder used to perform data binding * @param methodParam the method argument * @return {@code true} if the next method argument is not of type {@link Errors}
     */
    protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter methodParam) {
        int i = methodParam.getParameterIndex();
        Class<?>[] paramTypes = methodParam.getMethod().getParameterTypes();
        boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
        return !hasBindingResult;
    }

    protected void validateIfApplicable(WebDataBinder binder, MethodParameter methodParam) {
        Annotation[] annotations = methodParam.getParameterAnnotations();
        for (Annotation ann : annotations) {
            Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
            if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
                Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
                Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints : new Object[]{hints});
                binder.validate(validationHints);
                break;
            }
        }
    }

    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        Map<String, String[]> result = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (!parameterMap.isEmpty()) {
            Set<String> keys = parameterMap.keySet();
            for (String key : keys) {
                result.put(key, parameterMap.get(key));
                String translate = translate(key);
                if (key.equals(translate)) {
                    continue;
                }
                result.put(translate, parameterMap.get(key));
            }
        }
        MutablePropertyValues mpvs = new MutablePropertyValues(result);
        binder.bind(mpvs);
    }

    protected Object createAttribute(MethodParameter methodParam) throws Exception {
        return BeanUtils.instantiateClass(methodParam.getParameterType());
    }


    public static String translate(String input) { // garbage in, garbage out
        if (input == null) return input;
        int length = input.length();
        StringBuilder result = new StringBuilder(length * 2);
        boolean wasPrevTranslated = false;
        for (int i = 0; i < length; i++) { // skip first starting underscore
            char c = input.charAt(i);
            if (i > 0 && c == '_') {
                wasPrevTranslated = true;
            } else if (Character.isLowerCase(c) && wasPrevTranslated) {
                result.append(Character.toUpperCase(c));
                wasPrevTranslated = false;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}

