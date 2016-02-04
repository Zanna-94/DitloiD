package giava.menmath.ditloid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * @Author Emanuele Vannacci , Tiziano Menichelli , Simone Mattogno , Gianluca Giallatini
 *
 * @see Challenge
 * @see BClient
 *
 * Class provides the server side of bluetooth connection.
 * It creates a BloetoothServerSocket that listen all possible connection request.
 * The first request that arrives is accepted and a Socket is created.
 * Through the Socket the application can comunicate with the client side.
 */

class BServer extends Thread {

    BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
    final String deviceName = myDevice.getName();

    final UUID MY_UUID = UUID.fromString("e627cb94-ca85-11e5-9956-625662870761");

    private  BluetoothServerSocket mmServerSocket;

    private BluetoothAdapter mBluetoothAdapter;

    /**
     * Default Costructor
     * @param mBluetoothAdapter
     */
    public BServer(BluetoothAdapter mBluetoothAdapter) {

        this.mBluetoothAdapter = mBluetoothAdapter;

    }

    public void run() {
        BluetoothSocket socket = null;

        //wait
        while(!mBluetoothAdapter.isEnabled());

        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(deviceName, MY_UUID);
        } catch (IOException e) {
        }

        mmServerSocket = tmp;

        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
                socket = mmServerSocket.accept();

                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
//                    manageConnectedSocket(socket);
                    mmServerSocket.close();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * Will cancel the listening socket, and cause the thread to finish
     */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
        }
    }
}
