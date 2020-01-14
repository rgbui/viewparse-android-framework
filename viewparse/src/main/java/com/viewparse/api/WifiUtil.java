package com.viewparse.api;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

/**
 * wifi
 * 改变网络状态权限
 * 改变WiFi状态权限
 * 获取网络状态权限
 * 获取WiFi状态权限
 */
public class WifiUtil {
    //ip int字节转String
    private static String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    /**
     * 获取当前连接的Wifi
     * 获取WIFI信息状态的权限
     * 获取网络状态改变的权限
     *
     * @param activity
     * @return
     */
    public static Wifi getConnectedWifi(AppCompatActivity activity) {
        WifiManager wm = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wm.getConnectionInfo();
        Wifi wifi = new Wifi();
        wifi.SSID = wifiInfo.getSSID();
        wifi.BSSID = wifiInfo.getBSSID();
        wifi.networkId = wifiInfo.getNetworkId();
        wifi.linkSpeed = wifiInfo.getLinkSpeed();
        wifi.rssi = wifiInfo.getRssi();
        int ip = wifiInfo.getIpAddress();
        wifi.ip = intToIp(ip);
        wifi.macAddress = wifiInfo.getMacAddress();
        return wifi;
    }

    /**
     * 获取扫描的wifi列表
     *
     * @param activity
     * @return
     */
    public static List<Wifi> getScanWifiList(AppCompatActivity activity) {
        WifiManager wm = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> scanList = wm.getScanResults();
        List<Wifi> list = new ArrayList<>();
        for (ScanResult scan :
                scanList) {
            Wifi wifi = new Wifi();
            wifi.BSSID = scan.BSSID;
            wifi.capabilities = scan.capabilities;
            wifi.rssi = scan.level;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                wifi.timestamp = scan.timestamp;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CharSequence venueName = scan.venueName;
                wifi.venueName = scan.venueName.toString();
            }
            wifi.frequency = scan.frequency;
            list.add(wifi);
        }
        return list;
    }

    /**
     * 连接wif
     *
     * @param activity
     * @param SSID
     * @param password
     * @return
     */
    public static Map<String, Object> connectWifi(AppCompatActivity activity, String SSID, String password) {
        WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Map<String, Object> map = new HashMap<>();
        boolean success = false;
        String reason = "";
        try {
            //打开wifi功能
            if (!wifiManager.isWifiEnabled()) {
                boolean openWifi = wifiManager.setWifiEnabled(true);
            }

            //wifi配置文件
            WifiConfiguration config = new WifiConfiguration();
            config.allowedAuthAlgorithms.clear();
            config.allowedGroupCiphers.clear();
            config.allowedKeyManagement.clear();
            config.allowedPairwiseCiphers.clear();
            config.allowedProtocols.clear();
            config.SSID = "\"" + SSID + "\"";

            //wpa模式
            config.preSharedKey = "\"" + password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.status = WifiConfiguration.Status.ENABLED;
            int netID = wifiManager.addNetwork(config);
            boolean enabled = wifiManager.enableNetwork(netID, true);
            success = enabled;
        } catch (Exception e) {
            success = false;
            reason = "连接异常";
        }
        map.put("success",success);
        map.put("reason",reason);
        return map;
    }

    /**
     * 连接wifi
     *
     * @param activity
     * @param SSID
     * @param password
     * @return
     */
    public static boolean connectWifi2(AppCompatActivity activity, String SSID, String password) {
        WifiManager wm = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiConnectManager wifiConnectManager = WifiConnectManager.newInstance(wm);
        WifiConnectManager.WifiCipherType type = WifiConnectManager.WifiCipherType.WIFICIPHER_WPA;
        ConnectAsyncTask task = new ConnectAsyncTask(SSID, password, wifiConnectManager, type);
        boolean result = task.excute();
        return result;
    }
}

/**
 * wifi连接
 */
class WifiConnectManager {

    public static WifiManager wifiManager = null;
    private static WifiConnectManager wifiConnectManager;

