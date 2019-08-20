package com.cn.web.controller;

import com.cn.base.config.LocaleMessageSourceService;
import com.cn.base.config.RedisDao;
import com.cn.base.exception.SysException;
import com.cn.base.enums.ReturnCodeEnum;
import com.cn.service.user.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author dengyu
 * @desc 基础控制器，使用ThreadLocal来保存用户信息，保证每个线程中的用户能够独立，如果需要获取用户登录信息需要继承该类。
 * @date 2019/3/25
 */
@Slf4j
@ApiIgnore
public class BaseController {

    private static ThreadLocal<String> loginUserTicketThreadLocal = new ThreadLocal<>();

    private static ThreadLocal<Integer> userIdThreadLocal = new ThreadLocal<>();

    @Autowired
    private LocaleMessageSourceService messageSourceService;

    @Autowired
    RedisDao redisDao;

    @Autowired
    private AuthService authService;

    /**
     * 清除threadLocal里面的所有数据
     */
    public void cleanLocalThread() {
        loginUserTicketThreadLocal.remove();
        userIdThreadLocal.remove();
    }

    public String getTicket() {
        return loginUserTicketThreadLocal.get();
    }

    /**
     * 校验ticket是否有效
     *
     * @return boolean ticket是否有效
     */
    public Boolean hasAuth(String ticket) {
        if (StringUtils.isBlank(ticket)) {
            return false;
        }
        Integer userId = authService.checkUserLogin(ticket);
        if(userId != null){
            loginUserTicketThreadLocal.set(ticket);
            userIdThreadLocal.set(userId);
            return true;
        } else{
            loginUserTicketThreadLocal.remove();
            userIdThreadLocal.remove();
            return false;
        }
    }

    /**
     * 获取用户ID- 未获取到返回空
     *
     * @return 用户ID
     */
    public Integer getUserIdUnCheck() {
        return userIdThreadLocal.get();
    }

    /**
     * 获取用户ID- 未登录会报异常
     *
     * @return 用户ID
     */
    public int getUserId() {
        Integer userId = userIdThreadLocal.get();
        if (userId != null) {
            return userId;
        }
        throw new SysException(ReturnCodeEnum.ERROR_UNAUTHORIZED, messageSourceService.getMessage(ReturnCodeEnum.ERROR_UNAUTHORIZED));
    }
}
