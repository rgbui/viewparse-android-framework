package com.viewparse.api;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志记录
 */
public class LogUtil {
    public static int i(String tag, String msg) {
        return Log.i(tag,msg);
    }
    public static int i(Object object, String msg) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = df.format(new Date());
        String className = object.getClass().getName();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        int line =  Thread.currentThread().getStackTrace()[1].getLineNumber();
        String info = now+",method:"+methodName+",line:"+line+",msg:"+msg;
        return Log.i(className,info);
    }

    /**
     * 静态方法调用
     * @param msg
     * @return
     */
    public static int i( String msg) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = df.format(new Date());
        String className = Thread.currentThread().getStackTrace()[1].getClassName();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        int line =  Thread.currentThread().getStackTrace()[1].getLineNumber();
        String info = now+",method:"+methodName+",line:"+line+",msg:"+msg;
        return Log.i(className,info);
    }
}
