package com.suyashsrijan.londonmeetup.API.tfl.interfaces;

import android.content.Context;

import java.util.ArrayList;

public interface ApiResultCallback<T> {
        void response(Context context, ArrayList<T> output);
        void error(Context context, Exception exception);
}
