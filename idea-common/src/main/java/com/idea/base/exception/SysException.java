package com.idea.base.exception;

import com.idea.base.resp.ReturnCodeEnum;

public class SysException extends RuntimeException{

    private ReturnCodeEnum code;

    private Object data;

    public SysException() {
    }

    public SysException(String message) {
        super(message);
    }

    public SysException(String message, Throwable cause) {
        super(message, cause);
    }

    public SysException(Throwable cause) {
        super(cause);
    }

    public SysException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SysException(ReturnCodeEnum code, String message) {
        super(message);
        this.code = code;
    }
}
