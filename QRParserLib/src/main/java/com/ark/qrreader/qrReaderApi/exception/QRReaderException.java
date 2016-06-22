package com.ark.qrreader.qrReaderApi.exception;

/**
 * Created by ahmedb on 6/2/16.
 */
public class QRReaderException extends Exception {

    private String errorMsg = "";

    /* Error Codes */
    public static final int UNKNOWN_EXCEPTION = 0x00;
    public static final int CAMERA_PERMISSION_NOT_GRANTED = 0x01;
    public static final int CAMERA_ERROR = 0x02;
    public static final int CALL_PERMISSION_NOT_GRANTED = 0x03;

    private int errorCode = -1;

    public QRReaderException(String errorMsg) {
        super(errorMsg);
    }

    public QRReaderException(int errorCode) {
        super(getErrorMsg(errorCode));
        this.errorCode = errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setMissingField(String missingField){

        errorMsg = "Action can't be completed duo to " + missingField + " is missing";
    }

    @Override
    public String getMessage() {

        String msg = "";

        if(!errorMsg.equalsIgnoreCase(""))
            msg = errorMsg;

        else if(errorCode != -1)
            msg = getErrorMsg(errorCode);

        else
            msg = super.getMessage();

        return msg;
    }

    private static String getErrorMsg(int errorCode) {
        String msg;
        switch (errorCode) {
            case UNKNOWN_EXCEPTION:
                msg = "Unknown exception has occurred";
                break;
            case CAMERA_PERMISSION_NOT_GRANTED:
                msg = "camera permission not granted";
                break;
            case CAMERA_ERROR:
                msg="can't open camera";
                break;
            case CALL_PERMISSION_NOT_GRANTED:
                msg = "Call permission was not granted";
                break;
            default:
                msg = "Unkwown error";
                break;
        }
        return msg;
    }
}
