package com.viewparse.api;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import androidx.appcompat.app.AppCompatActivity;

public class SimUtil {
    /**
     * 获取sim卡基站定位信息
     * 基站定位权限
     * @param tManager
     * @return
     */
    public static CellLocation getCellLocation(TelephonyManager tManager) {
        CellLocation cellLocation = new CellLocation();
        if (tManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation)
                    tManager.getCellLocation();
            //cdma基站识别标号 BID
            cellLocation.baseStationId = cdmaCellLocation.getBaseStationId();
            //cdma网络编号NID
            cellLocation.networkId = cdmaCellLocation.getNetworkId();
            //cdma 系统标识号
            cellLocation.systemId = cdmaCellLocation.getSystemId();
        } else {
            GsmCellLocation gsmCellLocation = (GsmCellLocation) tManager.getCellLocation();
            //gsm基站识别标号
            cellLocation.cid = gsmCellLocation.getCid();
            //gsm网络编号
            cellLocation.lac = gsmCellLocation.getLac();
        }
        return cellLocation;
    }

    /**
     * 获取Sim卡信息
     * 读取电话状态权限
     * @param activity
     * @return
     */
    public static Sim getSimInfo(AppCompatActivity activity) {
        TelephonyManager tManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        Sim sim = new Sim();
        sim.deviceId = tManager.getDeviceId();
        //手机制式
        String phoneType = "";
        int type = tManager.getPhoneType();
        if (type == TelephonyManager.PHONE_TYPE_CDMA) phoneType = "CDMA";
        else if (type == TelephonyManager.PHONE_TYPE_GSM) phoneType = "GSM";
        else if (type == TelephonyManager.PHONE_TYPE_NONE) phoneType = "未知";
        sim.phoneType = phoneType;
        //设备软件版本号
        sim.deviceSoftwareVersion = tManager.getDeviceSoftwareVersion() != null ? tManager.getDeviceSoftwareVersion() : "未知";
        //本机号码
        sim.number = tManager.getLine1Number();
        //运营商国家代码
        sim.networkCountryIso = tManager.getNetworkCountryIso();
        //运营商代号
        sim.networkOperator = tManager.getNetworkOperator();
        //运营商名称
        sim.networkOperatorName = tManager.getNetworkOperatorName();
        //网络类型
        final String[] networkType = {"未知", "2G", "3G", "4G"};
        sim.networkType = networkType[tManager.getPhoneType()];
        sim.cellLocation = getCellLocation(tManager);
        //SIM卡的国别
        sim.simCountryIso = tManager.getSimCountryIso();
        //SIM卡序列号
        sim.simSerialNumber = tManager.getSimSerialNumber();
        //SIM卡状态
        final String[] simState = {"状态未知", "无SIM卡", "被PIN加锁", "被PUK加锁", "被NetWork PIN加锁", "已准备好"};
        sim.simState = simState[tManager.getSimState()];
        return sim;
    }

}
