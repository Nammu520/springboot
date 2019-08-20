package com.cn.web.aspect;

import com.cn.base.constant.SpecialSymbol;
import com.cn.base.exception.SysException;
import com.cn.web.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dengyu
 * @desc Controller层日志切面
 * @date 2019/8/16
 */
@Aspect
@Order(Integer.MAX_VALUE - 1)
@Component
@Slf4j
public class WebLogAspect {

    private static final String STATIC_INFO_LOG = "userId:{},module:{},target：{},method：{},params：{}";
    private static final String STATIC_ERROR_LOG = "userId:{},targetType:{},remarks:{},params: {}";

    /**
     * 切面
     */
    @Pointcut("execution(* com.cn.web.controller..*Controller.*(..)) && !execution(* com.cn.web.controller.BaseController.*(..))")
    private void pointCutMethod() {
    }

    /**
     * @param joinPoint joinPoint
     * @throws Throwable 异常
     */

    @Before("pointCutMethod()")
    public void doBefore(JoinPoint joinPoint) {
        Object targetObj = joinPoint.getTarget();
        Integer userId = null;
        if (targetObj instanceof BaseController) {
            BaseController target = (BaseController) targetObj;
            userId = target.getUserIdUnCheck();
        }
        StringBuilder targetName = new StringBuilder();
        targetName.append(joinPoint.getSignature().getDeclaringTypeName());
        targetName.append(SpecialSymbol.DOT);
        targetName.append(joinPoint.getSignature().getName());
        String httpMethod = null;
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            httpMethod = request.getMethod();
        }
        log.info(STATIC_INFO_LOG, userId == null
                        ? SpecialSymbol.PLACEHOLDER : userId, joinPoint.getSignature().getName(),
                targetName.toString(), httpMethod, joinPoint.getArgs());
    }

    /**
     * @param joinPoint joinPoint
     * @param ex        异常
     */
    @AfterThrowing(value = "pointCutMethod()", throwing = "ex")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        Object targetObj = joinPoint.getTarget();
        Integer userId = null;
        if (targetObj instanceof BaseController) {
            BaseController target = (BaseController) targetObj;
            userId = target.getUserIdUnCheck();
        }

        if (ex instanceof SysException) {
            log.error(STATIC_ERROR_LOG, userId == null
                            ? SpecialSymbol.PLACEHOLDER : userId,
                    joinPoint.getSignature().getName(), SpecialSymbol.PLACEHOLDER, joinPoint.getArgs(), ex);
        }
    }
}
