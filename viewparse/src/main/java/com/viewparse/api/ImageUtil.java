package com.viewparse.api;

import android.media.ExifInterface;

import java.io.File;
import java.io.FileInputStream;

/**
 * 图像
 */
class Image {
    public String name;
    public int size;
    public int width;
    public int height;
    //旋转角度
    public int orientation;
    //拍摄时间
    public String dateTime;
    //经纬度 拍摄时是否开启位置
    public double longitude;
    public double latitude;
}

/**
 * 图像处理
 */
public class ImageUtil {
    /**
     * 获取图像信息
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static Image getImageInfo(String path) throws Exception {
        Image image = new Image();
        ExifInterface exifInterface = new ExifInterface(path);
        image.height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0);
        image.width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);
        image.orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
        image.longitude = exifInterface.getAttributeDouble(ExifInterface.TAG_GPS_LONGITUDE, 0);
        image.latitude = exifInterface.getAttributeDouble(ExifInterface.TAG_GPS_LATITUDE, 0);
        File file = new File(path);
        image.name = file.getName();
        FileInputStream fs = new FileInputStream(file);
        image.size = fs.available();
        fs.close();
        return image;
    }


}
