package com.viewparse.callback;

import android.content.Intent;

import androidx.annotation.Nullable;

public interface PermissionActivityCallback {
    void onPermissionGranted();
    void onPermissionDenied();
    void onActivityOk(int requestCode, int resultCode, @Nullable Intent data);
    void onActivityNo(int requestCode, int resultCode, @Nullable Intent data);
}
