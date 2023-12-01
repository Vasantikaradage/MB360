package com.csform.android.MB360.fitness.AktivoData;

public interface AktivoMethods {
    void authenticated();
    void gotPermissions();
    void getToken(String token);
    void authenticationFailed();
}
