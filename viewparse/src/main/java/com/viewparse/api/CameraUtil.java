package com.viewparse.api;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 相机拍照
 * 参考:https://www.jianshu.com/p/bffd9868cbb5
 * 录制视频:https://www.jianshu.com/p/04809234bf19
 */
public class CameraUtil {
    //文件夹根路径
    private static final String picDirPath = "viewparse/camera/image";
    private static final String videoDirPath = "viewparse/camera/video";
    //图片存放位置
    private static String picPath;
    //视频存放位置
    private static String videoPath;

    //生成照片的名字
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return "IMG_" + format.format(date);
    }

    /*
     * bitmap转base64
     * */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 跳转系统相机拍摄页面
     *
     * @param activity
     * @param type        thumbnail,original 缩略图,原图
     * @param requestCode 请求码
     */
    public static void startCamera(AppCompatActivity activity, String type, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (type.equals("original")) {
            //获取默认存储地址，而且指定文件夹
            String path = Environment.getExternalStorageDirectory().getPath() + "/" + picDirPath;
            File dir = new File(path);
            if (!dir.exists()) dir.mkdir();
            picPath = path + File.separator + getPhotoFileName() + ".jpg";
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, picPath);
            Uri uri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取拍摄图片数据
     *
     * @param intent
     * @param type
     * @return
     */
    public static Camera getCamera(Intent intent, String type) {
        Camera camera = new Camera();
        if (type.equals("thumbnail")) {
            // 解析返回的图片成bitmap
            Bitmap bmp = (Bitmap) intent.getExtras().get("data");
            camera.data = bitmapToBase64(bmp);
        } else if (type.equals("original")) {
            try {
                FileInputStream fis = new FileInputStream(CameraUtil.picPath);
                Bitmap bmp = BitmapFactory.decodeStream(fis);
                camera.data = bitmapToBase64(bmp);
                camera.path = picPath;
            } catch (IOException e) {
                LogUtil.i(e.getMessage());
            }
        }
        return camera;
    }

    /**
     * 跳转系统相机拍摄页面
     *
     * @param activity
     */
    public static void startVideoCamera(AppCompatActivity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
        String path = Environment.getExternalStorageDirectory().getPath() + "/" + videoDirPath;
        File dir = new File(path);
        if (!dir.exists()) dir.mkdir();
        videoPath = path + File.separator + System.currentTimeMillis() + ".mp4";
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(videoPath)));
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取拍摄视频数据
     *
     * @param intent
     * @return
     */
    public static Camera getVideoCamera(Intent intent) {
        Camera camera = new Camera();
        camera.path = videoPath;
        return camera;
    }

}
