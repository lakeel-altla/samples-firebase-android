package com.example.firebase.custom.authentication.data;

public final class Beacon {

    public String beaconName;

    public int price;

    @Override
    public String toString() {
        return "beaconName:" + beaconName + " price:" + price;
    }
}
