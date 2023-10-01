package com.vitalorg.wild.verticles;

import io.vertx.rxjava3.core.http.HttpServerResponse;

import java.io.*;


public class WebUtils {
    public static void redirect(HttpServerResponse response, String target) {
        response.setStatusCode(303);
        response.putHeader("Location", target);
        response.end();
    }

    public static void failMessage(HttpServerResponse response, String message) {
        response.setStatusCode(200);
        response.write("<p style='color:red;'>" + message + "</p>");
    }

    public static String generateHtml(String path, String errMessage) throws IOException {
        File file = new File(path);
        return String.valueOf(file);
    }
}
