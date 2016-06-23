package com.ark.qrreader.qrReaderApi.parser;

import android.content.Context;

import com.ark.qrreader.myapplication.R;
import com.ark.qrreader.qrReaderApi.manager.HistoryManager;
import com.ark.qrreader.qrReaderApi.models.BarCodeObject;
import com.ark.qrreader.qrReaderApi.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ahmedb on 5/31/16.
 */
public class QRParser {

    private static QRParser instance;

    private BarCodeObject barCodeObject = new BarCodeObject();

    public static QRParser getInstance(){

        if(instance == null)
            instance = new QRParser();
        return instance;
    }

    public BarCodeObject getBarCodeObject() {
        return barCodeObject;
    }

    public void setBarCodeObject(BarCodeObject barCodeObject) {
        this.barCodeObject = barCodeObject;
    }

    public String parseQRObject(String rawValue , Context context){

        barCodeObject = new BarCodeObject();

        String value = "";

        barCodeObject.setBarObjectType(rawValue);

        rawValue = rawValue.replaceAll("\r", "");

        String text = "";

        switch(barCodeObject.getBarObjectType()){

            case BarCodeObject.CONTACT_TYPE:
                value = parseContact(rawValue);
                text = context.getResources().getString(R.string.contact_info);
                break;

            case BarCodeObject.CALENDER_TYPE:
                value = parseCalenderEvent(rawValue);
                text = context.getResources().getString(R.string.calendar_event);
                break;

            case BarCodeObject.GEO_TYPE:
                value = parseGeo(rawValue);
                text = context.getResources().getString(R.string.location);
                break;

            case BarCodeObject.PHONE_TYPE:
                value = parsePhone(rawValue);
                text = context.getResources().getString(R.string.phone_number);
                break;

            case BarCodeObject.TEXT_TYPE:
                value = parseText(rawValue);
                text = context.getResources().getString(R.string.text);
                break;

            case BarCodeObject.EMAIL_TYPE:
                value = parseEmail(rawValue);
                text = context.getResources().getString(R.string.email);
                break;

            case BarCodeObject.URL_TYPE:
                value = parseUrl(rawValue);
                text = context.getResources().getString(R.string.web_link);
                break;

            case BarCodeObject.SMS_TYPE:
                value = parseSMS(rawValue);
                text = context.getResources().getString(R.string.sms_message);
                break;
        }

        barCodeObject.setRawValue(value);
        barCodeObject.setQrType(text);
        HistoryManager.getInstance().onQRCodeRead(barCodeObject, context);
        return value;
    }

    private String parseSMS(String rawValue) {

        String sms = "";

        if (rawValue.toLowerCase().indexOf("sms:") != -1) {

            sms = getValue(rawValue, rawValue.indexOf(":", rawValue.toLowerCase().indexOf("sms:")));
        }

        barCodeObject.sms = barCodeObject.new ArkSMS();

        String[] smsArr = sms.split(":");

        this.barCodeObject.sms.setNumber(smsArr[0]);
        this.barCodeObject.setDiplayValue(smsArr[0]);

        if(smsArr.length > 1 && smsArr[1] != null)
            this.barCodeObject.sms.setMessage(smsArr[1]);

        return sms;

    }

    private String parseUrl(String rawValue) {

        barCodeObject.url = barCodeObject.new ArkUrl();
        barCodeObject.url.setUrl(rawValue);
        barCodeObject.setDiplayValue(rawValue);
        return rawValue;
    }

    private String parseEmail(String rawValue) {

        String value = "";

        rawValue = rawValue.replace(";" , "\n");

        barCodeObject.email = barCodeObject.new ArkEmail();

        if (rawValue.toLowerCase().indexOf("to:") != -1) {

            value = getValue(rawValue, rawValue.indexOf(":", rawValue.toLowerCase().indexOf("to:")));

            this.barCodeObject.email.setEmailAdress(value);
            this.barCodeObject.setDiplayValue(value);
        }

        if(rawValue.toLowerCase().indexOf("sub:") != -1){

            this.barCodeObject.email.setSubject(getValue(rawValue, rawValue.indexOf(":", rawValue.toLowerCase().indexOf("sub:"))));
        }

        if(rawValue.toLowerCase().indexOf("body:") != -1){

            this.barCodeObject.email.setBody(getValue(rawValue, rawValue.indexOf(":", rawValue.toLowerCase().indexOf("body:"))));
        }

        return value;
    }

    private String parseText(String rawValue) {

        barCodeObject.normalText = barCodeObject.new ArkText();

        barCodeObject.normalText.setText(rawValue);
        barCodeObject.setDiplayValue(rawValue);
        return rawValue;
    }

