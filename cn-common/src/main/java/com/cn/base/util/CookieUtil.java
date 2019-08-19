package com.cn.base.util;

import com.cn.base.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * @author dengyu
 * @desc cookie操作工具类
 * @date 2018/4/17
 */
@Slf4j
public final class CookieUtil {

    /**
     * 隐藏构造器
     */
    private CookieUtil() {

    }

    /**
     * @return Cookie[]
     */
    public static Cookie[] getCookies() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Cookie[] c = request.getCookies();
        return c;
    }

    /**
     * showCookie
     */
    public static void showCookie() {
        Cookie[] c = getCookies();
    }

    /**
     * @param cookie cookie
     */
    public static void saveCookie(Cookie cookie) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        response.addCookie(cookie);
    }

    /**
     * 添加cookie
     *
     * @param name   name
     * @param object object
     */
    public static void addCookie(String name, Object object) {
        try {
            Cookie cookie = new Cookie(name, String.valueOf(object));
            cookie.setPath("/");
            cookie.setMaxAge(CommonConstant.CacheTime.TICKET);
            // 设置保存cookie最大时长
            saveCookie(cookie);
        } catch (Exception e) {
            log.error(" -------添加cookie 失败！--------" + e.getMessage());
        }
    }

    /**
     * 添加cookie
     *
     * @param name   name name
     * @param maxAge maxAge maxAge
     * @param object object object
     */
    public static void addCookie(String name, Object object, int maxAge) {
        try {
            Cookie cookie = new Cookie(name, String.valueOf(object));
            cookie.setPath("/");
            cookie.setMaxAge(maxAge);
            // 设置保存cookie最大时长
            saveCookie(cookie);
        } catch (Exception e) {
            log.error(" -------添加cookie 失败！--------" + e.getMessage());
        }
    }


    /**
     * 获取cookie
     *
     * @param name  name
     * @param clazz clazz
     * @param <T>   <T>
     * @return T
     */
    public static <T> T getCookie(String name, Class<T> clazz) {
        try {
            Cookie[] cookies = getCookies();
            String v = null;
            for (int i = 0; i < (cookies == null ? 0 : cookies.length); i++) {
                if ((name).equalsIgnoreCase(cookies[i].getName())) {
                    v = URLDecoder.decode(cookies[i].getValue(), CommonConstant.CHARSET_UTF8);
                }
            }
            if (v != null) {
                return JSONUtil.toBean(v, clazz);
            }
        } catch (Exception e) {
            log.error("------获取 clazz Cookie 失败----- " + e.getMessage());
        }
        return null;
    }

    /**
     * 获取cookie
     *
     * @param name name
     * @return String
     */
    public static String getCookie(String name) {
        try {
            Cookie[] cookies = getCookies();
            for (int i = 0; i < (cookies == null ? 0 : cookies.length); i++) {
                if ((name).equalsIgnoreCase(cookies[i].getName())) {
                    return URLDecoder.decode(cookies[i].getValue(), CommonConstant.CHARSET_UTF8);
                }
            }
        } catch (Exception e) {
            log.error(" --------获取String cookie 失败--------   " + e.getMessage());
        }
        return null;
    }

    /**
     * 删除cookie
     *
     * @param name name
     */
    public static void removeCookie(String name) {
        try {
            Cookie[] cookies = getCookies();
            for (int i = 0; i < (cookies == null ? 0 : cookies.length); i++) {
                if ((name).equalsIgnoreCase(cookies[i].getName())) {
                    Cookie cookie = new Cookie(name, "");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    // 设置保存cookie最大时长为0，即使其失效
                    saveCookie(cookie);
                }
            }
        } catch (Exception e) {
            log.error(" -------删除cookie失败！--------" + e.getMessage());
        }
    }

}
