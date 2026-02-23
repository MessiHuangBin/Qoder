package com.example.springbootmybatis.common;

import java.io.Serializable;

/**
 * 统一API响应结果类
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String message;
    private T data;
    private long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public Result(boolean success, String message) {
        this();
        this.success = success;
        this.message = message;
    }

    public Result(boolean success, String message, T data) {
        this(success, message);
        this.data = data;
    }

    public static <T> Result<T> success() {
        return new Result<>(true, "操作成功");
    }

    public static <T> Result<T> success(String message) {
        return new Result<>(true, message);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(true, "操作成功", data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(true, message, data);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(false, message);
    }

    public static <T> Result<T> error(String message, T data) {
        return new Result<>(false, message, data);
    }

    // getter和setter方法
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}