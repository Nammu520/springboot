package com.cn.base.dto.resp;


import com.cn.base.enums.ReturnCodeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dengyu
 * @desc App返回类的数据结构
 * @date 2019/6/27
 */
@Data
public class RespData<T> implements Serializable {
    @ApiModelProperty(name = "code", value = "响应编码（0表示正常，400表示请求异常，500表示服务器异常）", example = "0", allowableValues = "0,400,500")
    private int code;
    @ApiModelProperty(name = "data", value = "响应数据")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(name = "msg", value = "响应消息", example = "请求成功")
    private String msg;

    public static final int pages_abnormal = -1;
    public static final int pages_simple = -2;
    public static final int pages_advance = -3;

    public static RespData getInstance() {
        return new RespData();
    }

    public RespData<T> success() {
        return success(null);
    }

    public RespData<T> genRespData(int code, T data, String msg) {
        if (data != null) {
            this.data = data;
        } else {
            this.data = (T) new Object();
        }
        this.code = code;
        this.msg = msg;
        return this;
    }

    public RespData<T> genRespData(ReturnCodeEnum returnCodeEnum, T data) {
        return genRespData(returnCodeEnum.getCode(), data, returnCodeEnum.getMessage());
    }

    public RespData<T> success(T data) {
        return genRespData(ReturnCodeEnum.SUCCESS, data);
    }

    public RespData<T> success(T data, String msg) {
        return genRespData(ReturnCodeEnum.SUCCESS.getCode(), data, msg);
    }

    public RespData<T> failed(ReturnCodeEnum codeEnum) {
        return genRespData(codeEnum, null);
    }

    public RespData<T> failed(String msg) {
        return genRespData(ReturnCodeEnum.ERROR_BAD_REQUEST.getCode(), null, msg);
    }

    public RespData<T> failed(ReturnCodeEnum codeEnum, T data, String msg) {
        return genRespData(codeEnum.getCode(), data, msg);
    }

    /**
     * 生成默认错误返回数据
     *
     * @param returnCodeEnum 错误码枚举
     * @param msg            消息内容
     * @return 响应数据
     */
    public RespData<T> failed(ReturnCodeEnum returnCodeEnum, String msg) {
        return genRespData(returnCodeEnum.getCode(), null, msg);
    }
}