package com.idea.web.filter;

import com.idea.base.constant.CommonConstant;
import com.idea.base.constant.SpecialSymbol;
import com.idea.base.resp.CommonRespData;
import com.idea.base.resp.ReturnCodeEnum;
import com.idea.base.util.CommonUtil;
import com.idea.base.util.JSONUtil;
import com.idea.web.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author leixiaoming
 * @desc 登录配置
 * @date 2019/4/18
 */
@Slf4j
@Configuration
public class SecurityInterceptor extends WebMvcConfigurerAdapter {

    /**
     * 这里面的记录表示用户可能有登录，也有可能未登录的过滤列表
     */
    public static final List<String> WHITE_LIST_WITH_METHOD = new ArrayList() {{
        //
    }};

    @Bean
    public SessionInterceptor getSessionInterceptor() {
        return new SessionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptor = registry.addInterceptor(getSessionInterceptor());
        // 排除配置，这里面的的过滤列表是不会经过装载用户信息。所以些接口内数据不能跟用户相关

        // (Swagger)
        interceptor.excludePathPatterns("/swagger-ui.html");
        interceptor.excludePathPatterns("/webjars/**");
        // 用户鉴权
        interceptor.excludePathPatterns("/auth/login/**");

        // 拦截配置
        interceptor.addPathPatterns("/**");

    }

    /**
     * @author leixiaoming
     * @desc
     * @date 2019/4/18
     */
    private class SessionInterceptor extends HandlerInterceptorAdapter {

        @Override
        public boolean preHandle(
                HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            String httpMethod = request.getMethod();
            if ("OPTIONS".equals(httpMethod)) {
                return true;
            }
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.getBean() instanceof BaseController) {
                BaseController baseController = (BaseController) handlerMethod.getBean();
                baseController.cleanLocalThread();
                String ticket = request.getHeader(CommonConstant.TICKET);
                if (ticket == null || ticket.length() == 0) { //兼容直接在header中传递ticket和在cookies中传递ticket两种方式
                    Cookie[] cookies = request.getCookies();
                    if (cookies != null && cookies.length > 0) {
                        for (int i = 0; i < cookies.length; i++) {
                            if (cookies[i].getName().equals(CommonConstant.TICKET)) {
                                ticket = cookies[i].getValue();
                                break;
                            }
                        }
                    }
                }
                if (!baseController.hasAuth(ticket) || !checkIsWhite(request.getRequestURI(), httpMethod)) {
                    response.setCharacterEncoding(CommonConstant.CHARSET_UTF8);
                    response.setContentType("application/json; charset=utf-8");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(
                            JSONUtil.toJson(CommonRespData.getInstance().failed(ReturnCodeEnum.ERROR_UNAUTHORIZED)));
                    return false;
                }
            }
            return super.preHandle(request, response, handler);
        }
    }

    /**
     * 查检请求是否在白名单中
     *
     * @param path       路径
     * @param httpMethod 请求方法
     * @return 在与不在
     */
    public static boolean checkIsWhite(String path, String httpMethod) {
        String signature = CommonUtil.stringAppend(path, SpecialSymbol.COMMA, httpMethod).toString();
        for (String pattern : WHITE_LIST_WITH_METHOD) {
            boolean matches = Pattern.matches(pattern, signature);
            if (matches) {
                return true;
            }
        }
        return false;
    }
}


