package com.example.traeveler.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Toast;

import com.example.traeveler.R;
import com.example.traeveler.Registration_place;

public abstract class DialogDynamicLayout extends DialogLayout{
    public DialogDynamicLayout(Context context, String title, int iconID) {
        super(context, title, iconID);
    }

    public abstract void LayoutDialogSetPositive(View dialogView);
    public abstract void LayoutDialogSetNegative(View dialogView);

    public AlertDialog DynamicLayoutDialog(int layoutID) {
        final View dialog = View.inflate(super.getContext(), layoutID, null);
        LinearLayout dialog_checklist = dialog.findViewById(R.id.dynamic_schedulechecklist);
        final Registration_place registration_place = new Registration_place(super.getContext());
        for(int i = 0; i < registration_place.getTourlistLength(); i++) {
            LinearLayout dynamic_checklist = new LinearLayout(getContext());
            dynamic_checklist.setOrientation(LinearLayout.HORIZONTAL);

            Space space = new Space(getContext());
            LinearLayout.LayoutParams params_spc = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            params_spc.weight = 0.1f;
            space.setLayoutParams(params_spc);

            CheckBox checkBox = new CheckBox(getContext());
            LinearLayout.LayoutParams params_chk = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            params_chk.weight = 0.9f;
            checkBox.setLayoutParams(params_chk);
            checkBox.setText(registration_place.getTourlistTitle(i));
            checkBox.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked)
                        registration_place.addDelete_Tourindex(compoundButton.getText().toString());
                    else
                        registration_place.removeDelete_Tourindex(compoundButton.getText().toString());
                }
            });
            dynamic_checklist.addView(space);
            dynamic_checklist.addView(checkBox);
            dialog_checklist.addView(dynamic_checklist);
        }
        AlertDialog alertDialog;
        AlertDialog.Builder dlg = new AlertDialog.Builder(super.getContext());
        dlg.setTitle(super.getTitle());
        dlg.setIcon(super.getIconID());
        dlg.setView(dialog);
        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LayoutDialogSetPositive(dialog);
            }
        });
        dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LayoutDialogSetNegative(dialog);
            }
        });
        alertDialog = dlg.create();
        return alertDialog;
    }
}
