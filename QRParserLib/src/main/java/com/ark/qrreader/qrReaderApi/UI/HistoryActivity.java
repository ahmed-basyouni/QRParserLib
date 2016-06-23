package com.ark.qrreader.qrReaderApi.UI;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ark.qrreader.qrReaderApi.manager.HistoryManager;
import com.ark.qrreader.qrReaderApi.models.BarCodeObject;
import com.ark.qrreader.myapplication.R;
import com.ark.qrreader.qrReaderApi.parser.QRParser;

import java.util.List;

public class HistoryActivity extends Activity implements SettingsDialog.OnHistoryCleared{

    private ListView historyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.ark_history_activity);

        Button scanBtn = (Button) findViewById(R.id.scanBtn);
        historyList = (ListView) findViewById(R.id.historyList);
        historyList.setEmptyView(findViewById(R.id.emptyElement));
        historyList.setAdapter(new BarCodeObjectsHistoryAdapter());

        scanBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                HistoryActivity.this.finish();
            }
        });

        findViewById(R.id.settingBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SettingsDialog dialog = new SettingsDialog();
                dialog.setListener(HistoryActivity.this);
                dialog.showDialog(HistoryActivity.this);
            }
        });

    }

    @Override
    public void onHistoryCleared() {
        historyList.setAdapter(new BarCodeObjectsHistoryAdapter());
    }

    private class BarCodeObjectsHistoryAdapter extends BaseAdapter {

        List<BarCodeObject> barCodeObjectList;

        public BarCodeObjectsHistoryAdapter() {
            this.barCodeObjectList = HistoryManager.getInstance().getHistoryList(HistoryActivity.this);
        }

        @Override
        public int getCount() {
            return barCodeObjectList.size();
        }

        @Override
        public Object getItem(int i) {
            return barCodeObjectList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            HistoryViewHolder viewHolder;
            if (view == null) {
                viewHolder = new HistoryViewHolder();
                view = getLayoutInflater().inflate(R.layout.ark_history_row, viewGroup, false);
                viewHolder.displayValue = (TextView) view.findViewById(R.id.displayValue);
                viewHolder.qrType = (TextView) view.findViewById(R.id.qrType);
                view.setTag(viewHolder);

            } else {

                viewHolder = (HistoryViewHolder) view.getTag();
            }

            viewHolder.displayValue.setText(barCodeObjectList.get(i).getDisplayValue());
            viewHolder.qrType.setText(barCodeObjectList.get(i).getQrType());

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HistoryActivity.this, CaptureActivity.class);
                    intent.putExtra(CaptureActivity.OBJECT_POSITION, i);
                    QRParser.getInstance().setBarCodeObject(barCodeObjectList.get(i));
                    startActivity(intent);
                }
            });

            return view;
        }

        private class HistoryViewHolder {
            TextView displayValue, qrType;
        }
    }

}
