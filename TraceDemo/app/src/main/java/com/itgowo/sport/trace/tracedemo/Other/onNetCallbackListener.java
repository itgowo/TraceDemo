package com.itgowo.sport.trace.tracedemo.Other;

import java.io.File;

/**
 * Created by lujianchao on 2016/11/2.
 */

public abstract class onNetCallbackListener implements onNetCallbackIListener {
    @Override
    public void onCancelled () {
    }

    @Override
    public void onError (final Throwable throwable) {
        throwable.printStackTrace ();
    }

    @Override
    public void onFinished () {

    }

    @Override
    public void onSuccess (final String result) {

    }

    @Override
    public void onSuccess (final File mFile) {

    }
}
