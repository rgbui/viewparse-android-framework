package com.viewparse.api;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 相册
 * 保存图片/视频到相册
 */
public class AlbumUtil {
    /**
     * 保存图片到相册
     *
     * @param context
     * @param imgFile
     * @return
     */
    public static boolean saveImageToGallery(Context context, File imgFile) {
        String fileName = imgFile.getName();
        if (!(fileName.endsWith(".jpg") || fileName.endsWith(".JPG") || fileName.endsWith(".jpeg") || fileName.endsWith(".JPEG")))
            return false;
        try {
            //把文件插入到系统图库
            MediaStore.Images.Media.insertImage(context.getContentResolver(), imgFile.getAbsolutePath(), fileName, null);
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(imgFile);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 保存位图到相册
     *
     * @param context
     * @param bmp
     * @return
     */
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String dirPath = "";
        // 小米手机
        if (Build.BRAND.equals("Xiaomi")) {
            dirPath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera";
        }
        // Meizu 、Oppo
        else {
            dirPath = Environment.getExternalStorageDirectory().getPath() + "/DCIM";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) dir.mkdir();
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(dirPath, fileName);
        boolean success = false;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            success = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();
            saveImageToGallery(context, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 保存图片到相册
     *
     * @param context
     * @param path    绝对路径
     * @return
     */
    public static boolean saveImageToGallery(Context context, String path) {
        File file = new File(path);
        if (!file.exists()) return false;
        return saveImageToGallery(context, file);
    }

    /**
     * 保存视频到相册
     *
     * @param context
     * @param videoFile
     * @return
     */
    public static boolean saveVideoToGallery(Context context, File videoFile) {
        String fileName = videoFile.getName();
        try {
            //把文件插入到系统图库
            MediaStore.Images.Media.insertImage(context.getContentResolver(), videoFile.getAbsolutePath(), fileName, null);
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(videoFile);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 保存视频到相册
     *
     * @param context
     * @param path
     * @return
     */
    public static boolean saveVideoToGallery(Context context, String path) {
        File file = new File(path);
        if (!file.exists()) return false;
        return saveVideoToGallery(context, file);
    }

}

