package com.naymushin.videofonmv;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.naymushin.videofonmv.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final long SCAN_PERIOD = 5000;
    private static final int REQUEST_ENABLE_BT = 1;

    private ActivityMainBinding mBinding;//ok

    private boolean mScanning;
    private Handler mHandler;

    public Map<String, BluetoothDevice> mScanResults;//ok

    private BluetoothAdapter mBluetoothAdapter;
    private LeDeviceListAdapter mLeDeviceListAdapter;

    // оставить, отредактировано
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //ok
        setContentView(R.layout.activity_main); //ok

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE); //ok
        mBluetoothAdapter = bluetoothManager.getAdapter(); //ok

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main); //ok

        mBinding.startScanningButton.setOnClickListener(v -> startScan()); //ok
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
            return;
        }

        mLeDeviceListAdapter = new LeDeviceListAdapter();


        mScanResults = new HashMap<>();//ok

        mBluetoothAdapter.startLeScan(mLeScanCallback);

        mHandler = new Handler();//ok
        mHandler.postDelayed(this::stopScan, SCAN_PERIOD);//ok

        mScanning = true;//ok
    }

    // оставить, отредактировано
    private void stopScan() {

        if (mScanning && mBluetoothAdapter != null && mBluetoothAdapter.isEnabled() && mBluetoothAdapter != null) {

            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            scanComplete();
        }

        mLeScanCallback = null;
        mScanning = false;
        mHandler = null;

    }

    // оставить, отредактировано
    private void scanComplete() {

        if (mScanResults.isEmpty()) {
            return;
        }

        EditText textField = findViewById(R.id.text_field);
        Map<String,String> devices = new HashMap<>();//key->name & value->address

        for (String deviceAddress : mScanResults.keySet()) {
            BluetoothDevice device = mScanResults.get(deviceAddress);//ok
            devices.put(device.getName(), device.getAddress());//ok
        }


        for(String key : devices.keySet()){

            textField.append(key + " " + devices.get(key));
        }


    }

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

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {

        public LeDeviceListAdapter() {
            super();
            mScanResults = new HashMap<>();
        }

        public void addDevice(BluetoothDevice device) {
            if(!mScanResults.containsKey(device.getName())) {

                mScanResults.put(device.getName(), device);
            }
        }

        @Override
        public int getCount() {
            return mScanResults.size();
        }

        @Override
        public Object getItem(int i) {
            return mScanResults.get(0);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            return view;
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLeDeviceListAdapter.addDevice(device);
                        }
                    });
                }
            };
}