package com.stejsoftware.zengine.data;

public class Response {
    private final String status;

    private final StatusBar statusBar;

    private final String windowText;

    public Response(String status) {
        this.status = status;
        this.statusBar = new StatusBar();
        this.windowText = "";
    }

    public Response(String status, StatusBar statusBar, String windowText) {
        this.status = status;
        this.statusBar = statusBar;
        this.windowText = windowText;
    }

    public String getStatus() {
        return this.status;
    }

    public StatusBar getStatusBar() {
        return this.statusBar;
    }

    public String getWindowText() {
        return this.windowText;
    }

}