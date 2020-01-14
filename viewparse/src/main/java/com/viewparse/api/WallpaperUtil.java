package com.viewparse.api;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;


/**
 * 设置壁纸权限
 */
public class WallpaperUtil {

    //跳转系统选择壁纸界面
    public static void showWallPaperSet(AppCompatActivity activity) {
        Intent chooseIntent = new Intent(Intent.ACTION_SET_WALLPAPER);
        activity.startActivity(Intent.createChooser(chooseIntent, "选择壁纸"));
    }

    /**
     * 设置桌面壁纸
     * 设置桌面壁纸权限
     * @param context
     * @param imageFilesPath
     */
    public static void setWallPaper(AppCompatActivity context, String imageFilesPath) {
        WallpaperManager mWallManager = WallpaperManager.getInstance(context);
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilesPath);
            mWallManager.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置锁屏壁纸
     * @param context
     * @param imageFilesPath
     */
    public static void setLockWallPaper(AppCompatActivity context, String imageFilesPath) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        try {
            InputStream is = new FileInputStream(imageFilesPath);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                int set = wallpaperManager.setStream(is,null,true,WallpaperManager.FLAG_LOCK);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
