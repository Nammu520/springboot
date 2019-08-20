package com.cn.base.resp;

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

    ERR_PARAM_ERROR(421, "参数错误"),

    ERROR_INTERNAL_SERVER(500, "操作失败，请稍后再试"),

    // 用户模块
    ERROR_GENERATE_TOKEN(10001, "生成全局token异常"),

    ERROR_GET_MEUN_LIST(10002 ,"获取菜单模板为空"),

    ERROR_USER_NOT_EXIST(10003, "用户不存在"),

    ERROR_USERNAME_OR_PASSWORD(10004, "账号或密码错误"),

    ERROR_ROLE_NOT_EXIST(10005, "角色不存在"),

    ERROR_ROLE_BIND_USER(10006, "角色已绑定用户");

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
