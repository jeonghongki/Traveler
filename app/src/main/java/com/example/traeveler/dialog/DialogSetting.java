package com.example.traeveler.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

public class DialogSetting {
    private Context _Context;
    private String _title;
    private int _iconID;

    public DialogSetting(Context context, String title, int iconID) {
        _Context = context;
        _title = title;
        _iconID = iconID;
    }

    public void DialogSimple(String content, final Runnable onConfirm, final Runnable onDenied) {
        new AlertDialog.Builder(_Context)
                .setTitle(_title)
                .setMessage(content)
                .setIcon(_iconID)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onConfirm.run();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onDenied.run();
                    }
                })
                .create().show();
    }

    public void DialogVerySimple(String content, final Runnable onConfirm) {
        new AlertDialog.Builder(_Context)
                .setTitle(_title)
                .setMessage(content)
                .setIcon(_iconID)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onConfirm.run();
                    }
                })
                .create().show();
    }

    public void DialogThreeButton(String content, final Runnable onConfirm, final Runnable onDenied, final Runnable onNeutral) {
        new AlertDialog.Builder(_Context)
                .setTitle(_title)
                .setMessage(content)
                .setIcon(_iconID)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onConfirm.run();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onDenied.run();
                    }
                })
                .setNeutralButton("리스트 초기화", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onNeutral.run();
                    }
                })
                .create().show();
    }
}