    private String parsePhone(String rawValue) {

        String value = "";

        if (rawValue.toLowerCase().indexOf("tel:") != -1) {

            value = getValue(rawValue, rawValue.indexOf(":", rawValue.toLowerCase().indexOf("tel:")));

            barCodeObject.phone = barCodeObject.new ArkPhone();

            this.barCodeObject.phone.setNumber(value);
            this.barCodeObject.setDiplayValue(value);

        }
        return value;
    }

    private String parseGeo(String rawValue) {

        String value = "";

        if (rawValue.toLowerCase().indexOf("geo:") != -1) {


            String geoLocation = getValue(rawValue, rawValue.indexOf(":", rawValue.toLowerCase().indexOf("geo:")));
            String[] geoPoints = geoLocation.split(",");

            value = "Latitude : " + geoPoints[0] + "\n" + "Longitude : " + geoPoints[1];

            barCodeObject.geoPoint = barCodeObject.new ArkGeoPoint();

            this.barCodeObject.geoPoint.setLat(Double.parseDouble(geoPoints[0]));
            this.barCodeObject.geoPoint.setLng(Double.parseDouble(geoPoints[1]));
            this.barCodeObject.setDiplayValue(value);
        }

        return value;
    }

    private String parseCalenderEvent(String rawValue) {

        String calender = "";

        barCodeObject.calenderEvent = barCodeObject.new ArkCalender();

        if (rawValue.toLowerCase().indexOf("title:") != -1) {

            String title = getValue(rawValue, rawValue.indexOf(":", rawValue.toLowerCase().indexOf("title:")));

            calender = calender + "Title : " + title;
            this.barCodeObject.calenderEvent.setTitle(title);
            this.barCodeObject.setDiplayValue(title);
        }

        if (rawValue.toLowerCase().indexOf("description:") != -1) {

            String description = getValue(rawValue, rawValue.indexOf(":", rawValue.toLowerCase().indexOf("description:")));

            calender = calender + "\nDescription : " + description;
            this.barCodeObject.calenderEvent.setDesc(description);

        }
        if (rawValue.toLowerCase().indexOf("location:") != -1) {

            String location = getValue(rawValue, rawValue.indexOf(":", rawValue.toLowerCase().indexOf("location:")));
            calender = calender + "\nLocation : " + location;
            this.barCodeObject.calenderEvent.setLocation(location);

        }
        if (rawValue.toLowerCase().indexOf("dtstart:") != -1) {

            String startTime = getValue(rawValue, rawValue.indexOf(":", rawValue.toLowerCase().indexOf("dtstart:")));

            Date startDate = DateUtils.getTimeGMT(startTime);

            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);

            calender = calender + "\nStart : " + new SimpleDateFormat("MMM").format(cal.getTime()) + " " + cal.get(Calendar.DAY_OF_MONTH) + ", " + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
            this.barCodeObject.calenderEvent.setStart(startDate);
        }
        if (rawValue.toLowerCase().indexOf("dtend:") != -1) {

            String endDate = getValue(rawValue, rawValue.indexOf(":", rawValue.toLowerCase().indexOf("dtend:")));

            Date endtDate = DateUtils.getTimeGMT(endDate);

            Calendar cal = Calendar.getInstance();
            cal.setTime(endtDate);

            calender = calender + "\nEnd : " + new SimpleDateFormat("MMM").format(cal.getTime()) + " " + cal.get(Calendar.DAY_OF_MONTH) + ", " + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
            this.barCodeObject.calenderEvent.setEnd(endtDate);

        }
        return calender;

    }

    private String parseContact(String rawValue) {

        String contact = "";

        barCodeObject.contactInfo = barCodeObject.new ArkContact();

        if (rawValue.toLowerCase().indexOf("\nn:" , rawValue.toLowerCase().indexOf("version:")) != -1) {

            String name = getValue(rawValue, rawValue.indexOf(":", rawValue.toLowerCase().indexOf("\nn:" , rawValue.toLowerCase().indexOf("version:"))));

            name = name.replace(";" , " ");

            String[] nameValue = name.split(" ");

            if(nameValue.length > 1 && nameValue[1] != null)
                name = nameValue[1];

            if(nameValue.length >= 1 && nameValue[0] != null)
                name += " " + nameValue[0];

            contact += "Name : " + name;

            if(nameValue.length > 1 && nameValue[1] != null)
                this.barCodeObject.contactInfo.setFirstName(nameValue[1]);

            if(nameValue.length > 1 && nameValue[0] != null)
                this.barCodeObject.contactInfo.setLastName(nameValue[0]);

            this.barCodeObject.contactInfo.setFullName(name);
            this.barCodeObject.setDiplayValue(name);
        }

        if (rawValue.toLowerCase().indexOf("title:") != -1) {

            String title = getValue(rawValue, rawValue.indexOf(":", rawValue.toLowerCase().indexOf("title:")));

            contact += "\nTitle : " + title;

            this.barCodeObject.contactInfo.setTitle(title);
        }

        if (rawValue.toLowerCase().indexOf("org:") != -1) {

            String organization = getValue(rawValue, rawValue.indexOf(":", rawValue.toLowerCase().indexOf("org:")));

            contact = contact + "\nOrganization : " + organization;

            this.barCodeObject.contactInfo.setOrganization(organization);
        }

        if (rawValue.toLowerCase().indexOf("email:") != -1) {

            String email = getValue(rawValue, rawValue.indexOf(":", rawValue.toLowerCase().indexOf("email:")));

            contact = contact + "\nEmail : " + email;

            this.barCodeObject.contactInfo.setEmail(email);
        }

        if(rawValue.toLowerCase().indexOf("tel:") != -1 || rawValue.toLowerCase().indexOf("tel;") != -1){

            String phones = parsePhones(rawValue);

            contact = contact + "\nPhone : " + phones;

            if(rawValue.toLowerCase().indexOf("cell:") != -1)
                this.barCodeObject.contactInfo.setPhones(new String[]{getValue(rawValue, rawValue.indexOf(":", rawValue.toLowerCase().indexOf("cell:")))});
            else
                this.barCodeObject.contactInfo.setPhones(phones.split(","));
        }

        if (rawValue.toLowerCase().indexOf("adr:") != -1) {

            String addressValue = getValue(rawValue, rawValue.indexOf(":", rawValue.toLowerCase().indexOf("adr:")));
            if (addressValue.contains(";")) {
                addressValue = addressValue.replaceAll(";", ", ");
            }

            contact = contact + "\nAddress : " + addressValue;

            this.barCodeObject.contactInfo.setAddress(addressValue);

        }

        if (rawValue.toLowerCase().indexOf("url::") != -1) {

            String url = getValue(rawValue, rawValue.indexOf(":", rawValue.toLowerCase().indexOf("url:")));

            contact = contact + "\nUrls : " + url;

            this.barCodeObject.contactInfo.setUrls(new String[]{url});
        }


        if (rawValue.toLowerCase().indexOf("bday:") != -1) {

            int birthDAyIndex = rawValue.toLowerCase().indexOf("bday:");
            try {
                Date birthday = new SimpleDateFormat(DateUtils.determineDateFormat(getValue(rawValue, rawValue.indexOf(":", birthDAyIndex)))).parse(getValue(rawValue, rawValue.indexOf(":", birthDAyIndex)));
                this.barCodeObject.contactInfo.setBirthday(birthday);
                contact = contact + "\nBirthDay : " + new SimpleDateFormat("dd MMM yyyy").format(birthday);

            } catch (ParseException e) {

                contact = contact + "\nBirthDay : " + getValue(rawValue, rawValue.indexOf(":", birthDAyIndex));
                e.printStackTrace();
            }
        }
        return contact;

    }


    private String parsePhones(String rawString){

        int telPhoneIndex = rawString.toLowerCase().indexOf("tel:");

        int telPhoneIndex2 = rawString.toLowerCase().indexOf("tel;");

        String phone = "";

        for(int x = 0 ; x < rawString.length() ; x++){

            if(telPhoneIndex != -1 || telPhoneIndex2 != -1) {


                phone += getValue(rawString, rawString.indexOf( ":" , telPhoneIndex != -1 ? telPhoneIndex : telPhoneIndex2));

                if(telPhoneIndex != -1)
                    telPhoneIndex = rawString.toLowerCase().indexOf("tel:" , rawString.indexOf(":" , telPhoneIndex));
                else if(telPhoneIndex2 != -1)
                    telPhoneIndex2 = rawString.toLowerCase().indexOf("tel;" , rawString.indexOf(";" , telPhoneIndex2));

                if(telPhoneIndex != -1 || telPhoneIndex2 != -1)
                    phone += ", ";

            }else{
                break;
            }
        }

        return phone;
    }

    private String getValue(String rawString, int firstIndex) {

        String value = "";

        for (int x = firstIndex; x < rawString.length(); x++) {

            if (!rawString.substring(x + 1, x + 2).matches(":|;")) {

                if(rawString.contains("\n"))
                    value = rawString.substring(x + 1, rawString.indexOf("\n", firstIndex));
                else
                    value = rawString.substring(x + 1);

                break;
            }
        }
        return value;
    }

}
