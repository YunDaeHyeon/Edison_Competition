package com.example.dh;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import petrov.kristiyan.colorpicker.ColorPicker;

public class BluetoothActivity_fragment extends Fragment{
    String TAG = "BluetoothActivity_fragment";
    UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier
    BluetoothAdapter btAdapter;
    Set<BluetoothDevice> pairedDevices;
    ArrayAdapter<String> btArrayAdapter;
    ArrayList<String> deviceAddressArray;
    TextView Status;
    ConstraintLayout layout;
    ImageButton btnParied, btnPower, btnSet, btnAuto, btnDetect;
    ListView listView;
    BluetoothSocket btSocket = null;
    ConnectedThread connectedThread;
    private  final static int REQUEST_BT = 1;
    //HEX 패턴
    int HEXLENGTH = 8;
    String hexaDecimalPattern = "^0x([\\da-fA-F]{1,8})$";
    String hexForRGBConversion = "";
    String ndata = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.bluetooth_fragment, container, false);
        Status = (TextView)rootView.findViewById(R.id.text_status);
        btnParied = (ImageButton)rootView.findViewById(R.id.btn_paired);
        //bthSearch = (Button)rootView.findViewById(R.id.btn_search);
        btnPower = (ImageButton)rootView.findViewById(R.id.power_button);
        btnSet = (ImageButton)rootView.findViewById(R.id.set_color_button);
        //btnAuto = (Button)rootView.findViewById(R.id.auto_color_button);
        btnDetect = (ImageButton)rootView.findViewById(R.id.detect_color_button);
        listView = (ListView)rootView.findViewById(R.id.listview);
        //블루투스 활성화
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!btAdapter.isEnabled()){
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, REQUEST_BT);
        }
        //페어링 장치 표시 (리스트뷰에 페어링 된 기기 뿌려주기
        btArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        deviceAddressArray = new ArrayList<>();
        listView.setAdapter(btArrayAdapter);

        listView.setOnItemClickListener(new myOnItemClickListener());
        //페어링 된 목록 뿌리기
        btnParied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPaired();
            }
        });
        //전원버튼
        btnPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onColorPower();
            }
        });
        //색상 설정
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });
        btnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDetectColor();
            }
        });
        return rootView;
    }

    // 브로드캐스트 수신기 - 검색된 기기의 정보를 가져오기
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //검색을 통한 블루투스 장치 가져오기
                //인텐트에서 객체 정보 불러오기
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC 주소
                btArrayAdapter.add(deviceName);
                deviceAddressArray.add(deviceHardwareAddress);
                btArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        // ACTION_FOUND 수신기 등록 취소
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    public void onPaired(){
        btArrayAdapter.clear();
        if(deviceAddressArray!=null && !deviceAddressArray.isEmpty()){ deviceAddressArray.clear();}
        //getBondedDevices를 통해서 페어링된 목록 가져오기
        pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC주소 불러오기
                //디바이스 이름은 btArrayAdapter에, 디바이스주소는 deviceAddressArray에
                //기기를 검색하고 페어링 된 기기 목록을 불러올 때 btArrayAdapter, DevicesAddressArray를 같이 쓰기에
                //버튼을 누를 때 마다 Array에 들어있던 값들을 모두 clear
                btArrayAdapter.add(deviceName);
                deviceAddressArray.add(deviceHardwareAddress);
            }
        }
    }
    // 전워버튼
    public void onColorPower(){
        if(connectedThread!=null){
            connectedThread.write("PowerOnx");
        }
    }
    // 자동모드
    public void onAutoColor(){
        if(connectedThread!=null){
            connectedThread.write("AutoOnx");
        }
    }

    // 색상감지
    public void onDetectColor(){
        if(connectedThread!=null){
            connectedThread.write("DetectOnx");
        }
    }

    public class myOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getContext(), btArrayAdapter.getItem(position), Toast.LENGTH_SHORT).show();

            Status.setText("try...");

            final String name = btArrayAdapter.getItem(position); // get name
            final String address = deviceAddressArray.get(position); // get address
            boolean flag = true;

            BluetoothDevice device = btAdapter.getRemoteDevice(address);

            // create & connect socket
            try {
                btSocket = createBluetoothSocket(device);
                btSocket.connect();
            } catch (IOException e) {
                flag = false;
                Status.setText("연결에 실패하였습니다.");
                btnParied.setImageResource(R.drawable.b_none);
                e.printStackTrace();
            }

            if(flag){
                Status.setText("연결되었습니다. / 이름 : "+name);
                connectedThread = new ConnectedThread(btSocket);
                connectedThread.start();
                btnParied.setImageResource(R.drawable.b_connected);
            }
        }
    }

    public void openColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(getActivity());  // ColorPicker 객체 생성
        ArrayList<String> colors = new ArrayList<>();  // Color 넣어줄 list

        colors.add("#f00000"); //0
        colors.add("#f07e00"); //1
        colors.add("#ffeb00"); //2
        colors.add("#40d000"); //3
        colors.add("#014bfe"); //4
        colors.add("#9c09ee"); //5
        colors.add("#d0a160"); //6
        colors.add("#fd2370"); //7
        colors.add("#fd9970"); //8
        colors.add("#2f6106"); //9
        colors.add("#613e06"); //10
        colors.add("#ae1f52"); //11
        colors.add("#0d1f52"); //12
        colors.add("#1edcc9"); //13
        colors.add("#5c2d69"); //14

        colorPicker.setColors(colors)  // 만들어둔 list 적용
                .setColumns(5)  // 5열로 설정
                .setRoundColorButton(true)  // 원형 버튼으로 설정
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {

                        //layout.setBackgroundColor(color);
                        //색상 선택 후 확인 시
                        //if(connectedThread!=null){
                        //;
                        Log.i(TAG, "ConvertHexToRGB = "+convertHexToRGB("0xffab91"));
                        Log.i(TAG, "ArrayList color = "+colors.get(0).toString()+"/ color data = "+color+"/ position data = "+position);
                        callByRGB(position);
                        //}
                    }
                    //RGB
                    @Override
                    public void onCancel() {
                        // Cancel 버튼 클릭 시 이벤트
                    }
                }).show();  // dialog 생성
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BT_MODULE_UUID);
        } catch (Exception e) {
            Log.e(TAG, "RFCOM 연결을 만들 수 없습니다.",e);
        }
        return  device.createRfcommSocketToServiceRecord(BT_MODULE_UUID);
    }

    public String convertHexToRGB(String hexForRGBConversion) {
        String rgbValue = "";
        Pattern hexPattern = Pattern.compile(hexaDecimalPattern);
        Matcher hexMatcher = hexPattern.matcher(hexForRGBConversion);

        if (hexMatcher.find()) {
            int hexInt = Integer.valueOf(hexForRGBConversion.substring(2), 16)
                    .intValue();

            int r = (hexInt & 0xFF0000) >> 16;
            int g = (hexInt & 0xFF00) >> 8;
            int b = (hexInt & 0xFF);

            rgbValue = r + "," + g + "," + b+"x";

        } else {
            Log.e(TAG, "올바르지 않거나 HEX값이 입력되지 않았습니다. ");
        }
        System.out.println();
        return rgbValue;
    }

    public void callByRGB(int position){
        if(position == 0){
            ndata = convertHexToRGB("0xf00000");
            connectedThread.write(ndata);
         }
        if(position == 1){
            ndata = convertHexToRGB("0xf07e00");
            connectedThread.write(ndata);
        }
        if(position == 2){
            ndata = convertHexToRGB("0xffeb00");
            connectedThread.write(ndata);
        }
        if(position == 3){
            ndata = convertHexToRGB("0x40d000");
            connectedThread.write(ndata);
        }
        if(position == 4){
            ndata = convertHexToRGB("0x014bfe");
            connectedThread.write(ndata);
        }
        if(position == 5){
            ndata = convertHexToRGB("0x9c09ee");
            connectedThread.write(ndata);
        }
        if(position == 6){
            ndata = convertHexToRGB("0xd0a160");
            connectedThread.write(ndata);
        }
        if(position == 7){
            ndata = convertHexToRGB("0xfd2370");
            connectedThread.write(ndata);
        }
        if(position == 8){
            ndata = convertHexToRGB("0xfd9970");
            connectedThread.write(ndata);
        }
        if(position == 9){
            ndata = convertHexToRGB("0x2f6106");
            connectedThread.write(ndata);
        }
        if(position == 10){
            ndata = convertHexToRGB("0x613e06");
            connectedThread.write(ndata);
        }
        if(position == 11){
            ndata = convertHexToRGB("0xae1f52");
            connectedThread.write(ndata);
        }
        if(position == 12){
            ndata = convertHexToRGB("0x0d1f52");
            connectedThread.write(ndata);
        }
        if(position == 13){
            ndata = convertHexToRGB("0x1edcc9");
            connectedThread.write(ndata);
        }
        if(position == 14){
            ndata = convertHexToRGB("0x5c2d69");
            connectedThread.write(ndata);
        }
    }

    //블루투스 기기 검색
        /*bthSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btAdapter.isDiscovering()){
                    btAdapter.cancelDiscovery();
                } else {
                    if (btAdapter.isEnabled()) {
                        //주변 기기 검색
                        btAdapter.startDiscovery();
                        btArrayAdapter.clear();
                        if (deviceAddressArray != null && !deviceAddressArray.isEmpty()) {
                            deviceAddressArray.clear();
                        }
                        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,filter);
                    } else {
                        Toast.makeText(getContext(), "블루투스가 비활성화 되어있습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        */
}
