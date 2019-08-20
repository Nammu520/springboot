package com.cn.base.exception;

import com.cn.base.enums.ReturnCodeEnum;
import lombok.Data;

@Data
public class SysException extends RuntimeException{

    private ReturnCodeEnum code;

    private Object data;

    public SysException() {
        super();
        this.code = ReturnCodeEnum.ERROR_BAD_REQUEST;
    }

    public SysException(String message, Throwable cause) {
        super(message, cause);
        this.code = ReturnCodeEnum.ERROR_BAD_REQUEST;
    }

    public SysException(String message) {
        super(message);
        this.code = ReturnCodeEnum.ERROR_BAD_REQUEST;
    }

    public SysException(String message, Object data) {
        super(message);
        this.code = ReturnCodeEnum.ERROR_BAD_REQUEST;
        this.data = data;
    }

    public SysException(Throwable cause) {
        super(cause);
        this.code = ReturnCodeEnum.ERROR_BAD_REQUEST;
    }

    public SysException(ReturnCodeEnum code) {
        super(code.getMessage());
        this.code = code;
    }

    public SysException(ReturnCodeEnum code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public SysException(ReturnCodeEnum code, String message, Object data, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.data = data;
    }

    public SysException(ReturnCodeEnum code, String message) {
        super(message);
        this.code = code;
    }

    public SysException(ReturnCodeEnum code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public SysException(ReturnCodeEnum code, Throwable cause) {
        super(cause);
        this.code = code;
    }
}
