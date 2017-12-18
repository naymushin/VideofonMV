package com.naymushin.videofonmv;

/**
 * Created by 1 on 18.12.2017.
 */

public class BtDevice {

    private String name;
    private String address;
    private String rssi;

    private static int counter = 1;

    public BtDevice(String name, String address, String rssi) {

        if(name == null) {

            this.name = "Device".concat(String.valueOf(counter));
            counter++;
        } else {

            this.name = name;
        }

        this.address = address;

        if(rssi == null)
            this.rssi = "unknown";
        else
            this.rssi = rssi;

    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getRssi() {
        return rssi;
    }
}