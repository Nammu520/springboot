package com.idea.base.config;

import com.idea.base.resp.ReturnCodeEnum;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

/**
 *
 */
@Component
public class LocaleMessageSourceService {

    @Resource
    private MessageSource messageSource;

    /**
     * @param returnCodeEnum 错误码枚举.
     * @return String
     */
    public String getMessage(ReturnCodeEnum returnCodeEnum) {
        return getMessage(String.valueOf(returnCodeEnum.getCode()), null);
    }

    /**
     * @param returnCodeEnum 错误码枚举.
     * @param args           : 数组参数.
     * @return String
     */
    public String getMessage(ReturnCodeEnum returnCodeEnum, Object[] args) {
        return getMessage(String.valueOf(returnCodeEnum.getCode()), args);
    }


    /**
     * @param code ：对应messages配置的key.
     * @return String
     */
    public String getMessage(String code) {
        return getMessage(code, null);
    }

    /**
     * @param code ：对应messages配置的key.
     * @param args : 数组参数.
     * @return String
     */
    public String getMessage(String code, Object[] args) {
        return getMessage(code, args, "");
    }

    /**
     * @param code           ：对应messages配置的key.
     * @param args           : 数组参数.
     * @param defaultMessage : 没有设置key的时候的默认值.
     * @return String
     */
    public String getMessage(String code, Object[] args, String defaultMessage) {
        //这里使用比较方便的方法，不依赖request.
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }

}
