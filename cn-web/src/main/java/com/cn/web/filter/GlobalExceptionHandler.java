package com.cn.web.filter;

import com.cn.base.config.LocaleMessageSourceService;
import com.cn.base.exception.SysException;
import com.cn.base.exception.WrongTokenException;
import com.cn.base.resp.RespData;
import com.cn.base.resp.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @author dengyu
 * @desc
 * @date 2019/4/18
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    @Resource
    private LocaleMessageSourceService messageSourceService;

    /**
     * 没有捕获的异常
     *
     * @param e 异常
     * @return 默认错误响应数据
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private RespData<?> runtimeExceptionHandler(Exception e) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.error("没有捕获的异常,请求路径：{}", request.getServletPath(), e);
        return RespData.getInstance().failed(ReturnCodeEnum.ERROR_BAD_REQUEST,
                messageSourceService.getMessage(ReturnCodeEnum.ERROR_BAD_REQUEST));
    }


    /**
     * Token错误异常
     *
     * @param e Token错误异常
     * @return 默认错误响应数据
     */
    @ExceptionHandler(value = WrongTokenException.class)
    @ResponseStatus(HttpStatus.OK)
    public RespData<?> wrongTokenExceptionHandler(WrongTokenException e) {
        log.error("Token错误异常", e);
        return RespData.getInstance().failed(ReturnCodeEnum.ERROR_UNAUTHORIZED,
                messageSourceService.getMessage(ReturnCodeEnum.ERROR_UNAUTHORIZED));
    }

    /**
     * 业务异常
     *
     * @param e 业务异常
     * @return 默认错误响应数据
     */
    @ExceptionHandler(value = SysException.class)
    @ResponseStatus(HttpStatus.OK)
    public RespData<?> sysExceptionHandler(SysException e) {
        return RespData.getInstance()
                .failed(e.getCode(), e.getMessage());
    }

    /**
     * 请求参数异常
     *
     * @param req 请求对象
     * @param e   请求参数异常
     * @return 默认错误响应数据
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.OK)
    public RespData<?> missingServletRequestParameterExceptionHandler(
            HttpServletRequest req, MissingServletRequestParameterException e) {
        return RespData.getInstance()
                .failed(ReturnCodeEnum.ERR_PARAM_ERROR,
                        messageSourceService.getMessage(ReturnCodeEnum.ERR_PARAM_ERROR));
    }

    /**
     * 请求方法不支持异常
     *
     * @param e   请求方法不支持异常
     * @param req 请求求对象
     * @return 默认错误响应数据
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    public RespData<?> httpRequestMethodNotSupportedExceptionHandler(
            HttpServletRequest req, HttpRequestMethodNotSupportedException e) {
        return RespData.getInstance()
                .failed(ReturnCodeEnum.ERR_PARAM_ERROR,
                        messageSourceService.getMessage(ReturnCodeEnum.ERR_PARAM_ERROR));
    }

    /**
     * 请求参数验证失败异常
     *
     * @param e 请求参数验证失败异常
     * @return 默认错误响应数据
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    private RespData<?> methodArgumentNotValidHandler(MethodArgumentNotValidException e) {
        StringBuffer errorMsg = new StringBuffer();
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        errors.stream().forEach(x -> {
            if (errorMsg.length() != 0) {
                errorMsg.append(";");
            }
            errorMsg.append(x.getDefaultMessage());
        });
        return RespData.getInstance().failed(ReturnCodeEnum.ERR_PARAM_ERROR, errorMsg.toString());
    }

    /**
     * 绑定异常
     *
     * @param e 绑定异常
     * @return 默认错误响应数据
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    private RespData<?> bindExceptionHandler(BindException e) {
        StringBuffer errorMsg = new StringBuffer();
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        errors.stream().forEach(x -> {
            if (errorMsg.length() != 0) {
                errorMsg.append(";");
            }
            errorMsg.append(x.getDefaultMessage());
        });
        return RespData.getInstance().failed(ReturnCodeEnum.ERR_PARAM_ERROR, errorMsg.toString());
    }

    /**
     * 请求参数类型不匹配异常
     *
     * @param e 请求参数类型不匹配异常
     * @return 默认错误响应数据
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.OK)
    private RespData<?> methodArgumentTypeMismatchExceptionHandler(
            MethodArgumentTypeMismatchException e) {
        return RespData.getInstance()
                .failed(ReturnCodeEnum.ERR_PARAM_ERROR,
                        messageSourceService.getMessage(ReturnCodeEnum.ERR_PARAM_ERROR));
    }

    /**
     * 上传文件流数据异常
     *
     * @param e 上传文件流数据异常
     * @return 默认错误响应数据
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.OK)
    private RespData<?> missingServletRequestPartExceptionHandler(MissingServletRequestPartException e) {
        log.error("参数错误", e);
        return RespData.getInstance()
                .failed(ReturnCodeEnum.ERR_PARAM_ERROR,
                        messageSourceService.getMessage(ReturnCodeEnum.ERR_PARAM_ERROR));
    }

    /**
     * 违反约束异常
     *
     * @param e 违反约束异常
     * @return 默认错误响应数据
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public RespData<?> handleValidationException(ConstraintViolationException e) {
        StringBuffer errorMsg = new StringBuffer();
        for (ConstraintViolation<?> s : e.getConstraintViolations()) {
            if (errorMsg.length() != 0) {
                errorMsg.append(";");
            }
            errorMsg.append(s.getMessage());
        }
        return RespData.getInstance().failed(ReturnCodeEnum.ERR_PARAM_ERROR, errorMsg.toString());
    }

    /**
     * 请求不可读异常
     *
     * @param e 违反约束异常
     * @return 默认错误响应数据
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    private RespData<?> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        log.error("参数错误", e);
        return RespData.getInstance()
                .failed(ReturnCodeEnum.ERR_PARAM_ERROR,
                        messageSourceService.getMessage(ReturnCodeEnum.ERR_PARAM_ERROR));
    }

    /**
     * 文件流不支持异常
     *
     * @param e 文件流不支持异常
     * @return 默认错误响应数据
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    private RespData<?> httpMediaTypeNotSupportedExceptionHandler(HttpMediaTypeNotSupportedException e) {
        log.error("参数错误", e);
        return RespData.getInstance()
                .failed(ReturnCodeEnum.ERR_PARAM_ERROR,
                        messageSourceService.getMessage(ReturnCodeEnum.ERR_PARAM_ERROR));
    }
}
