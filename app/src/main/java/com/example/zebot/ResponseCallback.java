package com.example.zebot;

public interface ResponseCallback {
    void onResponse(String response);

    void onError(Throwable throwable);


}
