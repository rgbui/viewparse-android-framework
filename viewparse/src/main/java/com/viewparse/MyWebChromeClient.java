package com.viewparse;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.viewparse.api.Camera;
import com.viewparse.api.CameraUtil;
import com.viewparse.api.FileUtil;
import com.viewparse.api.LogUtil;
import com.viewparse.callback.ActivityCallback;
import com.viewparse.callback.PermissionActivityCallback;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MyWebChromeClient extends WebChromeClient {
    private AppCompatActivity activity;
    private RequestHelper requestHelper;
    //文件上传
    //5.0以前的版本
    private ValueCallback<Uri> mUploadMessage;
    //5.0以后的版本
    private ValueCallback<Uri[]> uploadMessage;

    public MyWebChromeClient(AppCompatActivity activity, RequestHelper requestHelper) {
        this.activity = activity;
        this.requestHelper = requestHelper;
    }

    // For 3.0+ Devices (Start)
    // onActivityResult attached before constructor
    protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
        mUploadMessage = uploadMsg;
        upload(false, acceptType, false);
    }

    // For Lollipop 5.0+ Devices
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        uploadMessage = filePathCallback;
        //解析accept
        String[] acceptTypes = fileChooserParams.getAcceptTypes();
        String accept = "";
        if (acceptTypes != null && acceptTypes.length > 0) accept = acceptTypes[0];
        //是否支持多选
        boolean multiple = fileChooserParams.getMode() == FileChooserParams.MODE_OPEN_MULTIPLE;
        upload(true, accept, multiple);
        return true;
    }

    //For Android 4.1 only
    protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        openFileChooser(uploadMsg, acceptType);
    }

    protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooser(uploadMsg, "");
    }

    protected void upload(boolean aboveAndroid5, String accept, boolean multiple) {
        //提取动作action  choose/image/video/record 选择文件/拍照片/拍视频/录音
        String action = "choose";
        String typeAction = "";
        if (!accept.equals("")) {
            String[] types = accept.split(",");
            for (int i = 0; i < types.length; i++) {
                if (types[i].contains("action/")) {
                    action = types[i].substring(types[i].indexOf("/") + 1);
                    typeAction = types[i];
                    break;
                }
            }
        }
        //有效accept
        accept = accept.replace(typeAction + ",", "");
        final boolean AboveAndroid5 = aboveAndroid5;
        final String Accept = accept;
        final boolean Multiple = multiple;
        switch (action) {
            case "image":
                requestHelper.setPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE});
                requestHelper.request(1000, new PermissionActivityCallback() {
                    @Override
                    public void onPermissionGranted() {
                        CameraUtil.startCamera(activity, "original",1000);
                    }

                    @Override
                    public void onPermissionDenied() {
                        LogUtil.i("take image permission deny");
                    }

                    @Override
                    public void onActivityOk(int requestCode, int resultCode, @Nullable Intent data) {
                        Camera camera = CameraUtil.getCamera(data, "original");
                        Uri uri = Uri.fromFile(new File(camera.path));
                        if (AboveAndroid5 == true) {
                            if (uploadMessage == null) return;
                            uploadMessage.onReceiveValue(new Uri[]{uri});
                        } else {
                            if (mUploadMessage == null) return;
                            mUploadMessage.onReceiveValue(uri);
                        }
                    }

                    @Override
                    public void onActivityNo(int requestCode, int resultCode, @Nullable Intent data) {
                        LogUtil.i("take image cancel");
                    }
                });
                break;
            case "video":
                requestHelper.setPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE});
                requestHelper.request(1000, new PermissionActivityCallback() {
                    @Override
                    public void onPermissionGranted() {
                        CameraUtil.startVideoCamera(activity,1000);
                    }

                    @Override
                    public void onPermissionDenied() {
                        LogUtil.i("take video permission deny");
                    }

                    @Override
                    public void onActivityOk(int requestCode, int resultCode, @Nullable Intent data) {
                        Camera camera = CameraUtil.getVideoCamera(data);
                        Uri uri = Uri.fromFile(new File(camera.path));
                        if (AboveAndroid5 == true) {
                            if (uploadMessage == null) return;
                            uploadMessage.onReceiveValue(new Uri[]{uri});
                        } else {
                            if (mUploadMessage == null) return;
                            mUploadMessage.onReceiveValue(uri);
                        }
                    }

                    @Override
                    public void onActivityNo(int requestCode, int resultCode, @Nullable Intent data) {
                        LogUtil.i("take video cancel");
                    }
                });
                break;
            case "record":

                break;
            case "choose":
            default:
                requestHelper.request(1000, new ActivityCallback() {
                    @Override
                    public void onStart() {
                        FileUtil.showChooseFile(activity, Accept, Multiple);
                    }

                    @Override
                    public void onActivityOk(int requestCode, int resultCode, @Nullable Intent data) {
                        if (AboveAndroid5 == true) {
                            String[] paths = FileUtil.getChoosedFilePath(activity, data);
                            Uri[] uris = new Uri[paths.length];
                            for (int i = 0; i < uris.length; i++) {
                                uris[i] = Uri.fromFile(new File(paths[i]));
                            }
                            uploadMessage.onReceiveValue(uris);
                        } else mUploadMessage.onReceiveValue(data.getData());
                    }

                    @Override
                    public void onActivityNo(int requestCode, int resultCode, @Nullable Intent data) {
                        LogUtil.i("choose file cancel");
                    }
                });
                break;
        }
    }

}
