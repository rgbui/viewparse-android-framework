package com.viewparse.api;

/**
 * 手机SIM卡信息
 * 参考:
 * https://www.cnblogs.com/wzjhoutai/p/7366526.html
 * 手机制式:https://www.cnblogs.com/tangxl/articles/2025506.html
 */
public class Sim {
    //设备编号
    public String deviceId;
    //手机制式 {CDMA,GSM,未知}
    public String phoneType;
    //设备软件版本号
    public String deviceSoftwareVersion;
    //设备当前位置
    public CellLocation cellLocation;
    //本机号码
    public String number;
    //运营商国家代码
    public String networkCountryIso;
    //运营商代号
    public String networkOperator;
    //运营商名称
    public String networkOperatorName;
    //网络类型 {"未知", "2G", "3G", "4G"}
    public String networkType;
    //SIM卡的国别
    public String simCountryIso;
    //sim卡序列号
    public String simSerialNumber;
    //SIM卡状态 "{状态未知", "无SIM卡", "被PIN加锁", "被PUK加锁", "被NetWork PIN加锁", "已准备好"}
    public String simState;


}

/**
 * 基站位置信息
 * 通过基站进行定位：https://blog.csdn.net/chenxiaoping1993/article/details/77862785
 */
class CellLocation {
    //移动联通 gsm基站识别标号 cid
    public int cid;
    //移动联通 gsm网络编号 lac
    public int lac;
    //电信 cdma  基站识别标号 BID
    public int baseStationId;
    //电信 cdma 网络编号NID
    public int networkId;
    //电信 cdma 系统标识号 用谷歌API的话cdma网络的mnc要用这个getSystemId()取得→SID
    public int systemId;
}
