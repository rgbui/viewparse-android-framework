package com.viewparse.api;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class DeviceUtil {

    /**
     * 获取系统信息
     *
     * @return
     */
    public static Map<String, Object> getSystemInfo() {
        Map<String, Object> map = new HashMap<>();
        //品牌
        map.put("brand", Build.BRAND);
        //型号
        map.put("model", Build.MODEL);
        //获取当前系统的android版本号
        map.put("version", Build.VERSION.RELEASE);
        return map;
    }

    /**
     * 手机振动
     * 允许振动权限
     *
     * @param activity
     * @param pattern  模式振动 new int[200,400,600,800],就是让他在200,400,600,800这个时间交替启动与关闭振动器
     * @param repeat   重复次数 ,如果是-1的只振动一次,如果是0的话则一直振动
     */
    public static void vibrate(AppCompatActivity activity, long[] pattern, int repeat) {
        Vibrator vb = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vb.vibrate(pattern, repeat);
    }

    /**
     * 手机振动
     * @param activity
     * @param pattern  short,long,rhythm 短振动,长振动,节奏振动
     */
    public static void vibrate(AppCompatActivity activity, String pattern) {
        Vibrator vb = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        switch (pattern) {
            case "short":
                vb.vibrate(new long[]{100, 200, 100, 200}, 0);
                break;
            case "long":
                vb.vibrate(new long[]{100, 100, 100, 1000}, 0);
                break;
            case "rhythm":
                vb.vibrate(new long[]{500, 100, 500, 100, 500, 100}, 0);
                break;
        }
    }


    /**
     * 取消振动
     *
     * @param activity
     */
    public static void cancelVibrate(AppCompatActivity activity) {
        Vibrator vb = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vb.cancel();
    }

    /**
     * 检测设备是否振动
     *
     * @param activity
     */
    public static boolean checkVibrate(AppCompatActivity activity) {
        Vibrator vb = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        return vb.hasVibrator();
    }

    /**
     * 查看自己手机所支持的传感器
     *
     * @param activity
     * @return
     */
    public static List<Map<String, Object>> getAllSensors(AppCompatActivity activity) {
        SensorManager sm = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ALL);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Sensor sensor :
                allSensors) {
            Map<String, Object> map = new HashMap<>();
            //传感器种类
            String type = "";
            switch (sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    type = "加速度传感器";
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    type = "陀螺仪传感器";
                    break;
                case Sensor.TYPE_LIGHT:
                    type = "光线传感器";
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    type = "磁场传感器";
                    break;
                case Sensor.TYPE_ORIENTATION:
                    type = "方向传感器";
                    break;
                case Sensor.TYPE_PRESSURE:
                    type = "气压传感器";
                    break;
                case Sensor.TYPE_PROXIMITY:
                    type = "距离传感器";
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    type = "温度传感器";
                    break;
                default:
                    type = "其他传感器";
                    break;
            }
            map.put("type", type);
            // 传感器名称
            map.put("name", sensor.getName());
            //传感器供应商
            map.put("vendor", sensor.getVendor());
            //传感器版本
            map.put("version", sensor.getVersion());
            //精度值
            map.put("resolution", sensor.getResolution());
            //最大范围
            map.put("maximumRange", sensor.getMaximumRange());
            //传感器使用时的耗电量
            map.put("power", sensor.getPower());
            list.add(map);
        }
        return list;
    }

    /**
     * android中获取粘贴板中的内容
     */
    public static String getClipBoardData(AppCompatActivity context) {
        ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = clipboard.getPrimaryClip();
        String message = "";
        if (clipData != null && clipData.getItemCount() > 0) {
            CharSequence text = clipData.getItemAt(0).getText();
            message  = text.toString();
        }
        return message;
    }

    /**
     * android中设置粘贴板中的内容
     */
    public static void setClipboardData(AppCompatActivity context, String data) {
        ClipData clipData = ClipData.newPlainText("clipboard data", data);
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(clipData);
    }

    /**
     * 获取屏幕亮度
     */
    public static int getScreenBrightness(AppCompatActivity context) {
        int systemBrightness = 0;
        try {
            systemBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return systemBrightness;
    }

    /**
     * 设置屏幕亮度
     */
    public static void setScreenBrightness(AppCompatActivity context, int brightness) {
        Window window = context.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
    }

    /**
     * 获取屏幕信息
     * 宽高
     *
     * @param activity
     * @return
     */
    public static Map<String, Object> getScreenInfo(AppCompatActivity activity) {
        Map<String, Object> map = new HashMap<>();
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        //windows manager可以通过getSystemService获取，也可以在Activity中直接获取
        //val wm = this.windowManager
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)
        map.put("width", screenWidth);
        map.put("height", screenHeight);
        return map;
    }

    /**
     * 设置屏幕保持常亮
     *
     * @param activity
     */
    public static void keepScreenOn(AppCompatActivity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /***
     * 启动android默认浏览器
     * @param context
     * @param url
     */
    public static void openUrl(AppCompatActivity context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
}
