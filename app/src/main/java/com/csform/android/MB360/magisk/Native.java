package com.csform.android.MB360.magisk;

class Native {

    static {
        System.loadLibrary("native-lib");
    }

    static native boolean isMagiskPresentNative();

}