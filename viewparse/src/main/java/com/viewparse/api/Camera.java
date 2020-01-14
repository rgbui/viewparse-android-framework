package com.viewparse.api;

/**
 * 相机
 * 拍图片，视频
 */
public class Camera {
    //缩略图,原图 thumbnail,original
    public String type;
    //图片 base64
    public String data;
    //存放路径
    public String path;
    public Camera(){ }
    public Camera(String type,String data){
        this.type = type;
        this.data = data;
    }
    public Camera(String type,String data,String path){
        this.type = type;
        this.data = data;
        this.path = path;
    }
}
