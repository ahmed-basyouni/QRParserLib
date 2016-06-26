package com.ark.qrreader.qrReaderApi.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.view.View;

import com.ark.qrreader.qrReaderApi.annotations.AddToCalender;
import com.ark.qrreader.qrReaderApi.annotations.AddToContacts;
import com.ark.qrreader.qrReaderApi.annotations.Call;
import com.ark.qrreader.qrReaderApi.annotations.OpenBrowser;
import com.ark.qrreader.qrReaderApi.annotations.SendEmail;
import com.ark.qrreader.qrReaderApi.annotations.SendSMS;
import com.ark.qrreader.qrReaderApi.annotations.ShowOnMap;
import com.ark.qrreader.qrReaderApi.exception.QRReaderException;
import com.ark.qrreader.qrReaderApi.models.BarCodeObject;
import com.ark.qrreader.qrReaderApi.parser.QRParser;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by ahmedb on 5/31/16.
 */
public class ActionHandler {

    private final Activity activity;

    private Object clazz;

    QRReaderException exception = new QRReaderException("Field was missing");

    public ActionHandler(Fragment fragment) {

        this.activity = fragment.getActivity();
        this.clazz = fragment;

        handleClassFields(fragment);
    }

    public ActionHandler(Activity activity) {

        this.activity = activity;
        this.clazz = activity;

        handleClassFields(activity);
    }

    public ActionHandler(Object clazz, Activity activity) {

        this.activity = activity;

        this.clazz = clazz;

        handleClassFields(clazz);
    }

    private void handleClassFields(Object clazz) {

        Field[] fields = clazz.getClass().getDeclaredFields();

        for (Field field : fields) {

            try {

                checkAddToContactField(field);
                checkCallField(field);
                checkShowOnMapField(field);
                checkSendEmailField(field);
                checkAddToCalender(field);
                checkShowOnMapField(field);
                checkSMSField(field);
                checkOpenBrowserField(field);

            } catch (IllegalAccessException e) {

                e.printStackTrace();

            } catch (IllegalArgumentException ex) {

                ex.printStackTrace();
            }

        }
    }


    //Check fields methods

