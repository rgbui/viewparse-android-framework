package com.viewparse.api;

import com.google.gson.Gson;

import java.util.List;

/**
 * 基础包
 */
public class CommonUtil {

    public static long[] toLongArray(String arrayString){
        double[] doubleArray = new Gson().fromJson(arrayString,double[].class);
        long[] longArray = new long[doubleArray.length];
        for(int i = 0;i<doubleArray.length;i++){
            longArray[i] = new Double(doubleArray[i]).longValue();
        }
        return longArray;
    }
    public static long[] toLongArray(List<Double> list){
        long[] longArray = new long[list.size()];
        int i = 0;
        for (Double d:list
             ) {
            longArray[i] = d.longValue();
            i++;
        }
        return longArray;
    }
}
