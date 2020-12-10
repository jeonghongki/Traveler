package com.example.traeveler.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.traeveler.R;
import com.example.traeveler.Registration_place;

import java.util.ArrayList;

public abstract class DialogSwitchLayout extends DialogLayout{
    public ArrayList<String> title = new ArrayList<>();
    private static boolean is_SwitchActive = false;

    public DialogSwitchLayout(Context context, String title, int iconID) {
        super(context, title, iconID);
        setTitle();
    }

    public abstract void LayoutDialogSetPositive(View dialogView);
    public abstract void LayoutDialogSetNegative(View dialogView);

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public void SwitchLayoutDialog(int layoutID) {
        final View dialog = View.inflate(super.getContext(), layoutID, null);
        final LinearLayout stopover = dialog.findViewById(R.id.tmap_stopover);
        final Spinner start = dialog.findViewById(R.id.tmap_start);
        final Spinner destination = dialog.findViewById(R.id.tmap_destination);
        final Switch switch_stopover = dialog.findViewById(R.id.swictch_tmapstopover);
        final Registration_place registration_place = new Registration_place(super.getContext());

        final ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, title);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        start.setAdapter(adapter);
        start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selected, long l) {
                if(selected != 0) {
                    registration_place.changeTmap_link(selected, 0);
                } else {
                    registration_place.setTmap_link(selected);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        destination.setAdapter(adapter);
        destination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selected, long l) {
                if(is_SwitchActive) {
                    if (selected != 0) {
                        registration_place.changeTmap_link(selected, 2);
                    } else {
                        registration_place.setTmap_link(selected);
                    }
                } else {
                    if (selected != 0) {
                        registration_place.changeTmap_link(selected, 1);
                    } else {
                        registration_place.setTmap_link(selected);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        switch_stopover.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                 if(isChecked){
                     is_SwitchActive = true;
                     switch_stopover.setText("경유지 활성화");
                     LinearLayout linearLayout_switch = new LinearLayout(getContext());
                     linearLayout_switch.setOrientation(LinearLayout.VERTICAL);

                     TextView textView = new TextView(getContext());
                     LinearLayout.LayoutParams params_txt = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                     params_txt.weight = 0.5f;
                     textView.setLayoutParams(params_txt);
                     textView.setText("경유지");
                     textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

                     Spinner spinner_stopover = new Spinner(getContext());
                     LinearLayout.LayoutParams params_spin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                     params_spin.weight = 0.5f;
                     spinner_stopover.setLayoutParams(params_spin);
                     spinner_stopover.setAdapter(adapter);
                     spinner_stopover.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                         @Override
                         public void onItemSelected(AdapterView<?> adapterView, View view, int selected, long l) {
                             if(selected != 0) {
                                 registration_place.changeTmap_link(selected, 1);
                             } else {
                                 registration_place.setTmap_link(selected);
                             }
                         }
                         @Override
                         public void onNothingSelected(AdapterView<?> adapterView) { }
                     });
                     linearLayout_switch.setWeightSum(1f);
                     linearLayout_switch.addView(textView);
                     linearLayout_switch.addView(spinner_stopover);
                     stopover.addView(linearLayout_switch);
                 } else {
                     is_SwitchActive = false;
                     switch_stopover.setText("경유지 비활성화");
                     stopover.removeAllViews();
                 }
             }
        });
        new AlertDialog.Builder(super.getContext())
                .setTitle(super.getTitle())
                .setIcon(super.getIconID())
                .setView(dialog)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LayoutDialogSetPositive(dialog);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LayoutDialogSetNegative(dialog);
                    }
                })
                .create().show();
    }

    private void setTitle() {
        Registration_place registration_place = new Registration_place(getContext());
        for(int i = 0; i < registration_place.getTourlistLength(); i++) {
            title.add(registration_place.getTourlistTitle(i));
        }
        title.add(0, "장소를 선택하세요");
    }
}
