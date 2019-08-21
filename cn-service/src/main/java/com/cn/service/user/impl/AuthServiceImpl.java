package com.cn.service.user.impl;

import com.cn.base.config.RedisDao;
import com.cn.base.constant.CommonConstant;
import com.cn.base.constant.RedisConstants;
import com.cn.base.dto.user.BackendUserLoginReqDTO;
import com.cn.base.exception.SysException;
import com.cn.base.exception.WrongTokenException;
import com.cn.base.enums.ReturnCodeEnum;
import com.cn.base.util.CookieUtil;
import com.cn.base.util.PasswordUtil;
import com.cn.base.util.UserTokenUtil;
import com.cn.persist.user.dao.BackendUserMapper;
import com.cn.persist.user.model.BackendUser;
import com.cn.service.user.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthServiceImpl implements AuthService {

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private BackendUserMapper backendUserMapper;

    /**
     * 检查用户是否登录
     *
     * @param ticket ticket
     * @return 是否存在
     */
    @Override
    public Integer checkUserLogin(String ticket) {
        String user = redisDao.get(RedisConstants.getAdminTicketKey(ticket));
        if(StringUtils.isBlank(user)){
            return null;
        }
        return Integer.parseInt(user);
    }

    /**
     * 用户登出
     *
     * @param ticket ticket
     */
    @Override
    public void logout(String ticket) {
        redisDao.delete(RedisConstants.getAdminTicketKey(ticket));
        CookieUtil.removeCookie(CommonConstant.TICKET);
    }

    /**
     * 用户登录
     *
     * @param userLoginReqDto 用户登录对象
     */
    @Override
    public void login(BackendUserLoginReqDTO userLoginReqDto) {
        // 校验账号密码是否正确
        BackendUser backendUser = backendUserMapper.selectByUsername(userLoginReqDto.getUsername());
        if(backendUser == null){
            throw new SysException(ReturnCodeEnum.ERROR_USER_NOT_EXIST);
        }
        boolean flag = PasswordUtil.checkPasswordRight(userLoginReqDto.getPassword(), backendUser.getPassword());
        if(!flag) {
            throw new SysException(ReturnCodeEnum.ERROR_USERNAME_OR_PASSWORD);
        }
        String ticket;
        try {
            ticket = UserTokenUtil.generateToken(backendUser.getId());
        } catch (WrongTokenException e){
            log.error("生成全局token异常");
            throw new SysException(ReturnCodeEnum.ERROR_GENERATE_TOKEN);
        }
        redisDao.set(RedisConstants.getAdminTicketKey(ticket), backendUser.getId(), CommonConstant.CacheTime.ADMIN_TICKET);
        CookieUtil.addCookie(CommonConstant.TICKET, ticket);
    }
}
