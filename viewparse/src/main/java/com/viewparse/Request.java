package com.viewparse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * 请求
 */
public class Request {
    public String id;
    public String url;
    public String data;

    public Map getData() {
        return new Gson().fromJson(data, Map.class);
    }

    public <T> T getData(Class<T> classOfT) {
        return new Gson().fromJson(data, classOfT);
    }

    public String[] getArrayString() {
        return new Gson().fromJson(data, String[].class);
    }

    public long[] getArrayLong() {
        String[] arr = new Gson().fromJson(data, String[].class);
        long[] newArr = new long[arr.length];
        for(int i = 0;i<arr.length;i++){
            long value = 0l;
            try {
                value = Double.valueOf(arr[i]).longValue();
            } catch (Exception e) {
            }
            newArr[i] = value;
        }
        return newArr;
    }

    public List<String> getListString() {
        List<String> list = new Gson().fromJson(data, new TypeToken<List<String>>() {
        }.getType());
        return list;
    }

    public static Request from(String data) {
        return (Request) new Gson().fromJson(data, Request.class);
    }
}
