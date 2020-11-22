package com.example.traeveler;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

import com.skt.Tmap.TMapMarkerItem;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment {
    private LinearLayout DynamicAddList;
    private Button add_btn;
    private TextView end_list_textview;
    private EditText edt_tourstart, edt_tourdestination;

    private static boolean isEmpty_tourStart = true;
    private static boolean isEmpty_tourDestination = true;

    private final ArrayList<TMapMarkerItem> Tour_list = new ArrayList<>();

    private int index_Tourlist = 0;
    private int listNumber = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View scheduleview = inflater.inflate(R.layout.schedule_fragment, container, false);

        DynamicAddList = scheduleview.findViewById(R.id.add_list);
        add_btn = scheduleview.findViewById(R.id.add_btn);
        end_list_textview = scheduleview.findViewById(R.id.end_list_textview);
        edt_tourstart = scheduleview.findViewById(R.id.tour_start);
        edt_tourdestination = scheduleview.findViewById(R.id.tour_destination);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStopoverList(scheduleview, DynamicAddList, end_list_textview);
            }
        });
        return scheduleview;
    }

    public boolean isScheduleEmpty(final Context context, String key, final TMapMarkerItem tMapMarkerItem) {
        switch (key){
            case "tour_start":
                if(isEmpty_tourStart) {
                    setSchedule(key, tMapMarkerItem);
                    return true;
                }
                else {
                    changeLocation(context, "출발지", tMapMarkerItem, 0);
                    return false;
                }
            case "tour_stopover":
                setSchedule(key, tMapMarkerItem);
                return true;
            case "tour_destination":
                if(isEmpty_tourDestination) {
                    setSchedule(key, tMapMarkerItem);
                    return true;
                }
                else {
                    changeLocation(context, "도착지", tMapMarkerItem, index_Tourlist);
                    return false;
                }
            default:
                Toast.makeText(getContext(), "일정 추가에 오류가 발생하였습니다!\n잠시후 다시 시도해주세요!", Toast.LENGTH_SHORT).show();
               return false;
        }
    }

    public void setSchedule(String key, TMapMarkerItem tMapMarkerItem) {
        switch (key){
            case "tour_start":
                Tour_list.add(index_Tourlist, tMapMarkerItem);
                isEmpty_tourStart = false;
                index_Tourlist++;
                break;
            case "tour_stopover":
                Tour_list.add(index_Tourlist, tMapMarkerItem);
                index_Tourlist++;
                break;
            case "tour_destination":
                Tour_list.add(index_Tourlist, tMapMarkerItem);
                isEmpty_tourDestination = false;
                break;
            default:
                Toast.makeText(getContext(), "일정 추가에 오류가 발생하였습니다!\n잠시후 다시 시도해주세요!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void changeLocation(final Context context, final String string, final TMapMarkerItem tMapMarkerItem, final int index) {
        AlertDialog dialog = new DialogSetting(context, "알림", R.drawable.ic_editlocation) {
            @Override
            public void DialogSetPositive() {
                Tour_list.set(index, tMapMarkerItem);
                Toast.makeText(context, string + "를 변경하였습니다!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void LayoutDialogSetPositive(View dialogView) { }
            @Override
            public void DialogSetNegative() {
                Toast.makeText(context, string + " 변경을 취소하였습니다!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void LayoutDialogSetNegative(View dialogView) { }
        }.DialogSimple(string + "가 이미 설정되어 있습니다.\n수정하시겠습니까?");
        dialog.show();
    }

    public void addStopoverList(View view, LinearLayout linearLayout, TextView textView) {
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

        EditText dynamic_edt = new EditText(getContext());
        LinearLayout.LayoutParams params_edt = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        params_edt.weight = 0.7f;
        dynamic_edt.setLayoutParams(params_edt);

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
}
