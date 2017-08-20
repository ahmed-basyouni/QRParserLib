package com.ark.qrreader.qrReaderApi.UI;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;

import com.ark.qrreader.customViews.QRCodeReaderView;
import com.ark.qrreader.myapplication.R;
import com.ark.qrreader.qrReaderApi.exception.QRReaderException;
import com.ark.qrreader.qrReaderApi.models.BarCodeObject;
import com.ark.qrreader.qrReaderApi.parser.QRParser;
import com.ark.qrreader.qrReaderApi.processing.OnQRCodeReadListener;

/**
 *
 * Created by Ark on 8/20/2017.
 */

public class CaptureActivityForResult extends Activity implements OnQRCodeReadListener, View.OnClickListener{

    public static final String QR_ICON = "com.ark.qrreader.qrReaderApi.UI.qr_icon";
    public static final String QR_TITLE = "com.ark.qrreader.qrReaderApi.UI.qr_title";
    public static final String QR_OBJECT = "com.ark.qrreader.qrReaderApi.UI.qr_Obj";
    public static final String ERROR_CODE = "com.ark.qrreader.qrReaderApi.UI.error_code";
    public static final String ERROR_MSG = "com.ark.qrreader.qrReaderApi.UI.error_MSG";
    private QRCodeReaderView surfaceView;
    private ImageButton flashBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QRParser.init(this);

        if(isCameraPermissionGranted()){
            startCaptureFlow();
        }else{
            cameraPermissionNotGranted();
        }
    }

    private void cameraPermissionNotGranted() {
        Intent dataIntent = new Intent();
        dataIntent.putExtra(ERROR_CODE, QRReaderException.CAMERA_PERMISSION_NOT_GRANTED);
        setResult(RESULT_CANCELED, dataIntent);
        finish();
    }

    public boolean isCameraPermissionGranted() {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED);
    }

    private void startCaptureFlow() {
        setContentView(R.layout.capture_activity_for_result);
        surfaceView = (QRCodeReaderView) findViewById(R.id.camera_view);

        flashBtn = (ImageButton) findViewById(R.id.flashBtn);
        flashBtn.setOnClickListener(this);
        boolean hasFlash = getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if(!hasFlash)
            flashBtn.setVisibility(View.GONE);
    }

    @Override
    public void onQRCodeRead(final String text) {

    }

    // Called when your device have no camera
    @Override
    public void cameraNotFound() {
        Intent dataIntent = new Intent();
        dataIntent.putExtra(ERROR_CODE, QRReaderException.CAMERA_ERROR);
        setResult(RESULT_CANCELED, dataIntent);
        finish();
    }

    // Called when there's no QR codes in the camera preview image
    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    @Override
    public void QRException(QRReaderException ex) {
        Intent dataIntent = new Intent();
        dataIntent.putExtra(ERROR_CODE, ex.getErrorCode());
        dataIntent.putExtra(ERROR_MSG, ex.getMessage());
        setResult(RESULT_CANCELED, dataIntent);
        finish();
    }

    @Override
    public void onQRCodeReadWithObject(BarCodeObject barCodeObject) {

        handleQRObject(barCodeObject);
    }

    private void handleQRObject(BarCodeObject barCodeObject) {
        int resourceIcon = -1;
        String text = "";

        switch (barCodeObject.getBarObjectType()) {

            case BarCodeObject.CONTACT_TYPE:
                resourceIcon = R.drawable.contact;
                text = getResources().getString(R.string.contact_info);
                break;

            case BarCodeObject.CALENDER_TYPE:
                resourceIcon = R.drawable.calendar;
                text = getResources().getString(R.string.calendar_event);
                break;

            case BarCodeObject.GEO_TYPE:
                resourceIcon = R.drawable.location;
                text = getResources().getString(R.string.location);
                break;

            case BarCodeObject.PHONE_TYPE:
                resourceIcon = R.drawable.phone;
                text = getResources().getString(R.string.phone_number);
                break;

            case BarCodeObject.SMS_TYPE:
                resourceIcon = R.drawable.sms;
                text = getResources().getString(R.string.sms_message);
                break;

            case BarCodeObject.EMAIL_TYPE:
                resourceIcon = R.drawable.email;
                text = getResources().getString(R.string.email);
                break;

            case BarCodeObject.URL_TYPE:
                resourceIcon = R.drawable.web_link;
                text = getResources().getString(R.string.web_link);
                break;

            case BarCodeObject.TEXT_TYPE:
                resourceIcon = R.drawable.text;
                text = getResources().getString(R.string.text);
                break;
        }

        Intent dataIntent = new Intent();
        dataIntent.putExtra(QR_ICON, resourceIcon);
        dataIntent.putExtra(QR_TITLE, text);
        dataIntent.putExtra(QR_OBJECT, text);
        setResult(RESULT_OK, dataIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        surfaceView.getCameraManager().stopPreview();
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.flashBtn)
            changeFlash();

    }

    private void changeFlash() {
        flashBtn.setImageResource(surfaceView.isFlashOn() ? R.mipmap.ic_flash_on_white_24dp : R.mipmap.ic_flash_off_white_24dp);
        surfaceView.changeFlash();
    }
}
