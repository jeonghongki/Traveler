package com.example.traeveler.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

public abstract class DialogLayout {
    private Context _Context;
    private String _title;
    private int _iconID;

    public DialogLayout(Context context, String title, int iconID) {
        _Context = context;
        _title = title;
        _iconID = iconID;
    }

    public abstract void LayoutDialogSetPositive(View dialogView);
    public abstract void LayoutDialogSetNegative(View dialogView);

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

    public Context getContext() { return _Context; }

    public String getTitle() { return _title; }

    public int getIconID() { return  _iconID; }
}
