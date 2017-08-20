package com.ark.qrreader.qrReaderApi.UI;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ark.qrreader.customViews.QRCodeReaderView;
import com.ark.qrreader.qrReaderApi.annotations.Call;
import com.ark.qrreader.qrReaderApi.exception.QRReaderException;
import com.ark.qrreader.qrReaderApi.manager.HistoryManager;
import com.ark.qrreader.qrReaderApi.models.BarCodeObject;
import com.ark.qrreader.myapplication.R;
import com.ark.qrreader.qrReaderApi.parser.QRParser;
import com.ark.qrreader.qrReaderApi.processing.OnQRCodeReadListener;
import com.ark.qrreader.qrReaderApi.uiUtils.ButtonsGenerator;

/**
 * Created by ahmedb on 5/31/16
 * The default view used in Ark QrReader
 * it's a simple view consists of a title
 * textView that show parsed data
 * and buttons container
 */

public class CaptureActivity extends Activity implements OnQRCodeReadListener , View.OnClickListener{

    public static final String OBJECT_POSITION = "objectPosition";
    private final String BARCODE_OBJECT = "barCodeObj";
    private QRCodeReaderView surfaceView;
    private TextView textView_qrcode_info;
    private View historyButton;
    private LinearLayout buttonsContainer;
    private ImageView qrTypeIcon;
    private TextView qrTypeText;
    private boolean qrDetailsOnScreen;
    private ImageButton flashBtn;
    private BarCodeObject barCodeObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QRParser.init(this);
        setContentView(R.layout.ark_capture_activity);
        surfaceView = (QRCodeReaderView) findViewById(R.id.camera_view);

        textView_qrcode_info = (TextView) findViewById(R.id.code_info);
        buttonsContainer = (LinearLayout) findViewById(R.id.buttonsContainer);
        qrTypeIcon = (ImageView) findViewById(R.id.typeIcon);
        qrTypeText = (TextView) findViewById(R.id.typeText);
        historyButton = findViewById(R.id.historyBtn);
        flashBtn = (ImageButton) findViewById(R.id.flashBtn);
        flashBtn.setOnClickListener(this);
        boolean hasFlash = getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if(!hasFlash)
            flashBtn.setVisibility(View.GONE);

        historyButton.setOnClickListener(this);

        if(savedInstanceState != null && savedInstanceState.getSerializable(BARCODE_OBJECT) != null)
            barCodeObj = (BarCodeObject) savedInstanceState.getSerializable(BARCODE_OBJECT);

        int position = getIntent().getExtras() != null ? getIntent().getExtras().getInt(OBJECT_POSITION, -1) : -1;

        if (position != -1) {
            surfaceView.stopCamera();
            barCodeObj = HistoryManager.getInstance().getObjectByPosition(position);
        }

        if(barCodeObj != null){

            handleQRText(barCodeObj.getRawValue());
            handleQRObject(barCodeObj);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(BARCODE_OBJECT , barCodeObj);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onBackPressed() {

        if (qrDetailsOnScreen) {
            historyButton.setVisibility(View.VISIBLE);
            flashBtn.setVisibility(View.VISIBLE);
            textView_qrcode_info.setText(getResources().getString(R.string.emptyQRView));
            surfaceView.refreshCamera();
            buttonsContainer.removeAllViews();
            qrTypeIcon.setImageDrawable(null);
            qrTypeText.setText("");
            qrDetailsOnScreen = false;

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onQRCodeRead(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                handleQRText(text);
            }
        });
    }

    private void handleQRText(String text) {

        qrDetailsOnScreen = true;

        if(surfaceView.isFlashOn())
            changeFlash();

        historyButton.setVisibility(View.GONE);
        flashBtn.setVisibility(View.GONE);
        textView_qrcode_info.setText(text);
        buttonsContainer.addView(new ButtonsGenerator(CaptureActivity.this).getButtonsView(QRParser.getInstance().getBarCodeObject()));
    }

    // Called when your device have no camera
    @Override
    public void cameraNotFound() {

    }

    // Called when there's no QR codes in the camera preview image
    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    @Override
    public void QRException(QRReaderException ex) {

        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onQRCodeReadWithObject(BarCodeObject barCodeObject) {

        barCodeObj = barCodeObject;
        handleQRObject(barCodeObject);
    }

    private void handleQRObject(BarCodeObject barCodeObject) {
        Drawable resourceIcon = null;
        String text = "";

        switch (barCodeObject.getBarObjectType()) {

            case BarCodeObject.CONTACT_TYPE:
                resourceIcon = this.getResources().getDrawable(R.drawable.contact);
                text = getResources().getString(R.string.contact_info);
                break;

            case BarCodeObject.CALENDER_TYPE:
                resourceIcon = this.getResources().getDrawable(R.drawable.calendar);
                text = getResources().getString(R.string.calendar_event);
                break;

            case BarCodeObject.GEO_TYPE:
                resourceIcon = this.getResources().getDrawable(R.drawable.location);
                text = getResources().getString(R.string.location);
                break;

            case BarCodeObject.PHONE_TYPE:
                resourceIcon = this.getResources().getDrawable(R.drawable.phone);
                text = getResources().getString(R.string.phone_number);
                break;

            case BarCodeObject.SMS_TYPE:
                resourceIcon = this.getResources().getDrawable(R.drawable.sms);
                text = getResources().getString(R.string.sms_message);
                break;

            case BarCodeObject.EMAIL_TYPE:
                resourceIcon = this.getResources().getDrawable(R.drawable.email);
                text = getResources().getString(R.string.email);
                break;

            case BarCodeObject.URL_TYPE:
                resourceIcon = this.getResources().getDrawable(R.drawable.web_link);
                text = getResources().getString(R.string.web_link);
                break;

            case BarCodeObject.TEXT_TYPE:
                resourceIcon = this.getResources().getDrawable(R.drawable.text);
                text = getResources().getString(R.string.text);
                break;
        }

        qrTypeIcon.setImageDrawable(resourceIcon);
        qrTypeText.setText(text);
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

        if(view.getId() == R.id.historyBtn)
                startHistoryActivity();

        else if(view.getId() == R.id.flashBtn)
                changeFlash();

    }

    private void changeFlash() {
        flashBtn.setImageResource(surfaceView.isFlashOn() ? R.mipmap.ic_flash_on_white_24dp : R.mipmap.ic_flash_off_white_24dp);
        surfaceView.changeFlash();
    }

    private void startHistoryActivity() {

        Intent intent = new Intent(CaptureActivity.this, HistoryActivity.class);
        startActivity(intent);

    }
}
