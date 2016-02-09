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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

/**
 * Created by tizianomenichelli on 02/02/16.
 */

public class Challenge extends AppCompatActivity {


    /**
     * Dialog that the server user sees before get client connection.
     */
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

    /* Class provides bluetooth service */
    private BluetoothAdapter mBluetoothAdapter;

    /**
     * View element inner layout
     */
    private TextView tvDitloid;

    private TextView tvCategory;

    private Button btCheck;

    private EditText etSolution;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        btCheck = (Button) findViewById(R.id.btnCheck);
        tvDitloid = (TextView) findViewById(R.id.tvDitloid);
        tvCategory = (TextView) findViewById(R.id.tvCategory);
        etSolution = (EditText) findViewById(R.id.etSolution);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }

        mReceiver = new MyBroadcastReceiver();

        showServerOrClientDialog();


    }

    public void playLikeClient(BluetoothSocket socket) {

        try {
            InputStream istream = socket.getInputStream();
            OutputStream ostream = socket.getOutputStream();

            ConnectionManager cmanager = new ConnectionManager();
            cmanager.setInputStream(istream);
            cmanager.setOutputStream(ostream);

            cmanager.setBtnCheck(btCheck);
            cmanager.setEtSoluction(etSolution);
            cmanager.setTvEnigma(tvDitloid);
            cmanager.setTvCategoria(tvCategory);

            cmanager.execute("Client", null, null);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(Challenge.this, "Unable to connect", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    public void playLikeServer(BluetoothSocket socket) {
        waitingDialog.dismiss();
        //TODO
    }

    /**
     * Class provides a Listener for a ListView. The system shows a dialog with a inner ListView
     * that contains available devices (name and address) for bluetooth connection. The user can
     * choose one of them and the Listener create an instance of the class BluetoothDevice
     * through the MAC address. Then Client thread is started.
     */
    public class MyOnItemClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            String s = BTArrayAdapter.getItem(which);

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

//        AlertDialog.Builder popDialog = new AlertDialog.Builder(Challenge.this);
//        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
//        View viewLayout = inflater.inflate(R.layout.bt_dialog, (ViewGroup) findViewById(R.id.bt_list));
//
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(mReceiver, filter);
//        mBluetoothAdapter.startDiscovery();
//
//        popDialog.setTitle("Discovered Bluetooth Devices");
//        popDialog.setView(viewLayout);
//
//        // create the arrayAdapter that contains the BTDevices, and set it to a ListView
//        myListView = (ListView) viewLayout.findViewById(R.id.BTList);
//        myListView.setOnItemClickListener(new MyOnItemClickListener());
//
//        BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
//        myListView.setAdapter(BTArrayAdapter);
//
//
//        // Button Cancel
//        popDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                finish();
//            }
//        });
//
//        popDialog.create();
//        popDialog.show();

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Challenge.this);
        builderSingle.setTitle("Discovered Bluetooth Devices");

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        mBluetoothAdapter.startDiscovery();

        BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

        builderSingle.setAdapter(BTArrayAdapter, new MyOnItemClickListener());

        builderSingle.show();
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

                        dialog.dismiss();

                        //Waiting for a connection request
                        waitingDialog = new ProgressDialog(Challenge.this);
                        waitingDialog.setMessage("Wait a request froma friend!");
                        waitingDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                                waitingDialog.dismiss();
                                finish();
                                server.cancel();
                            }
                        });
                        waitingDialog.show();
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

