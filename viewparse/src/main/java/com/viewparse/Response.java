package com.viewparse;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求响应
 */
public class Response {
    public boolean success = true;
    public String message = "";
    public Object result = null;
    //是否立即响应
    public boolean isResponse = false;

    public Response(){
    }
    public Response(boolean success,String message){
        this.success = success;
        this.message = message;
    }
    public void set(boolean success,String message){
        this.success = success;
        this.message = message;
    }
    public String toJson() {
        Map<String,Object> map = new HashMap<>();
        map.put("success",success);
        map.put("message",message);
        map.put("result",result);
        return new Gson().toJson(map);
    }
}
