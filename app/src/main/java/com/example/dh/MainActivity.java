package com.example.dh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ViewPager 구현
        ViewPager vp = findViewById(R.id.viewpager);
        Main_Viewpager_Adapter adapter = new Main_Viewpager_Adapter(getSupportFragmentManager());
        vp.setAdapter(adapter);

        String[] permission_list = {
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        };
        ActivityCompat.requestPermissions(MainActivity.this, permission_list, 1);

        //TapLayout 구현
        final TabLayout tab = findViewById(R.id.tab);
        tab.setupWithViewPager(vp);

        //ArratList를 사용함으로써 탭 레이아웃에 사용하는 이미지 제어
        ArrayList<Integer> Images = new ArrayList<>();
        //addOnPageChangeListener는 ViewPager 내부에 존재하는 페이지에 변화가 발생했을 때 호출되는 메서드
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            //ViewPager 스크롤 효과가 나타나는 동안 계속해서 호출되는 메서드
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            //해당 탭의 포지션을 매개변수로 인식하여 사용한다.
            public void onPageSelected(int position) {
                //기본적인 탭 이미지들을 모두 비활성화 시킨다,
                tab.getTabAt(0).setIcon(R.drawable.b_off);
                //tab.getTabAt(1).setIcon(R.drawable.p_off);
                //사용자가 선택한 탭이 활성화 되었을 경우, 해당 탭 이미지만 활성화 시키고 나머지는 모두 비활성화 시킨다.
                if (position == 0) tab.getTabAt(0).setIcon(R.drawable.b_on);
                //if (position == 1) tab.getTabAt(1).setIcon(R.drawable.p_on);
            }

            @Override
            //매개변수로 state를 받지만 페이지의 상태 자체를 나타내기 때문에 상관없다.
            //0 : SCROLL_STATE_DRAGGING, 1 : SCROLL_STATE_DRAGGING, 2 : SCROLL_STATE_SETTLINGQ
            public void onPageScrollStateChanged(int state) {
            }
        });
        //탭 이미지 초기화
        tab.getTabAt(0).setIcon(R.drawable.b_on);
        //tab.getTabAt(1).setIcon(R.drawable.p_off);
    }
}