package giava.menmath.ditloid;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by tizianomenichelli on 02/02/16.
 */

public class Challenge extends AppCompatActivity {

    private ProgressDialog waitingDialog;

    /**
     * Thread implements Client side. It creates a socket to comunicate with another player
     */
    BClient client;

    /**
     * Thread implements Server side. It waits for a connection request.
     */
    BServer server;

    /**
     * List view of available devices inner dialog
     */
    private ListView myListView;

    /**
     * contains string with device names and Mac addresses
     */
    private ArrayAdapter<String> BTArrayAdapter;

    /**
     * Broadcast receiver that provides services to
     */
    private MyBroadcastReceiver mReceiver;

    /**
     * Socket through the Application can communicate with other connected device
     */
    private BluetoothSocket mSocket;

    /* Class provides bluetooth service */
    private BluetoothAdapter mBluetoothAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }

        mReceiver = new MyBroadcastReceiver();

        showServerOrClientDialog();


    }

    public void playLikeClient(BluetoothSocket socket){
        //TODO
    }

    public void playLikeServer(BluetoothSocket socket){
        waitingDialog.dismiss();
        //TODO
    }

    /**
     * Class provides a Listener for a ListView. The system shows a dialog with a inner ListView
     * that contains available devices (name and address) for bluetooth connection. The user can
     * choose one of them and the Listener create an instance of the class BluetoothDevice
     * through the MAC address. Then Client thread is started.
     */
    public class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String s = BTArrayAdapter.getItem(position);
            String[] parts = s.split("\\s*\\r?\\n\\s*");
            String address = parts[1];
            System.out.println(address);

            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

            client = new BClient(device, mBluetoothAdapter, Challenge.this);
            client.start();
        }

    }

    /**
     * Class provides method to receive information from devices nearby.
     * The process is asynchronous and it usually involves an inquiry scan of about 12 seconds
     */
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    }

    /**
     * Show Dialog allowing user to see available devices for bluetooth connection.
     * If no devices are available a alert dialog is shown.
     */
    public void showDeviceListDialog() {

        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View viewLayout = inflater.inflate(R.layout.bt_dialog, (ViewGroup) findViewById(R.id.bt_list));

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        mBluetoothAdapter.startDiscovery();

        popDialog.setTitle("Discovered Bluetooth Devices");
        popDialog.setView(viewLayout);

        // create the arrayAdapter that contains the BTDevices, and set it to a ListView
        myListView = (ListView) viewLayout.findViewById(R.id.BTList);
        myListView.setOnItemClickListener(new MyOnItemClickListener());

        BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        myListView.setAdapter(BTArrayAdapter);


        // Button Cancel
        popDialog.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }

                });

        popDialog.create();
        popDialog.show();

    }

    /**
     * The system shows a dialog to the user. He can choose if wait for a friend's request
     * to play or search available devices in the place.
     */
    private void showServerOrClientDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("do you want Wait for friend's request or Search neighbors device?")
                .setCancelable(false)
                .setPositiveButton("Wait", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        server = new BServer(mBluetoothAdapter, Challenge.this);
                        server.start();

                        if (mBluetoothAdapter.getScanMode()
                                != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
                            Toast.makeText(Challenge.this, "your device is not discoverable!",
                                    Toast.LENGTH_SHORT).show();

                        //Waiting for a connection request
                        waitingDialog = ProgressDialog.show(Challenge.this, "",
                                "Please wait...", true);

                        dialog.dismiss();
                    }
                });

        builder.setNegativeButton("Search", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                showDeviceListDialog();

                dialog.dismiss();

            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mBluetoothAdapter != null)
                mBluetoothAdapter.cancelDiscovery();

            unregisterReceiver(mReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


}

