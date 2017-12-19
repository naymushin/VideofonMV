package com.naymushin.videofonmv;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import java.util.Timer;
import java.util.TimerTask;

//import com.naymushin.videofonmv.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.naymushin.videofonmv.Constants.MY_PERMISSIONS_REQUEST_COARSE_LOCATION;
import static com.naymushin.videofonmv.Constants.MY_PERMISSIONS_REQUEST_FINE_LOCATION;
import static com.naymushin.videofonmv.Constants.SCAN_PERIOD;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private ArrayAdapter<String> adapter;

    ArrayList<String> dataToEnter;
    //private ActivityMainBinding mBinding;//ok

    private boolean mScanning;
    private Handler mHandler;
    private Map<String, BluetoothDevice> mScanResults;//ok
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private ScanCallback mScanCallback;
    private ListView listView;
    //private BroadcastReceiver receiver;

    // оставить, отредактировано
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //ok
        setContentView(R.layout.activity_main); //ok

        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        listView = findViewById(R.id.listView);

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE); //ok
        mBluetoothAdapter = bluetoothManager.getAdapter(); //ok

        //mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main); //ok

        //mBinding.startScanningButton.setOnClickListener(v -> startScan()); //ok


        Button button = (Button) findViewById(R.id.start_scanning_button);

        button.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                startScan();
            }
        });

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_COARSE_LOCATION);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_FINE_LOCATION);
    }

    // оставить, отредактировано
    // проверка наличия BLE на смартфоне
    @Override
    protected void onResume() {
        super.onResume();

        // Check low energy support
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            // Get a newer device
            finish();
        }
    }

    // оставить, отредактировано
    // Scanning
    private void startScan() {

        if (!hasPermissions() || mScanning) {

            Toast toast = Toast.makeText(getApplicationContext(),
                    "First turn on BT!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        mScanResults = new HashMap<>();//ok
        mScanCallback = new BtleScanCallback(mScanResults);//ok

        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();//ok
        mBluetoothLeScanner.startScan(mScanCallback);//ok

        mHandler = new Handler();//ok
        mHandler.postDelayed(this::stopScan, SCAN_PERIOD);//ok

        mScanning = true;//ok
    }

    // оставить, отредактировано
    private void stopScan() {

        /*
        Toast toast = Toast.makeText(getApplicationContext(),
                "stopScan()", Toast.LENGTH_SHORT);
        toast.show();
        */

        if (mScanning && mBluetoothAdapter != null && mBluetoothAdapter.isEnabled() && mBluetoothLeScanner != null) {
            mBluetoothLeScanner.stopScan(mScanCallback);
            scanComplete();
        }

        mScanCallback = null;
        mScanning = false;
        mHandler = null;
    }

    // оставить, отредактировано
    private void scanComplete() {
        if (mScanResults.isEmpty()) {
            return;
        }

        BtDevice btd;

        String btdName;
        String btdAddress;
        String btdRssi;

//        final String[] temp = new String[1];
//
//        receiver = new BroadcastReceiver(){
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                String action = intent.getAction();
//
//                if(BluetoothDevice.ACTION_FOUND.equals(action)) {
//
//                    temp[0] = intent.getStringExtra(BluetoothDevice.EXTRA_RSSI);
//
//                    Toast toast = Toast.makeText(getApplicationContext(),
//                            temp[0], Toast.LENGTH_SHORT);
//                    toast.show();
//                }
//            }
//        };

        // Создаём пустой массив для хранения имен устройств
        ArrayList<BtDevice> devices = new ArrayList<>();

        /*
        for (String deviceAddress : mScanResults.keySet()) {

            btdName = mScanResults.get(deviceAddress).getName();
            btdAddress = mScanResults.get(deviceAddress).getAddress();
            btdRssi = temp[0];


            btd = new BtDevice(btdName, btdAddress, btdRssi);

            devices.add(btd);
        }
        */

        dataToEnter = new ArrayList(); // final
        /*
        String s;

        for (BtDevice b : devices) {

            s = b.getName() + "\n" + b.getAddress() + " " + b.getRssi();
            dataToEnter.add(s);
        }
        */
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, dataToEnter);

        listView.setAdapter(adapter);

        //
        mBluetoothAdapter.startDiscovery();
        //

        Toast toast = Toast.makeText(getApplicationContext(),
                "startdiscovery", Toast.LENGTH_SHORT);
        toast.show();

        //adapter.notifyDataSetChanged();

//        ArrayAdapter<BtDevice> adapter = new ArrayAdapter<BtDevice>(this, android.R.layout.simple_list_item_2, android.R.id.text1, devices) {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                TextView text1 = view.findViewById(android.R.id.text1);
//                TextView text2 = view.findViewById(android.R.id.text2);
//
//                text1.setText(devices.get(position).getName());
//                text2.setText(devices.get(position).getAddress() +
//                        " signal: " + devices.get(position).getRssi());
//                return view;
//            }
//        };
//
//        listView.setAdapter(adapter);

        /*
//        for(BtDevice b : devices) {
//
//
//            Toast toast = Toast.makeText(getApplicationContext(),
//                    b.getRssi(), Toast.LENGTH_SHORT);
//            toast.show();
//
//        }


        MyAdapter adapter = new ArrayAdapter<BtDevice>(this,
                android.R.layout.simple_list_item_2, devices);

        // Привяжем массив через адаптер к ListView
        listView.setAdapter(adapter);
        setListAdapter(new MyAdapter(this, devices));


        listView.setAdapter(adapter);

        /*
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, devices) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                text1.setText(devices.get(position).getName());
                text2.setText(devices.get(position).getAddress() +
                        " signal: " + devices.get(position).getRssi());
                return view;
            }
        };
        */
        //listView.setAdapter(adapter);

        /*
        SimpleAdapter adapter = new SimpleAdapter(this, devices,
                R.layout.activity_main,
                new String[]{"MemberID", "Name", "Tel"},
                new int[]{R.id.text_view_member_id, R.id.text_view_name,
                        R.id.text_view_phone});

        listView.setAdapter(adapter);
        */

    }

    private final BroadcastReceiver receiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                dataToEnter.add("Name: " + device.getName() + "\n" +"Address: "+ device.getAddress() + "\n" +"rssi: "+ rssi +" dBm");
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"  RSSI: " + rssi + "dBm", Toast.LENGTH_SHORT).show();
            }
        }
    };

    // оставить, отредактировано
    private boolean hasPermissions() {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            requestBluetoothEnable();
            return false;
        }
        return true;
    }

    // оставить, отредактировано
    private void requestBluetoothEnable() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    // оставить, отредактировано
    private class BtleScanCallback extends ScanCallback {

        private Map<String, BluetoothDevice> mScanResults;

        BtleScanCallback(Map<String, BluetoothDevice> scanResults) {
            mScanResults = scanResults;
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            addScanResult(result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult result : results) {
                addScanResult(result);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
        }

        private void addScanResult(ScanResult result) {
            BluetoothDevice device = result.getDevice();
            String deviceAddress = device.getAddress();
            mScanResults.put(deviceAddress, device);
        }
    }
}