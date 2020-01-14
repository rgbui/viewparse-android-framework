package com.viewparse;

import android.content.Intent;
import android.content.pm.PackageManager;

import com.viewparse.api.LogUtil;
import com.viewparse.callback.ActivityCallback;
import com.viewparse.callback.PermissionActivityCallback;
import com.viewparse.callback.PermissionCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.app.Activity.RESULT_OK;

/**
 * 请求回调分发
 */
public class RequestHelper {
    private AppCompatActivity activity;
    private int requestCode;
    private String[] permissions = new String[]{};

    private boolean activityOk = false;

    //请求回调
    PermissionCallback permissionCallback = null;
    ActivityCallback activityCallback = null;
    PermissionActivityCallback permissionActivityCallback = null;

    public RequestHelper(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }

    private boolean hasPermission() {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void request(int requestCode,PermissionCallback permissionCallback) {
        this.requestCode = requestCode;
        this.permissionCallback = permissionCallback;
        if (hasPermission() == false) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        } else {
            permissionCallback.onPermissionGranted();
        }
    }

    public void request(int requestCode,PermissionActivityCallback callback) {
        this.requestCode = requestCode;
        this.permissionActivityCallback = callback;
        if (hasPermission() == false) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        } else {
            this.permissionActivityCallback.onPermissionGranted();
        }
    }

    public void request(int requestCode,ActivityCallback activityCallback) {
        this.requestCode = requestCode;
        this.activityCallback = activityCallback;
        activityCallback.onStart();
    }



    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == this.requestCode) {
            boolean denied = false;
            List<String> grantedPermissions = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) denied = true;
                else grantedPermissions.add(permissions[i]);
            }
            if (denied) {
                LogUtil.i(this, "Permission Denied");
                permissionCallback.onPermissionDenied();
            } else {
                permissionCallback.onPermissionGranted();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == this.requestCode) {
            if (resultCode == RESULT_OK) {
                if (activityCallback != null) activityCallback.onActivityOk(requestCode,resultCode,data);
                else if (permissionActivityCallback != null) {
                    if (hasPermission() == true)
                        permissionActivityCallback.onActivityOk(requestCode, resultCode, data);
                    else permissionActivityCallback.onPermissionDenied();
                }
            } else {
                if (activityCallback != null) activityCallback.onActivityNo(requestCode,resultCode,data);
                else if (permissionActivityCallback != null) {
                    if (hasPermission() == true)
                        permissionActivityCallback.onActivityNo(requestCode, resultCode, data);
                    else permissionActivityCallback.onPermissionDenied();
                }
            }
        }
        else LogUtil.i(this,"requestCode not match");
    }
}
