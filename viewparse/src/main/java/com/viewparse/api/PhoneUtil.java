package com.viewparse.api;

import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 电话
 */
public class PhoneUtil {
    //跳转到拨号界面
    public static void showCall(AppCompatActivity activity, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /**
     * 直接拨打电话
     * 打电话权限
     * @param activity
     * @param phone
     */
    public static void call(AppCompatActivity activity, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        activity.startActivity(intent);
    }
}
