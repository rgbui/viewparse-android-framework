package com.viewparse;

import android.Manifest;
import android.content.Intent;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viewparse.api.AlbumUtil;
import com.viewparse.api.CameraUtil;
import com.viewparse.api.CommonUtil;
import com.viewparse.api.Contact;
import com.viewparse.api.ContactUtil;
import com.viewparse.api.DeviceUtil;
import com.viewparse.api.FileUtil;
import com.viewparse.api.FlashlightUtil;
import com.viewparse.api.ImageUtil;
import com.viewparse.api.IntenetUtil;
import com.viewparse.api.LocationUtil;
import com.viewparse.api.LogUtil;
import com.viewparse.api.PhoneUtil;
import com.viewparse.api.QrcodeUtil;
import com.viewparse.api.RecordUtil;
import com.viewparse.api.SimUtil;
import com.viewparse.api.SmsUtil;
import com.viewparse.api.StorageUtil;
import com.viewparse.api.WallpaperUtil;
import com.viewparse.api.WifiUtil;
import com.viewparse.callback.ActivityCallback;
import com.viewparse.callback.PermissionActivityCallback;
import com.viewparse.callback.PermissionCallback;

import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 请求处理
 */
public class Factory {
    /**
     * 请求处理
     *
     * @param activity
     * @param request
     * @param requestHelper
     * @return
     */
    public static void requestHandler(final AppCompatActivity activity, Request request, RequestHelper requestHelper, final BridgeWebView bridgeWebView, final CallBackFunction callBack, final String channelNamePasv) {
        final Map<String, Object> reqDataMap = request.getData();
        final AppCompatActivity Activity = activity;
        final Request Request = request;
        final CallBackFunction CallBack = callBack;
        final Response response = new Response();
        try {
            switch (request.url) {
                case "/phone/call":
                    requestHelper.setPermissions(new String[]{Manifest.permission.CALL_PHONE});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            String phone = (String) reqDataMap.get("phone");
                            PhoneUtil.call(Activity, phone);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "phone call permission deny";
                            response.set(false, message);
                            CallBack.onCallBack(response.toJson());
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/phone/show/call":
                    String phone = (String) reqDataMap.get("phone");
                    PhoneUtil.showCall(activity, phone);
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/contact/query":
                    requestHelper.setPermissions(new String[]{Manifest.permission.READ_CONTACTS});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            String phone = (String) reqDataMap.get("phone");
                            response.result = ContactUtil.getContacts(Activity, null, null);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "contact query permission deny";
                            CallBack.onCallBack(message);
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/contact/query/name":
                    requestHelper.setPermissions(new String[]{Manifest.permission.READ_CONTACTS});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            String name = (String) reqDataMap.get("name");
                            response.result = ContactUtil.getContacts(Activity, name, null);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "contact query name permission deny";
                            CallBack.onCallBack(message);
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/contact/query/phone":
                    requestHelper.setPermissions(new String[]{Manifest.permission.READ_CONTACTS});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            String phone = (String) reqDataMap.get("phone");
                            response.result = ContactUtil.getContacts(Activity, null, phone);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "contact query phone permission deny";
                            CallBack.onCallBack(message);
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/contact/add":
                    requestHelper.setPermissions(new String[]{Manifest.permission.WRITE_CONTACTS});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            Contact contact = Request.getData(Contact.class);
                            ContactUtil.addContact(Activity, contact);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "contact add  permission deny";
                            CallBack.onCallBack(message);
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/contact/update":
                    requestHelper.setPermissions(new String[]{Manifest.permission.WRITE_CONTACTS});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            Contact contact = Request.getData(Contact.class);
                            ContactUtil.updateContact(Activity, contact);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "contact update permission deny";
                            CallBack.onCallBack(message);
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/contact/delete/id":
                    requestHelper.setPermissions(new String[]{Manifest.permission.WRITE_CONTACTS});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            String id = new Double((Double) reqDataMap.get("id")).longValue() + "";
                            ContactUtil.deleteContactsById(Activity, id);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "contact delete permission deny";
                            CallBack.onCallBack(message);
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/contact/delete/ids":
                    requestHelper.setPermissions(new String[]{Manifest.permission.WRITE_CONTACTS});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            String jsonArray = (String) reqDataMap.get("ids");
                            List<String> ids = new Gson().fromJson(jsonArray, new TypeToken<List<String>>() {
                            }.getType());
                            ContactUtil.deleteContactsByIds(Activity, ids);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "contact delete ids permission deny";
                            CallBack.onCallBack(message);
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/contact/delete/name":
                    requestHelper.setPermissions(new String[]{Manifest.permission.WRITE_CONTACTS});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            String name = (String) reqDataMap.get("name");
                            ContactUtil.deleteContactsByName(Activity, name);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "contact delete name permission deny";
                            CallBack.onCallBack(message);
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/sim/info/get":
                    requestHelper.setPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            response.result = SimUtil.getSimInfo(Activity);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "sim info get permission deny";
                            CallBack.onCallBack(message);
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/wifi/get/connected":
                    requestHelper.setPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            response.result = WifiUtil.getConnectedWifi(Activity);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "wifi get connected permission deny";
                            CallBack.onCallBack(message);
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/wifi/scan/list":
                    requestHelper.setPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            response.result = WifiUtil.getScanWifiList(Activity);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "wifi scan list permission deny";
                            CallBack.onCallBack(message);
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/wifi/connect":
                    requestHelper.setPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.CHANGE_WIFI_STATE});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            String SSID = (String) reqDataMap.get("SSID");
                            String password = (String) reqDataMap.get("password");
                            Map<String, Object> resultMap = WifiUtil.connectWifi(Activity, SSID, password);
                            response.success = (Boolean) resultMap.get("success");
                            response.message = (String) resultMap.get("reason");
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "wifi connect permission deny";
                            CallBack.onCallBack(message);
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/camera/take/photo":
                    requestHelper.setPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE});
                    requestHelper.request(1000, new PermissionActivityCallback() {
                        @Override
                        public void onPermissionGranted() {
                            String type = (String) reqDataMap.get("type");
                            CameraUtil.startCamera(Activity, type, 1000);
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "camera take photo permission deny";
                            CallBack.onCallBack(message);
                            LogUtil.i(message);
                        }

                        @Override
                        public void onActivityOk(int requestCode, int resultCode, @Nullable Intent data) {
                            String type = (String) reqDataMap.get("type");
                            response.result = CameraUtil.getCamera(data, type);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onActivityNo(int requestCode, int resultCode, @Nullable Intent data) {
                            String message = "camera take photo onActivityNo";
                            CallBack.onCallBack(message);
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/camera/take/video":
                    requestHelper.setPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE});
                    requestHelper.request(1000, new PermissionActivityCallback() {
                        @Override
                        public void onPermissionGranted() {
                            String type = (String) reqDataMap.get("type");
                            CameraUtil.startVideoCamera(Activity, 1000);
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "camera take video permission deny";
                            response.set(false, message);
                            CallBack.onCallBack(response.toJson());
                            LogUtil.i(message);
                        }

                        @Override
                        public void onActivityOk(int requestCode, int resultCode, @Nullable Intent data) {
                            String type = (String) reqDataMap.get("type");
                            response.result = CameraUtil.getCamera(data, type);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onActivityNo(int requestCode, int resultCode, @Nullable Intent data) {
                            String message = "camera take video onActivityNo";
                            response.set(false, message);
                            CallBack.onCallBack(response.toJson());
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/getSystemInfo":
                    response.result = DeviceUtil.getSystemInfo();
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/vibrate/pattern/repeat":
                    requestHelper.setPermissions(new String[]{Manifest.permission.VIBRATE});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            long[] pattern = CommonUtil.toLongArray((List<Double>) reqDataMap.get("pattern"));
                            int repeat = new Double((Double) reqDataMap.get("repeat")).intValue();
                            DeviceUtil.vibrate(Activity, pattern, repeat);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "vibrate pattern repeat permission deny";
                            response.set(false, message);
                            CallBack.onCallBack(response.toJson());
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/vibrate/pattern":
                    requestHelper.setPermissions(new String[]{Manifest.permission.VIBRATE});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            String patternStr = (String) reqDataMap.get("pattern");
                            DeviceUtil.vibrate(Activity, patternStr);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "vibrate pattern permission deny";
                            response.set(false, message);
                            CallBack.onCallBack(response.toJson());
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/vibrate/cancel":
                    DeviceUtil.cancelVibrate(activity);
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/vibrate/check":
                    response.result = DeviceUtil.checkVibrate(activity);
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/getAllSensors":
                    response.result = DeviceUtil.getAllSensors(activity);
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/clipboard/get":
                    response.result = DeviceUtil.getClipBoardData(activity);
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/clipboard/set":
                    String content = (String) reqDataMap.get("content");
                    DeviceUtil.setClipboardData(activity, content);
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/screen/getBrightness":
                    response.result = DeviceUtil.getScreenBrightness(activity);
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/screen/setBrightness":
                    int brightness = new Double((Double) reqDataMap.get("brightness")).intValue();
                    DeviceUtil.setScreenBrightness(activity, brightness);
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/screen/getInfo":
                    response.result = DeviceUtil.getScreenInfo(activity);
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/screen/keepOn":
                    DeviceUtil.keepScreenOn(activity);
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/openUrl":
                    String url = (String) reqDataMap.get("url");
                    DeviceUtil.openUrl(activity, url);
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/file/choose":
                    requestHelper.request(1000, new ActivityCallback() {
                        @Override
                        public void onStart() {
                            String type = (String) reqDataMap.get("type");
                            Boolean multiple = (Boolean) reqDataMap.get("multiple");
                            FileUtil.showChooseFile(Activity, type, multiple);
                        }

                        @Override
                        public void onActivityOk(int requestCode, int resultCode, @Nullable Intent data) {
                            response.result = FileUtil.getChoosedFilePath(Activity, data);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onActivityNo(int requestCode, int resultCode, @Nullable Intent data) {
                            String message = "file choose activity cancel";
                            response.set(false, message);
                            CallBack.onCallBack(message);
                        }
                    });
                    break;
                case "/getNetworkType":
                    response.result = IntenetUtil.getNetworkState(activity);
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/location/get":
                    requestHelper.setPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            response.result = LocationUtil.getLocation(Activity);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "location get permission deny";
                            response.set(false, message);
                            CallBack.onCallBack(response.toJson());
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/location/addLocationListener":
                    requestHelper.setPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            LocationUtil.location(activity, bridgeWebView, CallBack, channelNamePasv, null, null);
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "location addLocationListener permission deny";
                            response.set(false, message);
                            CallBack.onCallBack(response.toJson());
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/location/removeLocationListener":
                    LocationUtil.removeLocationListener(activity);
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/location/show/set":
                    LocationUtil.openGpsShow(activity);
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/record/start":
                    requestHelper.setPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            RecordUtil.startRecord();
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "record start permission deny";
                            response.set(false, message);
                            CallBack.onCallBack(response.toJson());
                        }
                    });
                    break;
                case "/record/stop":
                    response.result = RecordUtil.stopRecord();
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/sms/query":
                    requestHelper.setPermissions(new String[]{Manifest.permission.READ_SMS});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            String phone = (String) reqDataMap.get("phone");
                            String content = (String) reqDataMap.get("content");
                            response.result = SmsUtil.getSms(activity, phone, content);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "sms query permission deny";
                            response.set(false, message);
                            CallBack.onCallBack(response.toJson());
                        }
                    });
                    break;
                case "/sms/show/send":
                    phone = (String) reqDataMap.get("phone");
                    content = (String) reqDataMap.get("content");
                    SmsUtil.showSendSms(activity, phone, content);
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/sms/send":
                    requestHelper.setPermissions(new String[]{Manifest.permission.SEND_SMS});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            String phone = (String) reqDataMap.get("phone");
                            String content = (String) reqDataMap.get("content");
                            SmsUtil.sendSms(activity, phone, content, false);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "sms send permission deny";
                            response.set(false, message);
                            CallBack.onCallBack(response.toJson());
                            LogUtil.i(message);
                        }
                    });
                    break;
                case "/storage/external/exist":
                    response.result = StorageUtil.externalMemoryAvailable();
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/storage/external/left/size":
                    response.result = StorageUtil.getAvailableExternalMemorySize();
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/storage/external/total/size":
                    response.result = StorageUtil.getTotalExternalMemorySize();
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/storage/internal/left/size":
                    response.result = StorageUtil.getAvailableInternalMemorySize();
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/storage/internal/total/size":
                    response.result = StorageUtil.getTotalInternalMemorySize();
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/wallpaper/show/set":
                    WallpaperUtil.showWallPaperSet(activity);
                    CallBack.onCallBack(response.toJson());
                    break;
                case "/wallpaper/set":
                    requestHelper.setPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_WALLPAPER});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            String path = (String) reqDataMap.get("path");
                            WallpaperUtil.setWallPaper(activity, path);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "wallpaper set permission deny";
                            response.set(false, message);
                            CallBack.onCallBack(response.toJson());
                        }
                    });
                    break;
                case "/wallpaper/lock/set":
                    requestHelper.setPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_WALLPAPER});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            String path = (String) reqDataMap.get("path");
                            WallpaperUtil.setLockWallPaper(activity, path);
                            CallBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "wallpaper lock set permission deny";
                            response.set(false, message);
                            CallBack.onCallBack(response.toJson());
                        }
                    });
                    break;
                case "/saveImageToAlbum":
                    requestHelper.setPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            String path = (String) reqDataMap.get("path");
                            AlbumUtil.saveImageToGallery(Activity, path);
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "saveImageToAlbum permission deny";
                            response.set(false, message);
                            CallBack.onCallBack(response.toJson());
                        }
                    });
                    break;
                case "/saveVideoToAlbum":
                    requestHelper.setPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            String path = (String) reqDataMap.get("path");
                            AlbumUtil.saveVideoToGallery(Activity, path);
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "saveVideoToAlbum permission deny";
                            response.set(false, message);
                            CallBack.onCallBack(response.toJson());
                        }
                    });
                    break;
                case "/scanQrcode":
                    requestHelper.setPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE});
                    requestHelper.request(1000, new PermissionActivityCallback() {
                        @Override
                        public void onPermissionGranted() {
                            QrcodeUtil.showScanQrcode(Activity, 1000);
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "scan qrcode permission deny";
                            response.set(false, message);
                            callBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onActivityOk(int requestCode, int resultCode, @Nullable Intent data) {
                            response.result = QrcodeUtil.getQrcode(data);
                            callBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onActivityNo(int requestCode, int resultCode, @Nullable Intent data) {
                            String message = "scan qrcode activity cancel";
                            response.set(false, message);
                            callBack.onCallBack(response.toJson());
                        }
                    });
                    break;
                case "/light/has":
                    response.result = FlashlightUtil.hasFlashlight(activity);
                    callBack.onCallBack(response.toJson());
                    break;
                case "/light/on":
                    FlashlightUtil.lightsOn(activity);
                    callBack.onCallBack(response.toJson());
                    break;
                case "/light/off":
                    FlashlightUtil.lightsOff(activity);
                    callBack.onCallBack(response.toJson());
                case "/light/isOn":
                    response.result = !FlashlightUtil.isOff();
                    callBack.onCallBack(response.toJson());
                    break;
                case "/light/isSos":
                    response.result = FlashlightUtil.isSos();
                    callBack.onCallBack(response.toJson());
                    break;
                case "/light/sos/on":
                    FlashlightUtil.onSos(activity, null);
                    callBack.onCallBack(response.toJson());
                    break;
                case "/light/sos/off":
                    FlashlightUtil.offSos();
                    callBack.onCallBack(response.toJson());
                    break;
                case "/image/getImageInfo":
                    requestHelper.setPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
                    requestHelper.request(1000, new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            new Gson().toJson(null);
                            String path = (String) reqDataMap.get("path");
                            try {
                                response.result = ImageUtil.getImageInfo(path);
                            } catch (Exception e) {
                                response.set(false, e.getMessage());
                                callBack.onCallBack(response.toJson());
                                LogUtil.i(e.getMessage());
                                return;
                            }
                            callBack.onCallBack(response.toJson());
                        }

                        @Override
                        public void onPermissionDenied() {
                            String message = "getImageInfo permission deny";
                            response.set(false,message);
                            callBack.onCallBack(response.toJson());

                        }
                    });
                    break;
                default:
                    String message = "not found request api";
                    response.set(false, message);
                    LogUtil.i(message);
                    break;
            }
        } catch (Exception e) {
            response.set(false, e.getMessage());
            response.result = null;
            callBack.onCallBack(response.toJson());
            LogUtil.i(e.getMessage());
        }
    }
}
