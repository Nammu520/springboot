package com.cn.web.filter;

import com.cn.base.config.RedisDao;
import com.cn.base.constant.CommonConstant;
import com.cn.base.constant.RedisConstants;
import com.cn.base.constant.SpecialSymbol;
import com.cn.base.resp.CommonRespData;
import com.cn.base.resp.ReturnCodeEnum;
import com.cn.base.util.CommonUtil;
import com.cn.base.util.CookieUtil;
import com.cn.base.util.JSONUtil;
import com.cn.web.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @author dengyu
 * @desc 登录配置
 * @date 2019/4/18
 */
@Slf4j
@Configuration
public class SecurityInterceptor extends WebMvcConfigurerAdapter {

    @Autowired
    private RedisDao redisDao;

    @Bean
    public SessionInterceptor getSessionInterceptor() {
        return new SessionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptor = registry.addInterceptor(getSessionInterceptor());
        // (Swagger)
        interceptor.excludePathPatterns("/swagger-ui.html");
        interceptor.excludePathPatterns("/webjars/**");
        // 用户鉴权
        interceptor.excludePathPatterns("/auth/login/**");

        // 拦截配置
        interceptor.addPathPatterns("/**");

        // 微信接口
        interceptor.excludePathPatterns("/weixin/**");
    }

    /**
     * @author dengyu
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
            String ticket = request.getHeader(CommonConstant.TICKET);
            if (ticket == null || ticket.length() == 0) { //兼容直接在header中传递ticket和在cookies中传递ticket两种方式
                ticket = CookieUtil.getCookie(CommonConstant.TICKET);
            }
            boolean hasAuth = redisDao.isExist(RedisConstants.getAdminTicketKey(ticket));
            if(hasAuth){
                HandlerMethod hm = (HandlerMethod) handler;
                if(hm.getBean() instanceof BaseController) {
                    BaseController baseController = (BaseController) hm.getBean();
                    baseController.cleanLocalThread();
                    if(StringUtils.isNotBlank(ticket)) {
                        hasAuth = baseController.hasAuth(ticket);
                    }
                }
                //重置过期时间
                redisDao.expire(RedisConstants.getAdminTicketKey(ticket), CommonConstant.CacheTime.ADMIN_TICKET);
            }
            if (!hasAuth) {
                response.setCharacterEncoding(CommonConstant.CHARSET_UTF8);
                response.setContentType("application/json; charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(
                        JSONUtil.toJson(CommonRespData.getInstance().failed(ReturnCodeEnum.ERROR_UNAUTHORIZED)));
                return false;
            }
            return super.preHandle(request, response, handler);
        }
    }
}


