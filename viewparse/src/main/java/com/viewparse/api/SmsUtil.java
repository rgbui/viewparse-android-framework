package com.viewparse.api;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 短信
 * 发送短信权限
 * 阅读短信权限
 * 写入短信权限
 * 接收短信权限：广播监听短信的查收
 */
public class SmsUtil {

    //处理返回的发送状态
    public static final String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    //处理返回的接收状态
    public static final String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

    /**
     * 查询短信
     *
     * @param activity
     * @param phone
     * @param content
     * @return
     */
    public static List<Sms> getSms(AppCompatActivity activity, String phone, String content) {
        ContentResolver resolver = activity.getContentResolver();
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
        final Uri SMS_INBOX = Uri.parse("content://sms/");
        Cursor cursor = null;
        if (phone == null && content == null) {
            resolver.query(SMS_INBOX, projection, null, null, "date desc");
        } else if (phone != null) {
            cursor = resolver.query(SMS_INBOX, projection, "body like ?", new String[]{"%" + phone + "%"}, "date desc");
        } else if (content != null) {
            cursor = resolver.query(SMS_INBOX, projection, "body like ?", new String[]{"%" + content + "%"}, "date desc");
        }
        List<Sms> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Sms sms = new Sms();
            sms.id = cursor.getInt(cursor.getColumnIndex("_id"));
            sms.address = cursor.getString(cursor.getColumnIndex("address"));
            sms.person = cursor.getString(cursor.getColumnIndex("person"));
            sms.date = cursor.getInt(cursor.getColumnIndex("date"));
            sms.type = cursor.getInt(cursor.getColumnIndex("type"));
            sms.body = cursor.getString(cursor.getColumnIndex("body"));
            list.add(sms);
        }
        return list;
    }

    //调用系统发送短信界面
    public static void showSendSms(AppCompatActivity activity, String phone, String content) {
        Uri smsToUri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", content);
        activity.startActivity(intent);
    }

    /**
     * 发短信
     * 接收短信权限
     * @param activity
     * @param phone
     * @param message
     * @param hasSendResult 是否监听发送结果
     */
    public static void sendSms(AppCompatActivity activity, String phone, String message, boolean hasSendResult) {
        //获取短信管理器
        SmsManager smsManager = SmsManager.getDefault();
        if(hasSendResult == true){
            //创建接收返回的发送状态的Intent
            Intent sentIntent = new Intent(SENT_SMS_ACTION);
            PendingIntent sentPI = PendingIntent.getBroadcast(activity.getApplicationContext(), 0, sentIntent,  0);
            //创建接收返回的接收状态的Intent
            Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
            PendingIntent deliverPI = PendingIntent.getBroadcast(activity.getApplicationContext(), 0,deliverIntent, 0);
            //广播中接收状态
            smsManager.sendTextMessage(phone, null, message, sentPI, deliverPI);
        }
        else {
            smsManager.sendTextMessage(phone, null, message, null, null);
        }
    }

    /**
     * 删除短信
     * @param activity
     * @param address
     * @param content
     */
    public static int deleteSms(AppCompatActivity activity, String address, String content) {
        // 准备系统短信收信箱的uri地址
        Uri uri = Uri.parse("content://sms/inbox");
        // 查询收信箱里所有的短信
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type", "thread_id"};
        String where = " address = '" + address + "'";
        ContentResolver resolver = activity.getContentResolver();
        Cursor curs = resolver.query(uri, projection, where, null, "date desc");
        int delNum = 0;
        if (curs.moveToFirst()) {
            do {
                String body = curs.getString(curs.getColumnIndex("body")).trim();// 获取信息内容
                if (body.contains(content)) {
                    int id = curs.getInt(curs.getColumnIndex("_id"));
                    delNum +=resolver.delete(Uri.parse("content://sms/"), "_id=?", new String[]{String.valueOf(id)});
                }
            } while (curs.moveToNext());
        }
        curs.close();
        return delNum;
    }

    public static int deleteSms(AppCompatActivity activity, int id) {
        ContentResolver resolver = activity.getContentResolver();
        int delNum = resolver.delete(Uri.parse("content://sms/"), "_id=?", new String[]{String.valueOf(id)});
        return delNum;
    }

}
