package com.example.traeveler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.skt.Tmap.TMapMarkerItem;

public class Registration_place {
    private Context mContext;
    private TMapMarkerItem m_tMapMarkerItem;

    private RadioGroup radioGroup;

    public Registration_place(Context context, TMapMarkerItem tMapMarkerItem){
        mContext = context;
        m_tMapMarkerItem = tMapMarkerItem;
    }

    public void RegistrationDialog(){
        AlertDialog dialog = new DialogSetting(mContext, "장소 등록하기", R.drawable.ic_mark) {
            @Override
            public void DialogSetPositive() {}
            @Override
            public void LayoutDialogSetPositive(View dialogView) {
                radioGroup = dialogView.findViewById(R.id.radioGroup);
                ScheduleFragment scheduleFragment = new ScheduleFragment();
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.tour_start:
                        if(scheduleFragment.isScheduleEmpty(mContext, "tour_start", m_tMapMarkerItem))
                            Toast.makeText(mContext, "출발지로 설정하였습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tour_stopover:
                        if(scheduleFragment.isScheduleEmpty(mContext,"tour_stopover", m_tMapMarkerItem))
                            Toast.makeText(mContext, "경유지를 추가하였습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tour_destination:
                        if(scheduleFragment.isScheduleEmpty(mContext, "tour_destination", m_tMapMarkerItem))
                            Toast.makeText(mContext, "도착지로 설정하였습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(mContext, "장소 등록에 오류가 발생하였습니다!\n잠시후 다시 시도해주세요!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void DialogSetNegative() {}
            @Override
            public void LayoutDialogSetNegative(View dialogView) {
                Toast.makeText(mContext, "장소 등록을 취소하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        }.LayoutDialog(R.layout.dialog_pin_registration);
        dialog.show();
    }
}