    private void checkOpenBrowserField(Field field) throws IllegalAccessException {

        OpenBrowser openBrowser = field.getAnnotation(OpenBrowser.class);

        Class filedClass = field.getType();

        if (openBrowser != null && View.class.isAssignableFrom(filedClass)) {


            field.setAccessible(true);
            View viewToClick = (View) field.get(clazz);

            if(viewToClick != null)
                viewToClick.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    opnBrowser();
                }
            });
        }

    }

    private void checkSMSField(Field field) throws IllegalAccessException {

        SendSMS sendSMS = field.getAnnotation(SendSMS.class);

        Class filedClass = field.getType();

        if (sendSMS != null && View.class.isAssignableFrom(filedClass)) {


            field.setAccessible(true);
            View viewToClick = (View) field.get(clazz);

            if(viewToClick != null)
                viewToClick.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    sendSMSAction();
                }
            });
        }

    }

    private void checkAddToCalender(Field field) throws IllegalAccessException {

        AddToCalender sendEmail = field.getAnnotation(AddToCalender.class);

        Class filedClass = field.getType();

        if (sendEmail != null && View.class.isAssignableFrom(filedClass)) {


            field.setAccessible(true);
            View viewToClick = (View) field.get(clazz);

            if(viewToClick != null)
                viewToClick.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        addToCalenderAction();
                    }
                });
        }
    }

    private void checkSendEmailField(Field field) throws IllegalAccessException {

        SendEmail sendEmail = field.getAnnotation(SendEmail.class);

        Class filedClass = field.getType();

        if (sendEmail != null && View.class.isAssignableFrom(filedClass)) {


            field.setAccessible(true);
            View viewToClick = (View) field.get(clazz);

            if(viewToClick != null)
                viewToClick.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    sendEmail();
                }
            });
        }
    }

    private void checkShowOnMapField(Field field) throws IllegalAccessException {

        ShowOnMap showOnMap = field.getAnnotation(ShowOnMap.class);

        Class filedClass = field.getType();

        if (showOnMap != null && View.class.isAssignableFrom(filedClass)) {


            field.setAccessible(true);
            View viewToClick = (View) field.get(clazz);

            if(viewToClick != null)
                viewToClick.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    showOnMap();
                }
            });
        }

    }

    private void checkCallField(Field field) throws IllegalAccessException {

        Call call = field.getAnnotation(Call.class);

        Class filedClass = field.getType();

        if (call != null && View.class.isAssignableFrom(filedClass)) {


            field.setAccessible(true);
            View viewToClick = (View) field.get(clazz);

            if(viewToClick != null)
                viewToClick.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    callAction();
                }
            });
        }

    }

    private void checkAddToContactField(Field field) throws IllegalAccessException {

        AddToContacts addToContacts = field.getAnnotation(AddToContacts.class);

        Class filedClass = field.getType();

        if (addToContacts != null && View.class.isAssignableFrom(filedClass)) {


            field.setAccessible(true);
            View viewToClick = (View) field.get(clazz);

            if(viewToClick != null)
                viewToClick.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        addToContactsAction();
                    }
                });
        }
    }

    // Action methods

    private void sendEmail() {

        String email = "";
        String subject = "";
        String body = "";

        if (QRParser.getInstance().getBarCodeObject().contactInfo != null) {

            email = QRParser.getInstance().getBarCodeObject().contactInfo.getEmail();

        } else {

            email = QRParser.getInstance().getBarCodeObject().email.getEmailAdress();
            subject = QRParser.getInstance().getBarCodeObject().email.getSubject();
            body = QRParser.getInstance().getBarCodeObject().email.getBody();

        }

        String[] addresses = new String[]{email};
        if (email != null) {
            Intent emailIntent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", email, null));
            if (subject != null)
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            if (body != null)
                emailIntent.putExtra(Intent.EXTRA_TEXT, body);
            if (addresses != null)
                emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses);

            this.activity.startActivity(Intent.createChooser(emailIntent, "Send email..."));

        }else{


            exception.setMissingField("Email");
            QRParser.getInstance().getOnQRCodeReadListener().QRException(exception);
        }
    }

    private void showOnMap() {

        String mapURL = "";

        if (QRParser.getInstance().getBarCodeObject().geoPoint != null && Double.valueOf(QRParser.getInstance().getBarCodeObject().geoPoint.getLat()) != 0 && Double.valueOf(QRParser.getInstance().getBarCodeObject().geoPoint.getLng()) != 0) {

            mapURL = "http://maps.google.com/maps?q=loc:" + Double.valueOf(QRParser.getInstance().getBarCodeObject().geoPoint.getLat()) + "," + Double.valueOf(QRParser.getInstance().getBarCodeObject().geoPoint.getLng());

        } else if (QRParser.getInstance().getBarCodeObject().contactInfo.getAddress() != null && !QRParser.getInstance().getBarCodeObject().contactInfo.getAddress().equalsIgnoreCase("")) {

            mapURL = "http://maps.google.co.in/maps?q=" + QRParser.getInstance().getBarCodeObject().contactInfo.getAddress();
        }

        if(!mapURL.equalsIgnoreCase(""))
            this.activity.startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mapURL)));

        else {

            exception.setMissingField("Location");
            QRParser.getInstance().getOnQRCodeReadListener().QRException(exception);
        }
    }


    private void callAction() {

        String phone = "";
        if (QRParser.getInstance().getBarCodeObject().contactInfo != null) {
            phone = QRParser.getInstance().getBarCodeObject().contactInfo.getPhones()[0];
        } else {
            phone = QRParser.getInstance().getBarCodeObject().phone.getNumber();
        }
        Intent callIntent = new Intent("android.intent.action.CALL");
        callIntent.setData(Uri.parse("tel:" + phone));
        if (checkCallPermission()) {
            this.activity.startActivity(callIntent);
        }else
            exception.setErrorCode(QRReaderException.CALL_PERMISSION_NOT_GRANTED);
    }

    private boolean checkCallPermission() {

        String permission = Manifest.permission.CALL_PHONE;
        int res = activity.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


    private void addToContactsAction() {

        BarCodeObject.ArkContact person = QRParser.getInstance().getBarCodeObject().contactInfo;

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (person.getFullName() != null)
            intent.putExtra(ContactsContract.Intents.Insert.NAME, person.getFullName());
        if (person.getPhones()[0] != null)
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, person.getPhones()[0]);
        if (person.getEmail() != null)
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, person.getEmail());
        if (person.getTitle() != null)
            intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, person.getTitle());
        if (person.getOrganization() != null)
            intent.putExtra(ContactsContract.Intents.Insert.COMPANY, person.getOrganization());

        if (person.getBirthday() != null) {
            ArrayList<ContentValues> data = new ArrayList();
            ContentValues row1 = new ContentValues();
            row1.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE);
            row1.put(ContactsContract.CommonDataKinds.Event.TYPE, Integer.valueOf(3));
            row1.put(ContactsContract.CommonDataKinds.Event.START_DATE, DateUtils.getDateString(person.getBirthday()));
            data.add(row1);

            intent.putParcelableArrayListExtra("data", data);
        }

        this.activity.startActivity(intent);

    }

    private void sendSMSAction() {

        String phone = "";
        String message = "";
        if (QRParser.getInstance().getBarCodeObject().contactInfo != null) {
            phone = QRParser.getInstance().getBarCodeObject().contactInfo.getPhones()[0];
        } else {
            phone = QRParser.getInstance().getBarCodeObject().sms.getNumber();
            message = QRParser.getInstance().getBarCodeObject().sms.getMessage();
        }
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");

        if (phone != null)
            smsIntent.putExtra("address", phone);
        if (message != null)
            smsIntent.putExtra("sms_body", message);

        this.activity.startActivity(smsIntent);
    }

    private void opnBrowser() {

        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
        i.setData(Uri.parse(QRParser.getInstance().getBarCodeObject().url.getUrl()));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.activity.startActivity(i);
    }

    private void addToCalenderAction() {

        BarCodeObject.ArkCalender event = QRParser.getInstance().getBarCodeObject().calenderEvent;

        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        if (event.getTitle() != null)
            intent.putExtra(CalendarContract.Events.TITLE, event.getTitle());
        if (event.getStart() != null)
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                    event.getStart().getTime());
        if (event.getEnd() != null)
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                    event.getEnd().getTime());
        if (event.getDesc() != null)
            intent.putExtra(CalendarContract.Events.DESCRIPTION, event.getDesc());
        if (event.getLocation() != null)
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, event.getLocation());

        this.activity.startActivity(intent);

    }

}