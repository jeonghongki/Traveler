package com.example.traeveler;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.traeveler.dialog.DialogSetting;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends FragmentActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ContentsPagerAdapter mContentPagerAdapter;

    private boolean ScheduleMarkerShow = false;
    private boolean ScheduleMarkerPathShow = false;
    private boolean ScheduleCarPathShow = false;
    private boolean ScheduleMarkerReset = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = findViewById(R.id.tabs);
        mTabLayout.addTab(mTabLayout.newTab().setText("지도"));
        mTabLayout.addTab(mTabLayout.newTab().setText("일정"));
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_location);
        mTabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#E53942"), PorterDuff.Mode.SRC_IN);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_schedule);

        mViewPager = findViewById(R.id.pager_content);
        mContentPagerAdapter = new ContentsPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mContentPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#E53942"), PorterDuff.Mode.SRC_IN);
                mViewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()){
                    case 0:
                        MapFragment mapFragment = new MapFragment();
                        if(ScheduleMarkerReset) {
                            mapFragment.ScheduleMarkerReset();
                            ScheduleMarkerReset = false;
                        }
                        if(ScheduleMarkerShow) {
                            mapFragment.ScheduleMarkerShow();
                        } else if(ScheduleMarkerPathShow) {
                            mapFragment.ScheduleMarkerShow();
                            mapFragment.ScheduleMarkerPathShow();
                        } else if(ScheduleCarPathShow) {
                            mapFragment.ScheduleMarkerShow();
                            mapFragment.ScheduleCarPathShow();
                        }
                        break;
                    case 1:
                        ScheduleFragment scheduleFragment = new ScheduleFragment();
                        scheduleFragment.tabClickEvent();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void tabchange(int index) {
        TabLayout.Tab tab = mTabLayout.getTabAt(index);
        tab.select();
    }

    public void setScheduleMarkerShow(boolean bool) {
        this.ScheduleMarkerShow = bool;
        this.ScheduleMarkerPathShow = !bool;
        this.ScheduleCarPathShow = !bool;
        if(!this.ScheduleMarkerReset)
            this.ScheduleMarkerReset = !bool;
    }

    public void setScheduleMarkerPathShow(boolean bool) {
        this.ScheduleMarkerPathShow = bool;
        this.ScheduleMarkerShow = !bool;
        this.ScheduleCarPathShow = !bool;
        if(!this.ScheduleMarkerReset)
            this.ScheduleMarkerReset = !bool;
    }

    public void setScheduleCarPathShow(boolean bool) {
        this.ScheduleCarPathShow = bool;
        this.ScheduleMarkerShow = !bool;
        this.ScheduleMarkerPathShow = !bool;
        if(!this.ScheduleMarkerReset)
            this.ScheduleMarkerReset = !bool;
    }

    public void setScheduleMarkerReset(boolean bool) {
        this.ScheduleMarkerReset = bool;
        this.ScheduleMarkerShow = !bool;
        this.ScheduleMarkerPathShow = !bool;
        this.ScheduleCarPathShow = !bool;
    }

    @Override
    @SuppressLint("ResourceType")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode != 0) return;
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            new DialogSetting(this, "권한 승인", R.drawable.ic_feedback).DialogVerySimple("권한 요청이 승인되었습니다.\nGPS 데이터를 가져오고 있습니다. 다시 실행해주세요!",
                    new Runnable() {
                        @Override
                        public void run() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                finishAndRemoveTask();
                            } else {
                                finish();
                            }
                        }
                    });
        } else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new DialogSetting(this, "권한 거부", R.drawable.ic_feedback).DialogVerySimple("권한 요청이 거부되었습니다.\n다시 실행하여서 권한을 허용해주세요!",
                        new Runnable() {
                            @Override
                            public void run() {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    finishAndRemoveTask();
                                } else {
                                    finish();
                                }
                            }
                        });
            } else {
                Toast.makeText(this, "test2", Toast.LENGTH_SHORT).show();
                new DialogSetting(this, "권한 거부", R.drawable.ic_feedback).DialogVerySimple("권한 요청이 거부되었습니다.\n설정에서 권한을 허용해주세요!",
                        new Runnable() {
                            @Override
                            public void run() {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    finishAndRemoveTask();
                                } else {
                                    finish();
                                }
                            }
                        });
            }
        }

    }
}