package com.cn.base.util;

import com.cn.base.constant.SpecialSymbol;
import com.cn.base.exception.WrongTokenException;
import java.util.Date;

/**
 * @author dengyu
 * @desc
 * @date 2019/05/14
 */
public final class UserTokenUtil {
    /**
     * 隐藏构造器
     */
    private UserTokenUtil() {
    }

    private static final String DEFAULT_SECRET_KEY = "ONLINE_2019";

    /**
     * 生成token
     *
     * @param userId 用户id
     * @return token
     * @throws WrongTokenException token异常
     */
    public static String generateToken(Integer userId) throws WrongTokenException {
        try {
            return SecretUtil.encrypt(CommonUtil.stringAppend(userId,
                    SpecialSymbol.COMMA,
                    new Date()).toString(), DEFAULT_SECRET_KEY);
        } catch (Exception e) {
            throw new WrongTokenException("生成Token失败", e);
        }
    }

    /**
     * 获取用户登录信息
     *
     * @param token token
     * @return 用户登录信息
     * @throws WrongTokenException token异常
     */
    public static Integer getUserToken(String token) throws WrongTokenException {
        try {
            String decryptor = SecretUtil.decrypt(token, DEFAULT_SECRET_KEY);
            String[] split = decryptor.split(SpecialSymbol.COMMA);
            return Integer.parseInt(split[0]);
        } catch (Exception e) {
            throw new WrongTokenException("解析Token失败", e);
        }

    }

}