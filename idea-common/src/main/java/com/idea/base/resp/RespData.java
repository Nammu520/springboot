package com.idea.base.resp;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

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

    @Data
    public static class PageData<T> implements Serializable {
        @ApiModelProperty(name = "results", value = "分页数据")
        private List<T> results;
        @ApiModelProperty(name = "next", value = "下一页链接（android特供），如果下一页没有数据则为null", example = "http://127.0.0.1/getdata?page=2")
        private String next;
        @ApiModelProperty(name = "previous", value = "上一页链接（android特供），如果上一页没有数据则为null", example = "http://127.0.0.1/getdata?page=1")
        private String previous;
        @ApiModelProperty(name = "count", value = "数据条数")
        private long count;
        @JsonIgnore
        private int size;
    }

    @Data
    public static class PageData2<T> implements Serializable {
        @ApiModelProperty(name = "results", value = "响应对象（只是为了适应接口：获取问题回答列表）")
        private T results;
        @ApiModelProperty(name = "next", value = "下一页链接（android特供），如果下一页没有数据则为null")
        private String next;
        @ApiModelProperty(name = "previous", value = "上一页链接（android特供），如果上一页没有数据则为null")
        private String previous;
        @ApiModelProperty(name = "count", value = "数据条数")
        private long count;
        @JsonIgnore
        private int size;
    }

    @Data
    public static class PageData3<T> extends PageData {
        @JsonProperty(value = "service_phone")
        private String servicePhone;
    }

    @Data
    public static class PageDataSimple<T> implements Serializable {
        @ApiModelProperty(name = "results", value = "响应对象")
        private T results;
        @ApiModelProperty(name = "count", value = "数据条数")
        private long count;
        @JsonIgnore
        private int size;
    }
}