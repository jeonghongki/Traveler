package com.example.traeveler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skt.Tmap.TMapAddressInfo;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class MapFragment extends Fragment {
    private static String myTmapApiKey = "l7xxde49dc37a0c5414aabfb99bd28677815";

    private double longitude;
    private double latitude;

    private GpsTracker gpsTracker;

    private FloatingActionButton fab_main, fab_search, fab_gps, fab_navigation;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;

    private boolean isMarkerSelected = false;
    private boolean isScrollWithZoom = false;
    private boolean isPinBalloonOpen = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mapview = inflater.inflate(R.layout.map_fragment, container, false);
        final Context context = container.getContext();

        LinearLayout linearLayoutTmap = mapview.findViewById(R.id.linearLayoutTmap);

        gpsTracker = new GpsTracker(context, getActivity());
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        final TMapView tMapView = new TMapView(context);
        tMapView.setSKTMapApiKey(myTmapApiKey);
//        tMapView.setCenterPoint(longitude, latitude);
        tMapView.setCenterPoint(128.099383, 35.153312);
        linearLayoutTmap.addView(tMapView);

        fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(context, R.anim.fab_close);

        fab_main = mapview.findViewById(R.id.open_main);
        fab_search = mapview.findViewById(R.id.search_POI);
        fab_gps = mapview.findViewById(R.id.set_gpslocation);
        fab_navigation = mapview.findViewById(R.id.Tmap_navigation);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
            }
        });
        fab_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
                SearchPOI searchpoi = new SearchPOI(context, tMapView);
                searchpoi.searchDialog();
            }
        });
        fab_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
                tMapView.setCenterPoint(longitude, latitude);
            }
        });
        fab_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
            }
        });
        tMapView.setOnEnableScrollWithZoomLevelListener(new TMapView.OnEnableScrollWithZoomLevelCallback() {
            @Override
            public void onEnableScrollWithZoomLevelEvent(float v, TMapPoint tMapPoint) {
                isScrollWithZoom = true;
            }
        });
        tMapView.setOnDisableScrollWithZoomLevelListener(new TMapView.OnDisableScrollWithZoomLevelCallback() {
            @Override
            public void onDisableScrollWithZoomLevelEvent(float v, TMapPoint tMapPoint) {
                isScrollWithZoom = false;
            }
        });
        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                if (arrayList.isEmpty()) {
                    if(isPinBalloonOpen) {
                        isPinBalloonOpen = false;
                        isMarkerSelected = true;
                    } else
                        isMarkerSelected = false;
                }else {
                    isMarkerSelected = true;
                    isPinBalloonOpen = true;
                }
                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                if(!isScrollWithZoom && !isMarkerSelected && !isPinBalloonOpen) {
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.touched_pin);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);

                    final TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
                    tMapMarkerItem.setTMapPoint(tMapPoint);
                    tMapMarkerItem.setIcon(bitmap);
                    tMapMarkerItem.setPosition(0.5f, 1.0f);

                    Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pin_tour);
                    bitmap1 = Bitmap.createScaledBitmap(bitmap1, 50, 50, false);
                    TMapData tMapData = new TMapData();
                    tMapData.reverseGeocoding(tMapPoint.getLatitude(), tMapPoint.getLongitude(), "A04", new TMapData.reverseGeocodingListenerCallback() {
                        @Override
                        public void onReverseGeocoding(TMapAddressInfo tMapAddressInfo) {
                            tMapMarkerItem.setCalloutTitle(tMapAddressInfo.strFullAddress);
                            if(tMapAddressInfo.strBuildingName == null)
                                tMapMarkerItem.setCalloutTitle(tMapAddressInfo.strFullAddress);
                            else
                                tMapMarkerItem.setCalloutTitle(tMapAddressInfo.strBuildingName);
                        }
                    });
                    tMapMarkerItem.setCalloutSubTitle("선택된 위치");
                    tMapMarkerItem.setCanShowCallout(true);
                    tMapMarkerItem.setAutoCalloutVisible(false);
                    tMapMarkerItem.setCalloutRightButtonImage(bitmap1);

                    tMapView.addMarkerItem("touched", tMapMarkerItem);
                }
                return false;
            }
        });
        tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
                isPinBalloonOpen = true;
                Registration_place registration_place = new Registration_place(context, tMapMarkerItem);
                registration_place.RegistrationDialog();
            }
        });
        return mapview;
    }

    private void toggleFab(){
        if(isFabOpen){
            fab_main.setImageResource(R.drawable.ic_add);
            fab_search.startAnimation(fab_close);
            fab_navigation.startAnimation(fab_close);
            fab_gps.startAnimation(fab_close);
            fab_search.setVisibility(View.INVISIBLE);
            fab_navigation.setVisibility(View.INVISIBLE);
            fab_gps.setVisibility(View.INVISIBLE);
            isFabOpen = false;
        } else {
            fab_main.setImageResource(R.drawable.ic_close);
            fab_search.startAnimation(fab_open);
            fab_navigation.startAnimation(fab_open);
            fab_gps.startAnimation(fab_open);
            fab_search.setVisibility(View.VISIBLE);
            fab_navigation.setVisibility(View.VISIBLE);
            fab_gps.setVisibility(View.VISIBLE);
            isFabOpen = true;
        }
    }

}