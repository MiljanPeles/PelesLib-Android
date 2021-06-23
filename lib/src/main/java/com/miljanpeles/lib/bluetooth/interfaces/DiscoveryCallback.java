package com.miljanpeles.lib.bluetooth.interfaces;

import android.bluetooth.BluetoothDevice;

public interface DiscoveryCallback{
    void onDiscoveryStarted();
    void onDiscoveryFinished();
    void onDeviceFound(BluetoothDevice device);
    void onDevicePaired(BluetoothDevice device);
    void onDeviceUnpaired(BluetoothDevice device);
    void onError(int errorCode);
}