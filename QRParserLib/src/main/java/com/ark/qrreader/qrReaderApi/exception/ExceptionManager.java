package com.ark.qrreader.qrReaderApi.exception;

import com.ark.qrreader.qrReaderApi.processing.OnQRCodeReadListener;

/**
 * Created by ahmedb on 6/2/16.
 */
public class ExceptionManager {

    private static ExceptionManager instance;
    private OnQRCodeReadListener onQRCodeReadListener;

    public static ExceptionManager getInstance(){

        if(instance == null)
            instance = new ExceptionManager();
        return instance;
    }

    public OnQRCodeReadListener getOnQRCodeReadListener() {
        return onQRCodeReadListener;
    }

    public void setOnQRCodeReadListener(OnQRCodeReadListener onQRCodeReadListener) {
        this.onQRCodeReadListener = onQRCodeReadListener;
    }
}
