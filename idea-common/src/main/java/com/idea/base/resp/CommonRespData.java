package com.idea.base.resp;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author leixiaoming
 * @desc 通用返回类的数据结构
 * @date 2018/2/27
 */
@Data
public class CommonRespData<T> implements Serializable {
    @ApiModelProperty(name = "code", value = "响应编码（0表示正常，400表示请求异常，500表示服务器异常）", example = "0", allowableValues = "0,400,500")
    private int code;
    @ApiModelProperty(name = "data", value = "响应数据")
    private T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(name = "msg", value = "响应消息", example = "请求成功")
    private String msg;

    public static CommonRespData getInstance() {
        return new CommonRespData();
    }

    public CommonRespData<T> success() {
        return success(null);
    }

    public CommonRespData<T> genRespData(int code, T data, String msg) {
        if (data != null) {
            this.data = data;
        } else {
            this.data = (T) new Object();
        }
        this.code = code;
        this.msg = msg;
        return this;
    }

    public CommonRespData<T> genRespData(ReturnCodeEnum returnCodeEnum, T data) {
        return genRespData(returnCodeEnum.getCode(), data, returnCodeEnum.getMessage());
    }

    public CommonRespData<T> success(T data) {
        return genRespData(ReturnCodeEnum.SUCCESS, data);
    }

    public CommonRespData<T> success(T data, String msg) {
        return genRespData(ReturnCodeEnum.SUCCESS.getCode(), data, msg);
    }

    public CommonRespData<T> successMsg(String msg) {
        return genRespData(ReturnCodeEnum.SUCCESS.getCode(), null, msg);
    }

    public CommonRespData<T> failed(ReturnCodeEnum codeEnum) {
        return genRespData(codeEnum, null);
    }

    public CommonRespData<T> failed(String msg) {
        return genRespData(ReturnCodeEnum.ERROR_BAD_REQUEST.getCode(), null, msg);
    }

    public CommonRespData<T> failed(ReturnCodeEnum codeEnum, T data, String msg) {
        return genRespData(codeEnum.getCode(), data, msg);
    }

    @Data
    public static class PageData<T> implements Serializable {
        @ApiModelProperty(name = "results", value = "响应对象")
        private T results;
        @ApiModelProperty(name = "count", value = "数据条数")
        private long count;
    }

}