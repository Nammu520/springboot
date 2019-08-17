package com.cn.service;

import com.cn.base.config.LocaleMessageSourceService;
import com.cn.base.config.RedisDao;
import com.cn.base.constant.CommonConstant;
import com.cn.base.exception.SysException;
import com.cn.base.exception.WrongTokenException;
import com.cn.base.resp.ReturnCodeEnum;
import com.cn.base.util.UserTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthService {

    @Autowired
    private LocaleMessageSourceService messageSourceService;

    @Autowired
    private RedisDao redisDao;

    /**
     * 检查用户是否登录
     *
     * @param ticket ticket
     * @return 是否存在
     */
    public Integer checkUserLogin(String ticket) {
        try {
            if (redisDao.isHashKeyExist(CommonConstant.TICKET, ticket)) {
                return UserTokenUtil.getUserToken(ticket);
            }
        } catch (WrongTokenException e) {
            log.error("token错误", e);
            redisDao.delHashKey(CommonConstant.TICKET, ticket);
        }
        return null;
    }

    /**
     * 用户登出
     *
     * @param userId 用户ID
     * @param ticket ticket
     */
    public void logout(int userId, String ticket) {
        redisDao.delHashKey(CommonConstant.TICKET, ticket);
    }

    /**
     * 用户登录
     *
     * @param userId 用户ID
     */
    public void login(int userId) {
        String ticket;
        try {
            ticket = UserTokenUtil.generateToken(userId);
        } catch (WrongTokenException e){
            log.error("生成全局token异常");
            throw new SysException(ReturnCodeEnum.ERROR_GENERATE_TOKEN, messageSourceService.getMessage(ReturnCodeEnum.ERROR_GENERATE_TOKEN));
        }
        redisDao.putHashKey(CommonConstant.TICKET, ticket, System.currentTimeMillis() / 1000 + CommonConstant.CacheTime.TICKET);
    }
}
