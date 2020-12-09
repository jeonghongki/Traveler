package com.example.traeveler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.traeveler.dialog.DialogLayout;
import com.example.traeveler.dialog.DialogSetting;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class SearchPOI {
    private EditText edt_search;
    private Context mContext;
    private TMapView m_tMapView;

    public SearchPOI(Context context, TMapView tMapView){
        mContext = context;
        m_tMapView = tMapView;
    }

    public void searchDialog(){
        new DialogLayout(mContext, "명칭(POI) 통합검색", R.drawable.ic_search) {
            @Override
            public void LayoutDialogSetPositive(View dialogView) {
                edt_search = dialogView.findViewById(R.id.edt_search);
                final ArrayList<TMapPoint> arrTMapPoint = new ArrayList<>();
                final ArrayList<String> arrTitle = new ArrayList<>();
                TMapData tMapData = new TMapData();
                tMapData.findAllPOI(edt_search.getText().toString(), new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList poiItem) {
                        for (int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem item = (TMapPOIItem) poiItem.get(i);
                            arrTitle.add(item.getPOIName());
                            arrTMapPoint.add(item.getPOIPoint());
                        }
                        setMultiMarkers(arrTMapPoint, arrTitle);
                    }
                });
            }
            @Override
            public void LayoutDialogSetNegative(View dialogView) {
                Toast.makeText(mContext, "명칭(POI) 통합검색을 취소하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        }.LayoutDialog(R.layout.dialog_searchpoi).show();
    }

    private void setMultiMarkers(ArrayList<TMapPoint> arrTPoint, ArrayList<String> arrTitle) {
        for(int i = 0; i < arrTPoint.size(); i++){
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin);
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);

            TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
            tMapMarkerItem.setIcon(bitmap);
            tMapMarkerItem.setPosition(0.5f, 1.0f);
            tMapMarkerItem.setTMapPoint(arrTPoint.get(i));
            tMapMarkerItem.setName(arrTitle.get(i));

            Bitmap bitmap1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pin_tour);
            bitmap1 = Bitmap.createScaledBitmap(bitmap1, 50, 50, false);
            tMapMarkerItem.setCalloutTitle(arrTitle.get(i));
            tMapMarkerItem.setCalloutSubTitle(edt_search.getText().toString());
            tMapMarkerItem.setCanShowCallout(true);
            tMapMarkerItem.setAutoCalloutVisible(false);
            tMapMarkerItem.setCalloutRightButtonImage(bitmap1);

            m_tMapView.addMarkerItem("markerItem" + i, tMapMarkerItem);;
        }
    }
}
