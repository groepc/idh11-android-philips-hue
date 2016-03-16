package com.perryfaro.hue;

/**
 * Created by Frans on 16-3-2016.
 */
public class HueTaskParams {

    String urlString;
    String action;
    String requestJson;
    String requestMethod;

    HueTaskParams(String urlString, String action, String requestJson, String requestMethod) {
        this.urlString = urlString;
        this.action = action;
        this.requestJson = requestJson;
        this.requestMethod = requestMethod;
    }
}