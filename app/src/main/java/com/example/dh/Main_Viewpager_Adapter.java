package com.example.dh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class Main_Viewpager_Adapter extends FragmentPagerAdapter {
    //프래그먼트를 ArrayList형태로 Items에 저장한다.
    public ArrayList<Fragment> Items;
    //탭에 사용된 텍스트들을 ArrayList형태로 tab_test에 저장한다.
    public ArrayList<String> tab_test = new ArrayList<String>();
    public Main_Viewpager_Adapter(@NonNull FragmentManager fm) {
        super(fm);
        //프래그먼트 저장
        Items = new ArrayList<Fragment>();
        Items.add(new BluetoothActivity_fragment());
        //Items.add(new ColorSelectActivity_fragment());
        tab_test.add("컨트롤");
        //tab_test.add("소개");
    }

    public Main_Viewpager_Adapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    //기본적인 ViewPager의 아이템들에 대한 getter 메서드들
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tab_test.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return Items.get(position);
    }

    @Override
    public int getCount() {
        return Items.size();
    }
}

