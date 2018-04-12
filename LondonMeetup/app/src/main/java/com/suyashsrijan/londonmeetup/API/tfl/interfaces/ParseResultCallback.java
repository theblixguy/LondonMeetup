package com.suyashsrijan.londonmeetup.API.tfl.interfaces;

import java.util.ArrayList;

public interface ParseResultCallback<T> {
        void response(ArrayList<T> output);
        void error(Exception exception);
}
