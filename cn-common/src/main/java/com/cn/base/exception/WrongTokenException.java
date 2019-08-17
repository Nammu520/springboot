package com.cn.base.exception;

/**
 * @author leixiaoming
 * @desc
 * @date 2019/05/16
 */
public class WrongTokenException extends Exception {
    public WrongTokenException() {
    }

    public WrongTokenException(String message) {
        super(message);
    }

    public WrongTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongTokenException(Throwable cause) {
        super(cause);
    }

    public WrongTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
