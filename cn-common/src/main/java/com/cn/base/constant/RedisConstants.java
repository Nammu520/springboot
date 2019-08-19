package com.cn.base.constant;

import com.cn.base.util.CommonUtil;
import com.cn.base.util.DateUtils;

import java.util.Date;

/**
 * @author dengyu
 * @desc
 * @date 2019/5/12
 */
public final class RedisConstants {

    /**
     * 隐藏构造器
     */
    private RedisConstants() {

    }

    public static final String KLI_ADMIN = "admin";

    public static final String USER = "user";



    /**
     * @param key 唯一值
     * @return app ticket缓存key
     */
    public static String getTicketKey(String key) {
        return CommonUtil.stringAppend(CommonConstant.TICKET, SpecialSymbol.COLON, key).toString();
    }

    /**
     * 获取后台管理平台key
     *
     * @param key 唯一值
     * @return admin ticket缓存key
     */
    public static String getAdminTicketKey(String key) {
        return CommonUtil.stringAppend(KLI_ADMIN, SpecialSymbol.COLON,
                CommonConstant.TICKET, SpecialSymbol.COLON, key).toString();
    }

    /**
     * 获取用户ticket缓存key
     *
     * @param userId 用户id
     * @return 缓存key
     */
    public static String getUserIdKey(int userId) {
        return CommonUtil.stringAppend(USER, SpecialSymbol.COLON, userId, SpecialSymbol.COLON, CommonConstant.TICKET).toString();
    }


}
