package com.ark.qrreader.qrReaderApi.uiUtils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ark.qrreader.qrReaderApi.annotations.AddToCalender;
import com.ark.qrreader.qrReaderApi.annotations.AddToContacts;
import com.ark.qrreader.qrReaderApi.annotations.Call;
import com.ark.qrreader.qrReaderApi.annotations.OpenBrowser;
import com.ark.qrreader.qrReaderApi.annotations.SendEmail;
import com.ark.qrreader.qrReaderApi.annotations.SendSMS;
import com.ark.qrreader.qrReaderApi.annotations.ShowOnMap;
import com.ark.qrreader.qrReaderApi.models.BarCodeObject;
import com.ark.qrreader.myapplication.R;
import com.ark.qrreader.qrReaderApi.parser.QRParser;
import com.ark.qrreader.qrReaderApi.utils.ActionHandler;

/**
 * Created by ahmedb on 5/31/16.
 */
public class ButtonsGenerator {

    private final Activity activity;

    @AddToCalender
    Button addToCalender;

    @SendEmail
    Button sendEmail;

    @OpenBrowser
    Button openBrowser;

    @AddToContacts
    Button addToContactsButton;

    @Call
    Button callButton;

    @SendSMS
    Button sendSMS;

    @ShowOnMap
    Button showOnMap;

    public ButtonsGenerator(Activity activity){
        this.activity = activity;
    }

    private LinearLayout getContainer(){

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout container = new LinearLayout(activity);

        container.setLayoutParams(param);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setWeightSum(2.0f);

        return container;
    }

    private LinearLayout.LayoutParams getButtonLayoutParam(){

        LinearLayout.LayoutParams buttonsParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);

        buttonsParam.setMargins(5, 0, 5, 0);

        return buttonsParam;
    }

    private Button getAddToCalenderButton() {

        addToCalender = new Button(activity);
        addToCalender.setText(activity.getResources().getString(R.string.add_calendar));
        addToCalender.setLayoutParams(getButtonLayoutParam());
        return addToCalender;

    }

    private Button getEmaiButton() {

        sendEmail = new Button(activity);
        sendEmail.setText(activity.getResources().getString(R.string.send_email));
        sendEmail.setLayoutParams(getButtonLayoutParam());
        return sendEmail;
    }

    private Button getOpenBrowserLayout() {

        openBrowser = new Button(activity);
        openBrowser.setText(activity.getResources().getString(R.string.open_browser));
        openBrowser.setLayoutParams(getButtonLayoutParam());
        return openBrowser;
    }

    private LinearLayout getPhoneLayout() {


        LinearLayout container = getContainer();
        callButton = new Button(activity);
        callButton.setLayoutParams(getButtonLayoutParam());
        callButton.setText(activity.getResources().getString(R.string.call));
        container.addView(callButton);

        addToContactsButton = new Button(activity);
        addToContactsButton.setLayoutParams(getButtonLayoutParam());
        addToContactsButton.setText(activity.getResources().getString(R.string.add_contact));
        container.addView(addToContactsButton);

        return container;
    }

    private Button getSendSmsButton() {

        sendSMS = new Button(activity);
        sendSMS.setLayoutParams(getButtonLayoutParam());
        sendSMS.setText(activity.getResources().getString(R.string.send_sms));

        return sendSMS;
    }

    private Button getShowOnMapButton() {

        showOnMap = new Button(activity);
        showOnMap.setLayoutParams(getButtonLayoutParam());
        showOnMap.setText(activity.getResources().getString(R.string.on_map));

        return showOnMap;
    }

    private LinearLayout getTwoLayouts(View firstView, View secView) {

        LinearLayout container = getContainer();

        firstView.setLayoutParams(getButtonLayoutParam());
        secView.setLayoutParams(getButtonLayoutParam());

        container.addView(firstView);
        container.addView(secView);

        return container;
    }

    public LinearLayout getButtonsView(BarCodeObject barCodeObject) {

        LinearLayout container = new LinearLayout(activity);

        container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container.setOrientation(LinearLayout.VERTICAL);

        switch (barCodeObject.getBarObjectType()) {

            case BarCodeObject.CONTACT_TYPE:
                container.addView(getPhoneLayout());
                container.addView(getTwoLayouts(getShowOnMapButton(), getEmaiButton()));
                break;
            case BarCodeObject.CALENDER_TYPE:
                container.addView(getAddToCalenderButton());
                break;
            case BarCodeObject.PHONE_TYPE :
                container.addView(getPhoneLayout());
                break;
            case BarCodeObject.EMAIL_TYPE:
                container.addView(getEmaiButton());
                break;
            case BarCodeObject.GEO_TYPE:
                container.addView(getShowOnMapButton());
                break;
            case BarCodeObject.SMS_TYPE:
                container.addView(getSendSmsButton());
                break;
            case BarCodeObject.URL_TYPE:
                container.addView(getOpenBrowserLayout());
                break;
        }

        new ActionHandler(ButtonsGenerator.this , activity);

        return container;
    }

}
