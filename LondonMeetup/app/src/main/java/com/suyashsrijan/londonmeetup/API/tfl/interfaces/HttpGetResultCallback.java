package com.suyashsrijan.londonmeetup.API.tfl.interfaces;

public interface HttpGetResultCallback {
    void response(String output);
    void error(Exception exception);
}
