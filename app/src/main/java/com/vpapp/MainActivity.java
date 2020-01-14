package com.vpapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.Gson;
import com.viewparse.Factory;
import com.viewparse.MyWebChromeClient;
import com.viewparse.Request;
import com.viewparse.RequestHelper;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static android.view.KeyEvent.KEYCODE_BACK;

public class MainActivity extends AppCompatActivity {

    private final String CHANNEL_NAME = "$$android_sock_js_bridge";
    private final String CHANNEL_NAME_PASV = "$$android_sock_js_bridge_pasv";

    private BridgeWebView webView;
    //前台post请求
    private Request request;
    //响应前台
    private CallBackFunction callBack;
    //请求回调处理
    RequestHelper requestHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestHelper = new RequestHelper(MainActivity.this);
        setContentView(R.layout.activity_main);
        webView = (BridgeWebView) findViewById(R.id.webview);
        webView.setDefaultHandler(new DefaultHandler());
        // 严格模式 检测FileUriExposure异常 调用相机拍视频
        initPhotoError();
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebChromeClient(new MyWebChromeClient(this, requestHelper));
        webView.loadUrl("file:///android_asset/web/index.html");
        //接收监听前台消息
        webView.registerHandler(CHANNEL_NAME, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction callBack) {
                Request request = Request.from(data);
                MainActivity.this.request = request;
                MainActivity.this.callBack = callBack;
                Factory.requestHandler(MainActivity.this, request,requestHelper, webView, callBack, CHANNEL_NAME_PASV);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击回退键时，不会退出浏览器而是返回网页上一页
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //向前台发消息
    public void send(String url, Object data, CallBackFunction callBack) {
        Map map = new HashMap();
        map.put("url", url);
        map.put("data", data);
        webView.callHandler(CHANNEL_NAME_PASV, new Gson().toJson(map), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
            }
        });
    }

    /**
     * 请求权限授权回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        requestHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    /**
     * 跳转的activity回调
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        requestHelper.onActivityResult(requestCode,resultCode,intent);
    }

    /**
     * 关闭广播
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 检测exposed beyond app异常 解决7.0 拍视频崩溃问题
     */
    private void initPhotoError() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }
}
