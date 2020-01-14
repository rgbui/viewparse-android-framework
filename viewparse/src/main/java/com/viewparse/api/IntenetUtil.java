package com.viewparse.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 获取网络类型
 * 访问网络状态权限
 * 访问Wifi状态权限
 */
public class IntenetUtil {
    public static String getNetworkState(AppCompatActivity activity){
        //获取系统的网络服务
        ConnectivityManager connManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        //如果当前没有网络
        if (null == connManager)return "none";
        //获取当前网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) return "none";
        // 判断是不是连接的是不是wifi
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED
                        || state == NetworkInfo.State.CONNECTING) {
                    return "NETWORN_WIFI";
                }
        }
        // 如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null != networkInfo) {
            NetworkInfo.State state = networkInfo.getState();
            String strSubTypeName = networkInfo.getSubtypeName();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED
                        || state == NetworkInfo.State.CONNECTING) {
                    switch (activeNetInfo.getSubtype()) {
                        //如果是2g类型
                        case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                            return "NETWORK_TYPE_GPRS";
                        case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                            return "NETWORK_TYPE_CDMA";
                        case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                            return "NETWORK_TYPE_EDGE";
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                            return "NETWORK_TYPE_1xRTT";
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return "NETWORK_TYPE_IDEN";
                        //如果是3g类型
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                            return "NETWORK_TYPE_EVDO_A";
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                            return "NETWORK_TYPE_UMTS";
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            return "NETWORK_TYPE_EVDO_0";
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                            return "NETWORK_TYPE_HSDPA";
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                            return "NETWORK_TYPE_HSUPA";
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                            return "NETWORK_TYPE_HSPA";
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                            return "NETWORK_TYPE_EVDO_B";
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                            return "NETWORK_TYPE_EHRPD";
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return "NETWORK_TYPE_HSPAP";
                        //如果是4g类型
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return "NETWORK_TYPE_LTE";
                        default:
                            //中国移动 联通 电信 三种3G制式
                            if (strSubTypeName.equalsIgnoreCase("TD-SCDMA")
                                    || strSubTypeName.equalsIgnoreCase("WCDMA")
                                    || strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                return "NETWORN_3G";
                            } else {
                                return "NETWORN_MOBILE";
                            }
                    }
                }
        }
        NetworkInfo EthernetInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if (null != EthernetInfo){
            NetworkInfo.State state = EthernetInfo.getState();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return "NETWORN_ETHERNET";
                }
        }
        return "";
    }
}