    // 定义几种加密方式，一种是WEP，一种是WPA，还有没有密码的情况
    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    // 构造函数
    private WifiConnectManager(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    public static WifiConnectManager newInstance(WifiManager wifiManager) {
        if (wifiConnectManager == null) {
            wifiConnectManager = new WifiConnectManager(wifiManager);
        }
        return wifiConnectManager;
    }

    // 查看以前是否也配置过这个网络
    public WifiConfiguration isExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = wifiManager
                .getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    /**
     * 创建wifi配置文件
     *
     * @param SSID
     * @param Password
     * @param Type
     * @return
     */
    public WifiConfiguration createWifiInfo(String SSID, String Password, WifiCipherType Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        // config.SSID = SSID;
        // nopass
        if (Type == WifiCipherType.WIFICIPHER_NOPASS) {
            // config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            // config.wepTxKeyIndex = 0;
        } else if (Type == WifiCipherType.WIFICIPHER_WEP) {// wep
            if (!TextUtils.isEmpty(Password)) {
                if (isHexWepKey(Password)) {
                    config.wepKeys[0] = Password;
                } else {
                    config.wepKeys[0] = "\"" + Password + "\"";
                }
            }
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else if (Type == WifiCipherType.WIFICIPHER_WPA) {// wpa
            config.preSharedKey = "\"" + Password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    // 打开wifi功能
    public boolean openWifi() {
        boolean bRet = true;
        if (!wifiManager.isWifiEnabled()) {
            bRet = wifiManager.setWifiEnabled(true);
        }
        return bRet;
    }

    // 关闭WIFI
    private void closeWifi() {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    private static boolean isHexWepKey(String wepKey) {
        final int len = wepKey.length();
        // WEP-40, WEP-104, and some vendors using 256-bit WEP (WEP-232?)
        if (len != 10 && len != 26 && len != 58) {
            return false;
        }
        return isHex(wepKey);
    }

    private static boolean isHex(String key) {
        for (int i = key.length() - 1; i >= 0; i--) {
            final char c = key.charAt(i);
            if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a'
                    && c <= 'f')) {
                return false;
            }
        }

        return true;
    }


}

/**
 * 连接执行
 */
class ConnectAsyncTask {
    private String ssid;
    private String password;
    private WifiConnectManager wifiConnectManager;
    private WifiConnectManager.WifiCipherType type;
    WifiConfiguration tempConfig;
    boolean isLinked = false;

    public ConnectAsyncTask(String ssid, String password, WifiConnectManager wifiConnectManager, WifiConnectManager.WifiCipherType type) {
        this.ssid = ssid;
        this.password = password;
        this.wifiConnectManager = wifiConnectManager;
        this.type = type;
    }

    protected Boolean excute() {
        // 打开wifi
        wifiConnectManager.openWifi();
        // 开启wifi功能需要一段时间(我在手机上测试一般需要1-3秒左右)，所以要等到wifi
        // 状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句
        while (wifiConnectManager.wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            try {
                // 为了避免程序一直while循环，让它睡个100毫秒检测……
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                Log.e("wifidemo", ie.toString());
            }
        }

        tempConfig = wifiConnectManager.isExsits(ssid);
        //禁掉所有wifi
        for (WifiConfiguration c : wifiConnectManager.wifiManager.getConfiguredNetworks()) {
            wifiConnectManager.wifiManager.disableNetwork(c.networkId);
        }
        if (tempConfig != null) {
            LogUtil.i("wifidemo xxx:" + ssid + "配置过！");
            boolean result = wifiConnectManager.wifiManager.enableNetwork(tempConfig.networkId, true);
            if (!isLinked && type != WifiConnectManager.WifiCipherType.WIFICIPHER_NOPASS) {
                try {
                    Thread.sleep(5000);//超过5s提示失败
                    if (!isLinked) {
                        LogUtil.i("wifidemo xxx:" + ssid + "连接失败！");
                        wifiConnectManager.wifiManager.disableNetwork(tempConfig.networkId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            LogUtil.i("wifidemo xxx:" + "result=" + result);
            return result;
        } else {
            LogUtil.i("wifidemo xxx:" + ssid + "没有配置过！");
            if (type != WifiConnectManager.WifiCipherType.WIFICIPHER_NOPASS) {
                WifiConfiguration wifiConfig = wifiConnectManager.createWifiInfo(ssid, password,
                        type);
                if (wifiConfig == null) {
                    LogUtil.i("wifidemo wifiConfig is null!");
                    return false;
                }
                int netID = wifiConnectManager.wifiManager.addNetwork(wifiConfig);
                boolean enabled = wifiConnectManager.wifiManager.enableNetwork(netID, true);
                LogUtil.i("wifidemo enableNetwork status enable=" + enabled);

            } else {
                WifiConfiguration wifiConfig = wifiConnectManager.createWifiInfo(ssid, password, type);
                if (wifiConfig == null) {
                    LogUtil.i("wifidemo wifiConfig x2 is null!");
                    return false;
                }
                int netID = wifiConnectManager.wifiManager.addNetwork(wifiConfig);
                boolean enabled = wifiConnectManager.wifiManager.enableNetwork(netID, true);
                LogUtil.i("wifidemo enableNetwork x2 status enable=" + enabled);
                return enabled;
            }
            return false;
        }
    }

}
