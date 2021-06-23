package com.miljanpeles.lib.bluetooth.interfaces;

import android.bluetooth.BluetoothDevice;

public interface DeviceCallback {
    void onDeviceConnected(BluetoothDevice device);
    void onDeviceDisconnected(BluetoothDevice device, String message);
    void onMessage(byte[] message);
    void onError(int errorCode);
    void onConnectError(BluetoothDevice device, String message);
}