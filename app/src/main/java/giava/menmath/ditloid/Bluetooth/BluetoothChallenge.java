package giava.menmath.ditloid.Bluetooth;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

import giava.menmath.ditloid.DatabaseAccess;
import giava.menmath.ditloid.Ditloid;
import giava.menmath.ditloid.MyApplication;
import giava.menmath.ditloid.R;

/**
 * @author Emanuele Vannacci , Tiziano Menichelli , Simone Mattogno , Gianluca Giallatini;
 * @see BluetoothService
 * @see DeviceList
 * @see ChallengeState
 * <p/>
 * The Class provides all work to manage Bluetooth connection between the two users who
 * want compete.
 * It manage the UI updating it and syncronyzes the two application through message
 * exchange. To perform this responsability it make use of the service of BluetoothService
 * class.
 */
public class BluetoothChallenge extends AppCompatActivity {

    private String state = ChallengeState.INIT;

    // An user can have role of Client or Server in Bluetooth connection
    private String role;


    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Layout Views
    private TextView tvEnigma;
    private TextView tvCategoria;
    private TextView tvTimer;
    private EditText etInput;
    private Button btnCheck;

    private CountDownTimer timer;

    private ProgressDialog waitingDialog;

    private Ditloid ditloid;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;


    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothService mService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_challenge);

        tvEnigma = (TextView) findViewById(R.id.tvDitloid);
        tvCategoria = (TextView) findViewById(R.id.tvCategory);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        etInput = (EditText) findViewById(R.id.etSolution);
        btnCheck = (Button) findViewById(R.id.btnCheck);


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(BluetoothChallenge.this, "Bluetooth is not available",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        WaitOrSearch();
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mService == null) {
            setup();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            mService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                mService.start();
            }
        }
    }

    private void setup() {

        // Initialize the send button with a listener that for click events
        btnCheck.setOnClickListener(new MyCheckButtonListener());

        // Initialize the BluetoothChatService to perform bluetooth connections
        mService = new BluetoothService(BluetoothChallenge.this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");

    }

    protected class MyCheckButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (guess() && state.equals(ChallengeState.GAMING)) {

                state = ChallengeState.WAIT;

                v.setClickable(false);
                timer.cancel();
                sendMessage(Post.GUESSED);
                waitingDialog();
            }

            Toast.makeText(BluetoothChallenge.this, R.string.retry, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Check if solution typed by User is the correct solution for the ditloid.
     *
     * @return boolean : true etInput contains the correct solution for the ditloid.
     */
    private boolean guess() {
        if (ditloid != null)
            if (!etInput.getText().toString().equals(""))
                if (ditloid.getEnigma().equals(etInput.getText().toString()))
                    return true;


        return false;
    }

    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        Activity activity = BluetoothChallenge.this;

        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        Activity activity = BluetoothChallenge.this;

        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    /**
     * Makes this device discoverable.
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }


    private void play(String msg) {

        int id;

        switch (role) {
            case Constants.CLIENT:
                switch (state) {

                    case ChallengeState.INIT:
                        waitingDialog();

                        //Init instance of random class
                        long millis = System.currentTimeMillis();
                        Random rand = new Random(millis);

                        DatabaseAccess dbAccess = DatabaseAccess.getInstance(MyApplication.getAppContext());
                        dbAccess.open();
                        int dbSize = dbAccess.getCount();

                        id = rand.nextInt(dbSize);
                        ditloid = DatabaseAccess.getInstance(MyApplication.getAppContext())
                                .getById(id);

                        tvEnigma.setText(ditloid.getEnigma());
                        tvCategoria.setText(ditloid.getCategory());
                        etInput.setText(ditloid.getEnigma());

                        sendMessage(Integer.toString(id));

                        state = ChallengeState.ID_SENT;

                        break;

                    case ChallengeState.ID_SENT:
                        if (msg.equals(Post.START)) {
                            state = ChallengeState.GAMING;
                            game();
                        }
                        break;

                    case ChallengeState.WAIT:
                        waitingDialog.dismiss();

                        if (msg.equals(Post.GUESSED)) {
                            state = ChallengeState.BALANCE;
                            Toast.makeText(BluetoothChallenge.this, R.string.balance,
                                    Toast.LENGTH_SHORT).show();
                            //TODO action if balance

                        } else if (msg.equals(Post.TIMEOUT)) {
                            state = ChallengeState.WIN;
                            Toast.makeText(BluetoothChallenge.this, R.string.Win,
                                    Toast.LENGTH_SHORT).show();
                            //TODO action if i win
                        }

                        break;

                    case ChallengeState.TIMEOUT:

                        if (msg.equals(Post.TIMEOUT)) {
                            state = ChallengeState.BALANCE;
                            Toast.makeText(BluetoothChallenge.this, R.string.balance,
                                    Toast.LENGTH_SHORT).show();
                            //TODO action if balance

                        } else if (msg.equals(Post.GUESSED)) {
                            //TODO action if i lose
                            state = ChallengeState.LOST;
                            Toast.makeText(BluetoothChallenge.this, R.string.loss,
                                    Toast.LENGTH_SHORT).show();
                        }

                        break;
                }
                break;

            case (Constants.SERVER):

                switch (state) {

                    case ChallengeState.INIT:

                        if (msg != null) {
                            id = Integer.valueOf(msg);

                            Ditloid ditloid = DatabaseAccess.getInstance(MyApplication.getAppContext())
                                    .getById(id);

                            tvEnigma.setText(ditloid.getEnigma());
                            tvCategoria.setText(ditloid.getCategory());
                            etInput.setText(ditloid.getEnigma());

                            sendMessage(Post.START);

                            state = ChallengeState.GAMING;
                            waitingDialog.dismiss();
                            game();
                        }
                        break;

                    case ChallengeState.WAIT:
                        if (msg.equals(Post.TIMEOUT)) {
                            state = ChallengeState.WIN;
                            Toast.makeText(BluetoothChallenge.this, R.string.Win,
                                    Toast.LENGTH_SHORT).show();
                            //TODO action if i win
                        } else if (msg.equals(Post.GUESSED)) {
                            state = ChallengeState.BALANCE;
                            Toast.makeText(BluetoothChallenge.this, R.string.balance,
                                    Toast.LENGTH_SHORT).show();
                            //TODO action if balance
                        }

                        break;
                    case ChallengeState.TIMEOUT:
                        if (msg.equals(Post.TIMEOUT)) {
                            state = ChallengeState.BALANCE;
                            Toast.makeText(BluetoothChallenge.this, R.string.balance,
                                    Toast.LENGTH_SHORT).show();
                            //TODO action if Balance
                        } else if (msg.equals(Post.TIMEOUT)) {
                            state = ChallengeState.LOST;
                            Toast.makeText(BluetoothChallenge.this, R.string.loss,
                                    Toast.LENGTH_SHORT).show();
                            //TODO action if i lose
                        }

                }
                break;
        }
    }

    public void game() {

        waitingDialog.dismiss();
        btnCheck.setClickable(true);

        timer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                btnCheck.setClickable(false);
                if (state.equals(ChallengeState.GAMING)) {
                    sendMessage(Post.TIMEOUT);
                    state = ChallengeState.TIMEOUT;
                }
            }
        }.start();
    }


    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {

        try {
            // Check that we're actually connected before trying anything
            if (mService.getState() != BluetoothService.STATE_CONNECTED)
                throw new IOException();


            // Check that there's actually something to send
            if (message.length() > 0) {
                // Get the message bytes and tell the BluetoothChatService to write
                byte[] send = message.getBytes();
                mService.write(send);

                // Reset out string buffer to zero
                mOutStringBuffer.setLength(0);

            }

        } catch (IOException e) {
            Toast.makeText(BluetoothChallenge.this, "send Message error",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    /**
     * Establish connection with other divice
     *
     * @param data An {@link Intent} with {@link DeviceList#EXTRA_DEVICE_ADDRESS} extra.
     */
    private void connectDevice(Intent data) {
        // Get the device MAC address
        String address = data.getExtras().getString(DeviceList.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mService.connect(device);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                // data contains the MAC address of device
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data);
                }

                play(null);

                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setup();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Toast.makeText(BluetoothChallenge.this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }


    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    @SuppressWarnings("all")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                        default:
                            break;
                    }
                    break;

                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    play(readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    Toast.makeText(BluetoothChallenge.this, "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;

                case Constants.MESSAGE_TOAST:
                    Toast.makeText(BluetoothChallenge.this, msg.getData().getString(Constants.TOAST),
                            Toast.LENGTH_SHORT).show();
                    finish();
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * The system shows a dialog to the user. He can choose if wait for a friend's request
     * to play or search available devices in the place.
     */
    private void WaitOrSearch() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("do you want Wait for friend's request or Search neighbors device?")
                .setCancelable(false)
                .setPositiveButton("Wait", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        role = Constants.SERVER;

                        ensureDiscoverable();
                        if (mService.getState() != BluetoothService.STATE_LISTEN)
                            mService.start();

                        //show progress dialog
                        waitingDialog();
                    }
                });

        builder.setNegativeButton("Search", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                role = Constants.CLIENT;

                Intent serverIntent = new Intent(BluetoothChallenge.this, DeviceList.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                dialog.dismiss();

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Show in the UI a Progress dialog
     */
    private void waitingDialog() {
        //Waiting for a connection request
        waitingDialog = new ProgressDialog(BluetoothChallenge.this);
        waitingDialog.setMessage("Wait connection!");
        waitingDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        waitingDialog.show();
    }

   /* private void ConnectionProblemDialog() {

        new AlertDialog.Builder(MyApplication.getAppContext())
                .setTitle("Connession problem")
                .setMessage("your friend is no longer available")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
*/

}
