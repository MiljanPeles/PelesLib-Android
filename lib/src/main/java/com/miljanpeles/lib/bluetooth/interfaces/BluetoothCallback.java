package com.miljanpeles.lib.bluetooth.interfaces;

public interface BluetoothCallback {
    void onBluetoothTurningOn();
    void onBluetoothOn();
    void onBluetoothTurningOff();
    void onBluetoothOff();
    void onUserDeniedActivation();
}