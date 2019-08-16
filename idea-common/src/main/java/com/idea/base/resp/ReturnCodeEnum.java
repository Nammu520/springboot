package com.idea.base.resp;

/**
 * @author dengyu
 * @desc 返回数据代码枚举
 * @date 2019/6/27
 */
public enum ReturnCodeEnum {

    SUCCESS(0, "请求成功"),

    SUCCESS_UPDATED(201, "更新成功"),

    SUCCESS_DELETED(204, "删除成功"),

    ERROR_BAD_REQUEST(400, "错误请求"),

    ERROR_UNAUTHORIZED(401, "请先登录"),

    ERROR_FORBIDDEN(403, "请求被拒绝"),

    ERROR_NOT_FOUND(404, "请求未找到"),

    ERROR_TIMEOUT(408, "请求超时，请稍后再试"),

    ERROR_INTERNAL_SERVER(500, "操作失败，请稍后再试"),

    // 鉴权模块
    ERROR_GENERATE_TOKEN(10001, "生成全局token异常");

    private int code;

    private String message;

    ReturnCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
