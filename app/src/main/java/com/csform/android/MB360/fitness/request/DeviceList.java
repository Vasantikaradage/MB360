package com.csform.android.MB360.fitness.request;

public class DeviceList {

    private String DevicePLatform;
    private String ConnectedState;
    private String AuthURL;

    public String getDevicePLatform() {
        return DevicePLatform;
    }

    public void setDevicePLatform(String devicePLatform) {
        DevicePLatform = devicePLatform;
    }

    public String getConnectedState() {
        return ConnectedState;
    }

    public void setConnectedState(String connectedState) {
        ConnectedState = connectedState;
    }

    public String getAuthURL() {
        return AuthURL;
    }

    public void setAuthURL(String authURL) {
        AuthURL = authURL;
    }
}
