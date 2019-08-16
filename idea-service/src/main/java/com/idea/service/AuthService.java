package com.idea.service;

import com.idea.base.config.LocaleMessageSourceService;
import com.idea.base.config.RedisDao;
import com.idea.base.constant.CommonConstant;
import com.idea.base.exception.SysException;
import com.idea.base.exception.WrongTokenException;
import com.idea.base.resp.ReturnCodeEnum;
import com.idea.base.util.UserTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class AuthService {

    @Resource
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
