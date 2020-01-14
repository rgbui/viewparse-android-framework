package com.viewparse.api;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * GPS定位
 * 获取精确位置权限
 * 
 */
public class LocationUtil {

    //临近警告
    public static final String location_proximity_alert = "location_proximity_alert";
    public static String place;
    //位置变化监听器
    private static LocationListener locationListener = null;


    //GPS是否可用
    public static boolean isGpsAble(AppCompatActivity activity) {
        LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ? true : false;
    }

    public static boolean isGpsAble(LocationManager lm) {
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ? true : false;
    }

    //打开GPS位置信息设置页面
    public static void openGpsShow(AppCompatActivity activity) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivityForResult(intent, 0);
    }

    //定义一个更新位置的方法
    public static void updateLocation(AppCompatActivity activity, BridgeWebView webView, CallBackFunction callBack, String channelNamePasv, Location location) {
        if (location != null) {
            com.viewparse.api.Location ln = new com.viewparse.api.Location();
            ln.longitude = location.getLongitude();
            ln.latitude = location.getLatitude();
            ln.altitude = location.getAltitude();
            ln.speed = location.getSpeed();
            ln.bearing = location.getBearing();
            ln.accuracy = location.getAccuracy();
            //向前台发消息
            webView.callHandler(channelNamePasv, new Gson().toJson(ln), new CallBackFunction() {
                @Override
                public void onCallBack(String data) {
                }
            });
        }
    }

    /**
     * 获取当前定位信息
     *
     * @param activity
     */
    public static com.viewparse.api.Location getLocation(AppCompatActivity activity) {
        final LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (!isGpsAble(lm)) {
            openGpsShow(activity);
        }
        //权限检测
        boolean checked = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        com.viewparse.api.Location ln = new com.viewparse.api.Location();
        ln.longitude = location.getLongitude();
        ln.latitude = location.getLatitude();
        ln.altitude = location.getAltitude();
        ln.speed = location.getSpeed();
        ln.bearing = location.getBearing();
        ln.accuracy = location.getAccuracy();
        return ln;
    }

    /**
     * 动态定位
     *
     * @param activity
     */
    public static void location(final AppCompatActivity activity, final BridgeWebView webView, final CallBackFunction callBack, final String channelNamePasv, Long minTime, Float minDistance) {
        final LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (!isGpsAble(lm)) {
            openGpsShow(activity);
        }
        //权限检测
        boolean checked = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        Location lc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        updateLocation(activity,webView,callBack,channelNamePasv, lc);
        //设置间隔两秒获得一次GPS定位信息
        if (minTime == null) minTime = 2000L;
        if (minDistance == null) minDistance = new Float(8);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // 当GPS定位信息发生改变时，更新定位
                updateLocation(activity,webView,callBack,channelNamePasv, location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
                // 当GPS LocationProvider可用时，更新定位
                updateLocation(activity,webView,callBack,channelNamePasv, lm.getLastKnownLocation(provider));
            }

            @Override
            public void onProviderDisabled(String provider) {
                updateLocation(activity,webView,callBack,channelNamePasv, null);
            }
        };
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, locationListener);
    }

    public static void removeLocationListener(AppCompatActivity activity) {
        final LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (locationListener != null) {
            lm.removeUpdates(locationListener);
        }
    }

    /**
     * 临近警告(地理围栏)
     *
     * @param activity
     * @param longitude
     * @param latitude
     * @param radius
     * @param place     地点名称
     */
    void proximityAlert(AppCompatActivity activity, double longitude, double latitude, float radius, String place) {
        Intent intent = new Intent(location_proximity_alert);
        PendingIntent pi = PendingIntent.getBroadcast(activity, -1, intent, 0);
        LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        lm.addProximityAlert(latitude, longitude, radius, -1, pi);
    }

}
