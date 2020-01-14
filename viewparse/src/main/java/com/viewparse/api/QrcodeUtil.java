package com.viewparse.api;

import android.content.Intent;

import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 二维码工具包
 * 二维码,条码
 * https://github.com/zxing/zxing
 * https://github.com/yuzhiqiang1993/zxing
 */
public class QrcodeUtil {
    /**
     * 跳转扫码窗口
     */
    public static void showScanQrcode(AppCompatActivity activity, int requestCode) {
        Intent intent = new Intent(activity, CaptureActivity.class);
        /*ZxingConfig是配置类
         *可以设置是否显示底部布局，闪光灯，相册，
         * 是否播放提示音  震动
         * 设置扫描框颜色等
         * 也可以不传这个参数
         * */
        ZxingConfig config = new ZxingConfig();
        // config.setPlayBeep(false);//是否播放扫描声音 默认为true
        //  config.setShake(false);//是否震动  默认为true
        // config.setDecodeBarCode(false);//是否扫描条形码 默认为true
        // config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
        //config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
        //config.setFrameLineColor(activity.R.color.green);
        // config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 返回扫码内容
     * @param data
     * @return
     */
    public static String getQrcode(Intent data){
        String content = data.getStringExtra(Constant.CODED_CONTENT);
        return content;
    }
}
