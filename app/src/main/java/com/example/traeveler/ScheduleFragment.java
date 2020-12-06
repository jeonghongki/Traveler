package com.example.traeveler;

import android.app.AlertDialog;
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
import android.widget.DatePicker;
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
import java.util.Random;

public class ScheduleFragment extends Fragment {
    private LinearLayout DynamicAddList;
    private Button add_btn, btn_mapMark, btn_markerPath, btn_pathSeek, btn_searchDB, btn_resetDB, btn_saveDB;
    private static Button btn_dataload;
    private TextView end_list_textview;
    private EditText edt_tourstart, edt_tourdestination;
    private DatePicker datePicker;
    private int mYear, mMonth, mDay;
    private String mDate;

    private MainActivity activity;
    private TravelerDB travelerDB;

    private final static ArrayList<EditText> Dynamic_edtlist = new ArrayList<>();

    private int listNumber = 1;
    private boolean isDBopen = false;

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
        btn_searchDB = scheduleview.findViewById(R.id.btn_searchDB);
        btn_resetDB = scheduleview.findViewById(R.id.btn_resetDB);
        btn_saveDB = scheduleview.findViewById(R.id.btn_saveDB);
        end_list_textview = scheduleview.findViewById(R.id.end_list_textview);
        edt_tourstart = scheduleview.findViewById(R.id.tour_start);
        edt_tourdestination = scheduleview.findViewById(R.id.tour_destination);
        datePicker = scheduleview.findViewById(R.id.datePicker);

        mYear = datePicker.getYear();
        mMonth = datePicker.getMonth();
        mDay = datePicker.getDayOfMonth();
        mDate = mYear + "/" + mMonth + "/" + mDay;

        travelerDB = new TravelerDB(getContext());

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
        btn_searchDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(travelerDB.isExistData(travelerDB, mDate, "Date")) {
                    isDBopen = true;
                    ScheduleListReset(getContext());
                    Registration_place registration_place = new Registration_place(getContext());
                    registration_place.setTourlistFromDB(travelerDB, mDate);
                    btn_dataload.performClick();
                } else {
                    Toast.makeText(getContext(), "일정이 존재하지 않습니다!", Toast.LENGTH_SHORT).show();
                    isDBopen = false;
                }
            }
        });
        btn_resetDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDBopen) {
                    travelerDB.resetDB(travelerDB);
                    isDBopen = false;
                }
                ScheduleListReset(getContext());
                DynamicAddList.removeAllViews();
                Dynamic_edtlist.clear();
                listNumber = 1;
                end_list_textview.setText((listNumber + 1) + ".");
            }
        });
        btn_saveDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Registration_place registration_place = new Registration_place(getContext());
                if(!registration_place.isEmpty_Tourlist()) {
                    Random random = new Random();
                    String Id = String.valueOf(random.nextInt());
                    while (travelerDB.isExistData(travelerDB, Id, "Id")) {
                        Id = String.valueOf(random.nextInt());
                    }
                    travelerDB.InsertDate(travelerDB, Id, mDate);
                    for(int i = 0; i < registration_place.getTourlistLength(); i++) {
                        travelerDB.InsertSchedule(travelerDB, String.valueOf(i), registration_place.getTourlistTitle(i),
                                registration_place.getTourlistLongitude(i), registration_place.getTourlistLatitude(i), Id);
                    }
                    Toast.makeText(getContext(), "일정을 저장하였습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog dialog = new DialogSetting(getContext(), "주의", R.drawable.ic_save) {
                        @Override
                        public void DialogSetPositive() {
                            Random random = new Random();
                            String Id = String.valueOf(random.nextInt());
                            while (travelerDB.isExistData(travelerDB, Id, "Id")) {
                                Id = String.valueOf(random.nextInt());
                            }
                            travelerDB.InsertDate(travelerDB, Id, mDate);
                        }
                        @Override
                        public void LayoutDialogSetPositive(View dialogView) { }
                        @Override
                        public void DialogSetNegative() {
                            Toast.makeText(getContext(), "저장을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void LayoutDialogSetNegative(View dialogView) { }
                    }.DialogSimple("저장할 여행지가 존재하지 않습니다!\n현재 설정된 날짜만 저장하시겠습니까?");
                    dialog.show();
                }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                    mYear = datePicker.getYear();
                    mMonth = datePicker.getMonth();
                    mDay = datePicker.getDayOfMonth();
                    mDate = mYear + "/" + mMonth + "/" + mDay;
                }
            });
        } else {
            Toast.makeText(getContext(), "날짜를 가져오는데 문제가 발생했습니다!", Toast.LENGTH_SHORT).show();
        }
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
        textView.setText((listNumber + 2) + ".");
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
        // 출발지, 도착지, 경유지 설정시 해당 위치에 정확하게 들어가도록 설정하기! -- 수정필요!!
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

    private void ScheduleListReset(Context context) {
        Registration_place registration_place = new Registration_place(context);
        registration_place.TourlistReset();
        edt_tourstart.setText("");
        edt_tourdestination.setText("");
    }
}
