package com.example.traeveler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

public abstract class DialogSetting {
    private Context _Context;
    private String _title;
    private int _iconID;

    public DialogSetting(Context context, String title, int iconID) {
        _Context = context;
        _title = title;
        _iconID = iconID;
    }

    public abstract void DialogSetPositive();
    public abstract void LayoutDialogSetPositive(View dialogView);
    public abstract void DialogSetNegative();
    public abstract void LayoutDialogSetNegative(View dialogView);

    public AlertDialog DialogSimple(String content) {
        AlertDialog alertDialog;
        AlertDialog.Builder dlg = new AlertDialog.Builder(_Context);
        dlg.setTitle(_title);
        dlg.setIcon(_iconID);
        dlg.setMessage(content);
        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DialogSetPositive();
            }
        });
        dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DialogSetNegative();
            }
        });
        alertDialog = dlg.create();
        return alertDialog;
    }

    public AlertDialog LayoutDialog(int layoutID){
        AlertDialog alertDialog;
        final View dialogView = View.inflate(_Context, layoutID, null);
        AlertDialog.Builder dlg = new AlertDialog.Builder(_Context);
        dlg.setTitle(_title);
        dlg.setIcon(_iconID);
        dlg.setView(dialogView);
        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LayoutDialogSetPositive(dialogView);
            }
        });
        dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LayoutDialogSetNegative(dialogView);
            }
        });
        alertDialog = dlg.create();
        return alertDialog;
    }

    public View DialogView(int layoutID) {
        View dialogView = View.inflate(_Context, layoutID, null);
        return dialogView;
    }
}
