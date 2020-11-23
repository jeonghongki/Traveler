package com.example.traeveler;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends FragmentActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ContentsPagerAdapter mContentPagerAdapter;

    private boolean ScheduleMarkerShow = false;
    private boolean ScheduleMarkerPathShow = false;
    private boolean ScheduleCarPathShow = false;

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
    }

    public void setScheduleMarkerPathShow(boolean bool) {
        this.ScheduleMarkerPathShow = bool;
        this.ScheduleMarkerShow = !bool;
        this.ScheduleCarPathShow = !bool;
    }

    public void setSceduleCarPathShow(boolean bool) {
        this.ScheduleCarPathShow = bool;
        this.ScheduleMarkerShow = !bool;
        this.ScheduleMarkerPathShow = !bool;
    }
}