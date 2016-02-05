package giava.menmath.ditloid;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.UUID;

import java.util.Set;

/**
 * @Author Emanuele Vannacci , Tiziano Menichelli , Simone Mattogno , Gianluca Giallatini
 *
 * @see Challenge
 * @see BServer
 */

class BClient extends Thread {

    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;

    private BluetoothAdapter mBluetoothAdapter;

    final UUID MY_UUID = UUID.fromString("e627cb94-ca85-11e5-9956-625662870761");

    Challenge father;

    public BClient(BluetoothDevice device, BluetoothAdapter mBluetoothAdapter, Challenge father) {

        this.father = father;
        this.mBluetoothAdapter= mBluetoothAdapter;

        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) { }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }

        father.playLikeClient(mmSocket);
    }

    public BluetoothSocket getSocket(){
        return mmSocket;
    }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}