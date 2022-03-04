package com.orainge.tools.spring_boot.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ApiResult", description = "前后端交互数据标准")
public class ApiResult<T> {
    /**
     * 状态码: 成功
     */
    public static final Integer SUCCESS_CODE = 0;

    /**
     * 状态码: 失败
     */
    public static final Integer ERROR_CODE = -1;

    /**
     * 状态码: 未登录
     */
    public static final Integer UNAUTHORIZED_CODE = 401;

    /**
     * 状态码: 无权限
     */
    public static final Integer FORBIDDEN_CODE = 403;

    @JsonProperty(index = 1)
    @ApiModelProperty("结果代码 0: 成功; -1: 失败; 401: 未登录; 403: 无权限")
    private Integer code;

    @JsonProperty(index = 2)
    @ApiModelProperty("结果信息")
    private String message;

    @JsonProperty(index = 3)
    @ApiModelProperty("结果生成时间戳")
    private long timestamp = System.currentTimeMillis();

    @JsonProperty(index = 4)
    @ApiModelProperty("结果数据")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ApiResult<T> setSuccess() {
        this.code = SUCCESS_CODE;
        this.message = "SUCCESS";
        return this;
    }

    public ApiResult<T> setError() {
        this.code = ERROR_CODE;
        this.message = "ERROR";
        return this;
    }

    public static ApiResult<String> success() {
        return new ApiResult<String>().setCode(SUCCESS_CODE).setMessage("SUCCESS");
    }

    public static <T> ApiResult<T> success(Class<T> clazz) {
        return new ApiResult<T>().setCode(SUCCESS_CODE).setMessage("SUCCESS");
    }

    public static ApiResult<String> error() {
        return new ApiResult<String>().setCode(ERROR_CODE).setMessage("ERROR");
    }

    public static <T> ApiResult<T> error(Class<T> clazz) {
        return new ApiResult<T>().setCode(ERROR_CODE).setMessage("ERROR");
    }

    public static ApiResult<String> forbidden() {
        return new ApiResult<String>().setCode(FORBIDDEN_CODE).setMessage("FORBIDDEN");
    }

    public static <T> ApiResult<T> forbidden(Class<T> clazz) {
        return new ApiResult<T>().setCode(FORBIDDEN_CODE).setMessage("FORBIDDEN");
    }

    public static ApiResult<String> unauthorized() {
        return new ApiResult<String>().setCode(UNAUTHORIZED_CODE).setMessage("UNAUTHORIZED");
    }

    public static <T> ApiResult<T> unauthorized(Class<T> clazz) {
        return new ApiResult<T>().setCode(UNAUTHORIZED_CODE).setMessage("UNAUTHORIZED");
    }

    public String getMessage() {
        return message;
    }

    public ApiResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public ApiResult<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ApiResult<T> setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public T getData() {
        return data;
    }

    public ApiResult<T> setData(T data) {
        this.data = data;
        return this;
    }
}