package com.ark.qrreader.qrReaderApi.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ahmedb on 5/31/16.
 */
public class BarCodeObject implements Serializable{

    public static final int CONTACT_TYPE = 1;
    public static final int CALENDER_TYPE = 2;
    public static final int GEO_TYPE = 3;
    public static final int PHONE_TYPE = 4;
    public static final int SMS_TYPE = 5;
    public static final int TEXT_TYPE = 6;
    public static final int URL_TYPE = 7;
    public static final int EMAIL_TYPE = 8;

    private int barObjectType;
    public ArkContact contactInfo;
    public ArkCalender calenderEvent;
    public ArkGeoPoint geoPoint;
    public ArkPhone phone;
    public ArkSMS sms;
    public ArkText normalText;
    public ArkUrl url;
    public ArkEmail email;
    private String diplayValue;
    private String qrType;
    private String rawValue;

    public String getRawValue() {
        return rawValue;
    }

    public void setRawValue(String rawValue) {
        this.rawValue = rawValue;
    }

    public String getDiplayValue() {
        return diplayValue;
    }

    public void setDiplayValue(String diplayValue) {
        this.diplayValue = diplayValue;
    }

    public String getQrType() {
        return qrType;
    }

    public void setQrType(String qrType) {
        this.qrType = qrType;
    }

    public int getBarObjectType() {
        return barObjectType;
    }

    public void setBarObjectType(String value) {

        if(value.toLowerCase().contains("vcard") || value.toLowerCase().contains("mecard"))
                barObjectType = CONTACT_TYPE;

        else if(value.toLowerCase().contains("vcalendar"))
                barObjectType = CALENDER_TYPE;
        else if(value.toLowerCase().contains("geo"))
                barObjectType = GEO_TYPE;
        else if(value.toLowerCase().contains("tel"))
                barObjectType = PHONE_TYPE;
        else if(value.toLowerCase().contains("sms"))
                barObjectType = SMS_TYPE;
        else if(value.toLowerCase().contains("http"))
                barObjectType = URL_TYPE;
        else if(value.toLowerCase().contains("mail") || value.toLowerCase().contains("matmsg"))
                barObjectType = EMAIL_TYPE;
        else
            barObjectType = TEXT_TYPE;

        }

    public class ArkContact implements Serializable{

        private String fullName;
        private String firstName;
        private String lastName;
        private String title;
        private String organization;
        private String address;
        private Date birthday;
        private String email;
        private String[] phones;
        private String[] urls;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOrganization() {
            return organization;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String[] getPhones() {
            return phones;
        }

        public void setPhones(String[] phones) {
            this.phones = phones;
        }

        public String[] getUrls() {
            return urls;
        }

        public void setUrls(String[] urls) {
            this.urls = urls;
        }
    }


    public class ArkCalender implements Serializable{

        private String title;
        private String desc;
        private String location;
        Date start;
        Date end;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public Date getStart() {
            return start;
        }

        public void setStart(Date start) {
            this.start = start;
        }

        public Date getEnd() {
            return end;
        }

        public void setEnd(Date end) {
            this.end = end;
        }
    }

    public class ArkGeoPoint implements Serializable{

        private double lng;
        private double lat;

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

    public class ArkPhone implements Serializable{

        private String number;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }

    public class ArkSMS implements Serializable{

        private String number;
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }

    public class ArkText implements Serializable{

        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public class ArkUrl implements Serializable{

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public class ArkEmail implements Serializable{

        private String emailAdress;
        private String subject;
        private String body;

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getEmailAdress() {
            return emailAdress;
        }

        public void setEmailAdress(String emailAdress) {
            this.emailAdress = emailAdress;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BarCodeObject that = (BarCodeObject) o;

        if (!qrType.equals(that.qrType)) return false;
        return rawValue.equals(that.rawValue);

    }

    @Override
    public int hashCode() {
        int result = qrType.hashCode();
        result = 31 * result + rawValue.hashCode();
        return result;
    }
}
