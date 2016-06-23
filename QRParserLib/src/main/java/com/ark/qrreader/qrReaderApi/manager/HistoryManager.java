package com.ark.qrreader.qrReaderApi.manager;

import android.app.Activity;
import android.content.Context;

import com.ark.qrreader.qrReaderApi.models.BarCodeObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmedb on 6/15/16.
 */
public class HistoryManager {

    private final String CACHED_FILE = "barCodeObjectsListCache";
    private static HistoryManager instance;
    private List<BarCodeObject> barCodeObjects = new ArrayList<>();
    private Context context;
    private boolean duplicateHistory = false;

    public static HistoryManager getInstance() {
        if (instance == null)
            instance = new HistoryManager();
        return instance;
    }

    private void saveHistory() {

        try {

            FileOutputStream fos = context.openFileOutput(CACHED_FILE, Activity.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(barCodeObjects);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadListFromFile() {

        ObjectInputStream ois = null;
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(CACHED_FILE);
            ois = new ObjectInputStream(fis);
            List<BarCodeObject> tempObjects = (List<BarCodeObject>) ois.readObject();
            if (tempObjects != null && !tempObjects.isEmpty())
                barCodeObjects.addAll(tempObjects);

            tempObjects = null;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {

            try {
                if (fis != null)
                    fis.close();
                if (ois != null)
                    ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    public void onQRCodeRead(BarCodeObject barCodeObject, Context context) {
        this.context = context;

        if(barCodeObjects.isEmpty())
            loadListFromFile();

        if(!duplicateHistory && !barCodeObjects.contains(barCodeObject))
             barCodeObjects.add(barCodeObject);
        else if(duplicateHistory)
            barCodeObjects.add(barCodeObject);

        saveHistory();
    }

    public List<BarCodeObject> getHistoryList(Context context) {
        this.context = context;
        if (barCodeObjects.isEmpty())
            loadListFromFile();
        return barCodeObjects;
    }

    public BarCodeObject getObjectByPosition(int position) {
        return barCodeObjects.get(position);
    }

    public boolean clearHistory(Context context) {
        File file;
        try {
            file = context.getFileStreamPath(CACHED_FILE);
        } catch (NullPointerException e) {
            return false;
        }

        if (file.exists()) {
            barCodeObjects.clear();
            return file.delete();
        }
        return false;
    }

    public boolean isDuplicateHistory() {
        return duplicateHistory;
    }

    public void setDuplicateHistory(boolean duplicateHistory) {
        this.duplicateHistory = duplicateHistory;
    }
}
