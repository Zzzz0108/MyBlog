package com.bupt.backend.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;



@Data
@NoArgsConstructor
public class Result<T> implements Serializable {
    private Integer code;   // 状态码
    private String msg;     // 提示信息
    private T data;         // 响应数据

    // 成功响应（无数据）
    public static <T> Result<T> success() {
        return success(null);
    }

    // 成功响应（带数据）
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("success");
        result.setData(data);
        return result;
    }

    // 失败响应
    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
    public static <T> Result<T> fail(String msg) {
        return error(400, msg);
    }

    // 链式调用支持
    public Result<T> code(Integer code) {
        this.code = code;
        return this;
    }

    public Result<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public Result<T> data(T data) {
        this.data = data;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}