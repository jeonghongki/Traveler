package com.example.traeveler;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Registration_place {
    private Context mContext;
    private TMapMarkerItem m_tMapMarkerItem;

    private RadioGroup radioGroup;

    private static boolean isEmpty_tourStart = true;
    private static boolean isEmpty_tourDestination = true;

    private static int index_Tourlist = 0;

    private final static ArrayList<TMapMarkerItem> Tour_list = new ArrayList<>();

    public Registration_place(Context context, TMapMarkerItem tMapMarkerItem){
        mContext = context;
        m_tMapMarkerItem = tMapMarkerItem;
    }

    public Registration_place(Context context){
        mContext = context;
    }

    public void RegistrationDialog(){
        AlertDialog dialog = new DialogSetting(mContext, "장소 등록하기", R.drawable.ic_mark) {
            @Override
            public void DialogSetPositive() {}
            @Override
            public void LayoutDialogSetPositive(View dialogView) {
                radioGroup = dialogView.findViewById(R.id.radioGroup);
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.tour_start:
                        if(isScheduleEmpty(mContext, "tour_start", m_tMapMarkerItem))
                            Toast.makeText(mContext, "출발지로 설정하였습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tour_stopover:
                        if(isScheduleEmpty(mContext,"tour_stopover", m_tMapMarkerItem))
                            Toast.makeText(mContext, "경유지를 추가하였습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tour_destination:
                        if(isScheduleEmpty(mContext, "tour_destination", m_tMapMarkerItem))
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

    public boolean isScheduleEmpty(final Context context, String key, final TMapMarkerItem tMapMarkerItem) {
        switch (key){
            case "tour_start":
                if(isEmpty_tourStart) {
                    setSchedule(key, tMapMarkerItem);
                    return true;
                } else {
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
                } else {
                    changeLocation(context, "도착지", tMapMarkerItem, index_Tourlist);
                    return false;
                }
            default:
                Toast.makeText(mContext, "일정 추가에 오류가 발생하였습니다!\n잠시후 다시 시도해주세요!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mContext, "일정 추가에 오류가 발생하였습니다!\n잠시후 다시 시도해주세요!", Toast.LENGTH_SHORT).show();
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

    public boolean isEmpty_Tourlist() {
        if(Tour_list.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public int getTourlistLength() {
        return Tour_list.size();
    }

    public String getTourlistTitle(int index) {
        return Tour_list.get(index).getCalloutTitle();
    }

    public TMapMarkerItem getTourlistMark(int index) {
        return Tour_list.get(index);
    }

    public TMapPoint getTourlistMarkPoint(int index) {
        return Tour_list.get(index).getTMapPoint();
    }

    public String getTourlistLongitude(int index) { return String.valueOf(Tour_list.get(index).getTMapPoint().getLongitude()); }

    public String getTourlistLatitude(int index) { return String.valueOf(Tour_list.get(index).getTMapPoint().getLatitude()); }

    public void setTourlistTitle(String title, int index) { Tour_list.get(index).setCalloutTitle(title);}

    public void setTourlistFromDB(TravelerDB travelerDB, String Date) {
        ArrayList<String> Title = new ArrayList<>();
        ArrayList<Double> Longitude = new ArrayList<>();
        ArrayList<Double> Latitude = new ArrayList<>();
        travelerDB.GetQueryData(travelerDB, Date, Title, Longitude, Latitude);
        setTourlistMarkFromDB(Title, Longitude, Latitude);
    }

    private void setTourlistMarkFromDB(ArrayList<String> Title, ArrayList<Double> Longitude, ArrayList<Double> Latitude) {
        for(int i = 0; i < Title.size(); i++) {
            TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
            tMapMarkerItem.setCalloutTitle(Title.get(i));
            tMapMarkerItem.setTMapPoint(new TMapPoint(Latitude.get(i), Longitude.get(i)));
            if(i == 0) {
                tMapMarkerItem.setCalloutSubTitle("출발지");
                setSchedule("tour_start", tMapMarkerItem);
            } else if (i == Title.size() - 1) {
                tMapMarkerItem.setCalloutSubTitle("도착지");
                setSchedule("tour_destination", tMapMarkerItem);
            } else {
                tMapMarkerItem.setCalloutSubTitle("경유지" + i);
                setSchedule("tour_stopover", tMapMarkerItem);
            }
        }
    }

    public void TourlistReset() {
        Tour_list.clear();
        index_Tourlist = 0;
        isEmpty_tourStart = true;
        isEmpty_tourDestination = true;
    }
}
