package com.community.common;

import lombok.Data;

/**
 * 统一返回结果类
 */

@Data
public class Result<T>{
    private int code; //200是正常，非200表示异常
    private String msg;  //异常信息
    private T data;   // 返回数据

    public Result(String msg, int code, T data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }
    //成功,有数据
    public static <T> Result<T> success(T data) {
        return new Result<T>("操作成功",200,data);
    }
    //成功,无数据
    public static <T> Result<T> success() {
        return new Result<T>("操作成功",200,null);
    }
    //失败
    public static <T> Result<T> error(String msg) {
        return new Result<T>(msg,500,null);
    }
    //自定义错误
    public static <T> Result<T> error(int code, String msg){
        return new Result<T>(msg,code,null);
    }
}
