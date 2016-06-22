package com.ark.qrreader.qrReaderApi.UI;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.ark.qrreader.qrReaderApi.manager.HistoryManager;
import com.ark.qrreader.myapplication.R;

/**
 * Created by ahmedb on 6/22/16.
 */
public class SettingsDialog {

    private OnHistoryCleared listener;

    public void setListener(OnHistoryCleared listener) {
        this.listener = listener;
    }

    public void showDialog(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ark_setting_dialog);

        final CheckBox allowHistoryDuplicate = (CheckBox) dialog.findViewById(R.id.allowDuplicateCheckBox);
        allowHistoryDuplicate.setChecked(HistoryManager.getInstance().isDuplicateHistory());

        allowHistoryDuplicate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                HistoryManager.getInstance().setDuplicateHistory(allowHistoryDuplicate.isChecked());

            }
        });

        Button clearHistory = (Button) dialog.findViewById(R.id.clearHistory);
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = HistoryManager.getInstance().clearHistory(activity);
                if(success)
                    Toast.makeText(activity , activity.getResources().getString(R.string.clear_history_success) , Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(activity , activity.getResources().getString(R.string.clear_history_failed) , Toast.LENGTH_SHORT).show();

                if(listener != null)
                    listener.onHistoryCleared();

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public interface OnHistoryCleared{
        public void onHistoryCleared();
    }
}
