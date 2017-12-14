package com.naymushin.videofonmv;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.naymushin.videofonmv.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.naymushin.videofonmv.Constants.SCAN_PERIOD;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;

    private ActivityMainBinding mBinding;//ok

    private boolean mScanning;
    private Handler mHandler;
    private Map<String, BluetoothDevice> mScanResults;//ok
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private ScanCallback mScanCallback;

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