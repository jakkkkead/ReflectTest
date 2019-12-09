package com.web.bean;

import lombok.Data;

@Data
public class RestResponse<T>{
    private int code;
    private String msg;
    private T data;
    public RestResponse(){

    }
    public RestResponse(T data){
        this.data = data;
    }
    public RestResponse success(){
        this.code = 200;
        this.msg = "操作成功";
        return this;
    }
    public RestResponse success(T data){
        this.code = 200;
        this.msg = "操作成功";
        this.data = data;
        return this;
    }
    public RestResponse success(String msg){
        this.code = 200;
        this.msg = msg;
        return this;
    }
    public RestResponse error(String msg){
        this.code = 500;
        this.msg = msg;
        return this;
    }
    public RestResponse error(){
        this.code = 500;
        this.msg = "操作失败";
        return this;
    }
}
