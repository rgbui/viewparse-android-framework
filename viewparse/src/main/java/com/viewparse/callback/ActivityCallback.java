package com.viewparse.callback;

import android.content.Intent;

import androidx.annotation.Nullable;

public interface ActivityCallback {
    void onStart();
    void onActivityOk(int requestCode, int resultCode, @Nullable Intent data);
    void onActivityNo(int requestCode, int resultCode, @Nullable Intent data);
}
