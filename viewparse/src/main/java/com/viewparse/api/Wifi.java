package com.viewparse.api;

/**
 * Wifi
 */
public class Wifi {
    //描述wifi热点的名称
    public String SSID;
    //热点的mac地址，但实际有所不同
    public String BSSID;
    //网络ID
    public int networkId;
    //连接速度
    public int linkSpeed;
    //接受信号强度指示
    public int rssi;
    //IP
    public String ip;
    //MAC地址
    public String macAddress;

    //扫描时 描述了身份验证、密钥管理和访问点支持的加密方案
    public String capabilities;
    //最后被看见 时间戳在微秒 API level 17
    public long timestamp;
    //表示地点名称(如“旧金山机场”)发布的接入点;  API level 23
    public String venueName;
    // 主20 MHz的频率(MHz)。
    public int frequency;
}
