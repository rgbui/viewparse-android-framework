package com.viewparse.api;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/*
 * 手电筒（开启闪光灯）工具类
 * 使用前，先申请闪光灯权限 <uses-permission android:name="android.permission.FLASHLIGHT" />
 * 部分手机需要摄像头权限： <uses-permission android:name="android.permission.CAMERA" /> 注：注意android5.0以上权限调用
 * 使用前，请先用hasFlashlight()方法判断设备是否有闪光灯
 * 务必在activity或fragment的onDestroy()方法里调用lightsOff()方法，确保Camera被释放
 *
 * 使用方法
 *  开启sos:new FlashlightUtils().sos()
 *  开启闪光灯: new FlashlightUtils().lightsOn()
 */
public class FlashlightUtil {

    static {
        try {
            Class.forName("android.hardware.Camera");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static Camera mCamera;
    private static CameraManager manager;
    private static boolean isSos = false;


    public static boolean isOff() {
        if (isVersionM()) {
            return manager == null;
        } else
            return mCamera == null;

    }
    /**
     * 判断设备是否有闪光灯
     *
     * @param context
     * @return true 有 false 没有
     */
    public static boolean hasFlashlight(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    //打开手电筒
    public static void lightsOn(Context context) {
        offSos();
        if (hasFlashlight(context)) {
            if (isVersionM()) {
                linghtOn23(context);
            } else {
                lightOn22();
            }
        } else {
            Toast.makeText(context, "您的手机不支持开启闪光灯", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 安卓6.0以上打开手电筒
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void linghtOn23(Context context) {
        try {
            manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            manager.setTorchMode("0", true);// "0"是主闪光灯
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * android6.0以下打开手电筒
     */
    public static void lightOn22() {
        if (mCamera == null) {
            mCamera = Camera.open();
            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(params);
        }
    }

    public static void lightsOff() {
        if (isVersionM()) {
            lightsOff23();
        } else {
            lightsOff22();
        }
    }

    public static void  lightsOff(Context mContext){
        if(context == null) context = mContext;
        lightsOff();
    }

    //安卓6.0以下关闭手电筒
    public static void lightsOff22() {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(params);
            mCamera.release();
            mCamera = null;
        }
    }

    //安卓6.0以上打关闭电筒
    @TargetApi(Build.VERSION_CODES.M)
    public static void lightsOff23() {
        try {
            if (manager == null) {
                return;
            }
            manager.setTorchMode("0", false);
            manager = null;
        } catch (Exception e) {
        }
    }


    private static TimerTask mTimerTask;
    private static Timer mTimer;
    private static int mInt = 0;
    private static Context context;

    /**
     * 打开sos
     *
     * @param aContext
     * @param speed   闪烁速度，建议取值1~6
     */
    public static void onSos(Context aContext, Integer speed) {
        if(speed == null) speed = 3;
        lightsOff();

        if (speed <= 0) {
            throw new RuntimeException("speed不能小于等于0");
        }

        context = aContext;
        if (mTimer != null)
            offSos();

        isSos = true;
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                mInt = mInt == 0 ? 1 : 0;
                message.what = mInt;
                handler.sendMessage(message);
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTimerTask, 0, 1500 / speed);
    }

    //关闭sos
    public static void offSos() {
        isSos = false;
        if (mTimer == null)
            return;
        mTimer.cancel();
        lightsOff();
    }

    public static boolean isSos() {
        return isSos;
    }

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    lightsOn(context);
                    break;
                case 0:
                    lightsOff();
                    break;
            }
        }
    };

    private static boolean isVersionM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

}
