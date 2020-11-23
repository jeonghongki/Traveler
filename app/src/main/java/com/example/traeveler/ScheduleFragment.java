package com.example.traeveler;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment {
    private LinearLayout DynamicAddList;
    private Button add_btn, btn_mapMark, btn_markerPath, btn_pathSeek;
    private static Button btn_dataload;
    private TextView end_list_textview;
    private EditText edt_tourstart, edt_tourdestination;

    private MainActivity activity;

    private final static ArrayList<EditText> Dynamic_edtlist = new ArrayList<>();

    private int listNumber = 1;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View scheduleview = inflater.inflate(R.layout.schedule_fragment, container, false);

        DynamicAddList = scheduleview.findViewById(R.id.add_list);
        add_btn = scheduleview.findViewById(R.id.add_btn);
        btn_dataload = scheduleview.findViewById(R.id.btn_dataload);
        btn_mapMark = scheduleview.findViewById(R.id.btn_mapMark);
        btn_markerPath = scheduleview.findViewById(R.id.btn_markerPath);
        btn_pathSeek = scheduleview.findViewById(R.id.btn_pathSeek);
        end_list_textview = scheduleview.findViewById(R.id.end_list_textview);
        edt_tourstart = scheduleview.findViewById(R.id.tour_start);
        edt_tourdestination = scheduleview.findViewById(R.id.tour_destination);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStopoverList(DynamicAddList, end_list_textview);
                Dynamic_edtlist.get(listNumber - 2).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        edtFocusEvent(Dynamic_edtlist.get(listNumber - 2), b);
                    }
                });
            }
        });
        btn_dataload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DynamicAddList.removeAllViews();
                Dynamic_edtlist.clear();
                listNumber = 1;
                setScheduleList(getContext(), DynamicAddList, end_list_textview);
            }
        });
        btn_mapMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtTitleUpdate(getContext());
                activity.setScheduleMarkerShow(true);
                activity.tabchange(0);
            }
        });
        btn_markerPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtTitleUpdate(getContext());
                activity.setScheduleMarkerPathShow(true);
                activity.tabchange(0);
            }
        });
        btn_pathSeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setSceduleCarPathShow(true);
                activity.tabchange(0);
            }
        });
        edt_tourstart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                edtFocusEvent(edt_tourstart, b);
            }
        });
        edt_tourdestination.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                edtFocusEvent(edt_tourdestination, b);
            }
        });
        return scheduleview;
    }

    public void addStopoverList(LinearLayout linearLayout, TextView textView) {
        LinearLayout dynamic_layout = new LinearLayout(getContext());
        dynamic_layout.setOrientation(LinearLayout.HORIZONTAL);

        TextView dynamic_test = new TextView(getContext());
        LinearLayout.LayoutParams params_txt = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        params_txt.weight = 0.1f;
        dynamic_test.setLayoutParams(params_txt);
        dynamic_test.setGravity(Gravity.CENTER);
        dynamic_test.setText((listNumber + 1) + ".");
        String temp = String.valueOf(listNumber + 2);
        textView.setText(temp + ".");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        final EditText dynamic_edt = new EditText(getContext());
        LinearLayout.LayoutParams params_edt = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        params_edt.weight = 0.7f;
        dynamic_edt.setLayoutParams(params_edt);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            dynamic_edt.setId(View.generateViewId());
        } else {
            dynamic_edt.setId(ViewCompat.generateViewId());
        }
        Dynamic_edtlist.add(dynamic_edt);

        Space dynamic_spc = new Space(getContext());
        LinearLayout.LayoutParams params_spc = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        params_spc.weight = 0.2f;
        dynamic_spc.setLayoutParams(params_spc);

        dynamic_layout.setWeightSum(1f);
        dynamic_layout.addView(dynamic_test);;
        dynamic_layout.addView(dynamic_edt);
        dynamic_layout.addView(dynamic_spc);

        linearLayout.addView(dynamic_layout);
        listNumber++;
    }

    public void setScheduleList(Context context, LinearLayout linearLayout, TextView textView) {
        Registration_place registration_place = new Registration_place(context);
        if(!registration_place.isEmpty_Tourlist()){
            for (int i = 0; i < registration_place.getTourlistLength(); i++) {
                if (i == 0) {
                    edt_tourstart.setText(registration_place.getTourlistTitle(i));
                } else if (i > 0 && i < registration_place.getTourlistLength() - 1) {
                    addStopoverList(linearLayout, textView);
                    Dynamic_edtlist.get(i - 1).setText(registration_place.getTourlistTitle(i));
                } else {
                    edt_tourdestination.setText(registration_place.getTourlistTitle(i));
                }
            }
        } else {
            textView.setText((listNumber + 1) + ".");
            Toast.makeText(context, "설정한 목적지가 없습니다!", Toast.LENGTH_SHORT).show();
        }
    }

    public void tabClickEvent() {
        btn_dataload.performClick();
    }

    public void edtFocusEvent(EditText editText, boolean hasfocus) {
        if(hasfocus && TextUtils.isEmpty(editText.getText().toString())) {
            Toast.makeText(getContext(), "지도 탭에서 등록해주세요", Toast.LENGTH_SHORT).show();
            editText.clearFocus();
            activity.tabchange(0);
        }
    }

    private void edtTitleUpdate(Context context) {
        Registration_place registration_place = new Registration_place(context);
        if(!registration_place.isEmpty_Tourlist()) {
            for (int i = 0; i < registration_place.getTourlistLength(); i++) {
                if(i == 0) {
                    if(!edt_tourstart.getText().equals(registration_place.getTourlistTitle(i))) {
                        registration_place.setTourlistTitle(edt_tourstart.getText().toString(), i);
                    }
                } else if (i > 0 && i < registration_place.getTourlistLength() - 1) {
                    if(!Dynamic_edtlist.get(i - 1).getText().equals(registration_place.getTourlistTitle(i))) {
                        registration_place.setTourlistTitle(Dynamic_edtlist.get(i - 1).getText().toString(), i);
                    }
                } else {
                    if(!edt_tourdestination.getText().equals(registration_place.getTourlistTitle(i))) {
                        registration_place.setTourlistTitle(edt_tourdestination.getText().toString(), i);
                    }
                }
            }
        }
    }
}
