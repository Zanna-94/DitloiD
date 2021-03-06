package giava.menmath.ditloid.Bluetooth;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

import giava.menmath.ditloid.Database.DatabaseAccess;
import giava.menmath.ditloid.Database.DatabaseAccessFactory;
import giava.menmath.ditloid.Database.TypeDB;
import giava.menmath.ditloid.Ditloid;
import giava.menmath.ditloid.R;
import giava.menmath.ditloid.User.UserDao;
import giava.menmath.ditloid.User.UserInfo;

/**
 * The Class provides all work to manage Bluetooth connection between the two users who
 * want compete.
 * It manage the UI updating it and syncronyzes the two application through message
 * exchange. To perform this responsability it make use of the service of BluetoothService
 * class.
 *
 * @author MenMath.GiaVa
 * @see BluetoothService
 * @see DeviceList
 * @see ChallengeState
 */

public class BluetoothChallenge extends AppCompatActivity {

    private String gameState = ChallengeState.INIT;

    // An user can have role of Client or Server in Bluetooth connection
    private String role;

    // Var to determinate if the other user has solved the ditloid before me.
    private boolean myFriendGuessed = false;
    //Var to determinate if the other user has terminated the time to solve the ditolid
    private boolean myFriendTimeout = false;

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Layout Views
    private TextView tvEnigma;
    private TextView tvCategoria;
    private TextView tvTimer;
    private EditText etInput;
    private Button btnCheck;

    //Provide a timer
    private CountDownTimer timer;


    /**
     * Class contains data for user progress
     * {@link UserInfo}
     */
    private UserInfo userInfo;

    /**
     * Ditloid chosen by device that performs the {@link #role} of Client
     *
     * @see Ditloid
     */
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
     * Local Bluetooth adapter that perform fundamental tasks
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * @see BluetoothService
     */
    private BluetoothService mService = null;

    /**
     * Handler to communicate with {@link BluetoothService} and get messages from it
     */
    private MyHandler mHandler;

    private ProgressDialog waitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_challenge);

        tvEnigma = (TextView) findViewById(R.id.tvDitloid);
        tvCategoria = (TextView) findViewById(R.id.tvCategory);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        etInput = (EditText) findViewById(R.id.etSolution);
        btnCheck = (Button) findViewById(R.id.btnCheck);

        btnCheck.setVisibility(View.INVISIBLE);
        etInput.setVisibility(View.INVISIBLE);

        //keep the screen turned on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(BluetoothChallenge.this, getString(R.string.strBluetoothNoAvailable),
                    Toast.LENGTH_LONG).show();
            finish();
        }

        mHandler = new MyHandler();

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
        } else if (mService == null) {
            setup();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        //Obtain user info to modify credit in case of win
        try {
            userInfo = UserDao.deserializza();

        } catch (IOException e) {
            e.printStackTrace();
            userInfo = UserInfo.getInstance();
        }

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

    /**
     * Initialize the state of the connection and the Bluetooth service class
     */
    private void setup() {

        gameState = ChallengeState.INIT;


        // Initialize the send button with a listener that for click events
        btnCheck.setOnClickListener(new MyCheckButtonListener());

        // Initialize the BluetoothChatService to perform bluetooth connections
        mService = new BluetoothService(mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");

    }

    /**
     * Listener for the Check button that verify if the solution typed by the user
     * is correct. Depending on the state of the challenge in witch the application is,
     * are performed action to change state.
     */
    protected class MyCheckButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (guess() && getGameState().equals(ChallengeState.GAMING)) {

                v.setClickable(false);
                timer.cancel();
                sendMessage(Post.GUESSED);

                if (myFriendGuessed) {
                    setGameState(ChallengeState.BALANCE);
                    Toast.makeText(BluetoothChallenge.this, R.string.balance,
                            Toast.LENGTH_SHORT).show();
                    iDraw();
                } else {
                    setGameState(ChallengeState.WAIT);
                    Toast.makeText(BluetoothChallenge.this, getString(R.string.strCorrectSolution), Toast.LENGTH_SHORT).show();
                    waitingDialog(R.string.strWaitFriend);
                }


            } else
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
                if (ditloid.getSolution().trim().compareToIgnoreCase(etInput.getText().toString().
                        trim()) == 0)
                    return true;

        return false;
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

    /**
     * @param msg: {@link Post}
     */
    private void play(String msg) {

        int id;

        switch (role) {
            case Constants.CLIENT:
                switch (gameState) {

                    case ChallengeState.INIT:

                        if (msg == null) {

                            if (waitingDialog != null)
                                waitingDialog.dismiss();

                            waitingDialog(R.string.strWaitConnection);

                            //Init instance of random class
                            long millis = System.currentTimeMillis();
                            Random rand = new Random(millis);

                            DatabaseAccessFactory factory = new DatabaseAccessFactory();
                            DatabaseAccess dbAccess = factory.getDatabaseAccess(TypeDB.DB_CHALLENGE);
                            dbAccess.open();
                            int dbSize = dbAccess.getCount();

                            id = rand.nextInt(dbSize - 1) + 1; //exclude id=0
                            ditloid = dbAccess.getById(id);
                            dbAccess.close();

                            sendMessage(Integer.toString(id));
                            setGameState(ChallengeState.ID_SENT);
                        }

                        break;

                    case ChallengeState.ID_SENT:
                        if (msg.equals(Post.START)) {

                            tvEnigma.setText(ditloid.getEnigma());
                            tvCategoria.setText(ditloid.getCategory());
                            System.out.println(ditloid.getCategory());

                            etInput.setText(ditloid.getEnigma());

                            btnCheck.setVisibility(View.VISIBLE);
                            btnCheck.setClickable(true);
                            etInput.setVisibility(View.VISIBLE);

                            waitingDialog.dismiss();

                            setGameState(ChallengeState.GAMING);
                            game();
                        }
                        break;

                    case ChallengeState.WAIT:
                        waitingDialog.dismiss();

                        if (msg.equals(Post.GUESSED)) {
                            setGameState(ChallengeState.BALANCE);
                            Toast.makeText(BluetoothChallenge.this, R.string.balance,
                                    Toast.LENGTH_SHORT).show();
                            waitingDialog.dismiss();
                            iDraw();

                        } else if (msg.equals(Post.TIMEOUT)) {
                            setGameState(ChallengeState.WIN);
                            Toast.makeText(BluetoothChallenge.this, R.string.Win,
                                    Toast.LENGTH_SHORT).show();
                            waitingDialog.dismiss();
                            iWin();
                        }

                        break;

                    case ChallengeState.TIMEOUT:

                        if (msg.equals(Post.TIMEOUT)) {
                            setGameState(ChallengeState.BALANCE);
                            Toast.makeText(BluetoothChallenge.this, R.string.balance,
                                    Toast.LENGTH_SHORT).show();
                            iDraw();

                        } else if (msg.equals(Post.GUESSED)) {
                            setGameState(ChallengeState.LOST);
                            Toast.makeText(BluetoothChallenge.this, R.string.loss,
                                    Toast.LENGTH_SHORT).show();
                            iLose();
                        }

                        break;

                    case ChallengeState.GAMING:

                        if (msg.equals(Post.GUESSED)) {
                            myFriendGuessed = true;
                        } else if (msg.equals((Post.TIMEOUT)))
                            myFriendTimeout = true;

                        break;
                }

                break;

            case (Constants.SERVER):

                switch (gameState) {

                    case ChallengeState.INIT:

                        if (msg != null) {

                            id = Integer.valueOf(msg);

                            DatabaseAccessFactory factory = new DatabaseAccessFactory();
                            DatabaseAccess dbAccess = factory.getDatabaseAccess(TypeDB.DB_CHALLENGE);
                            dbAccess.open();
                            ditloid = dbAccess.getById(id);
                            dbAccess.close();

                            tvEnigma.setText(ditloid.getEnigma());
                            tvCategoria.setText(ditloid.getCategory());

                            etInput.setText(ditloid.getEnigma());
                            btnCheck.setVisibility(View.VISIBLE);
                            btnCheck.setClickable(true);
                            etInput.setVisibility(View.VISIBLE);

                            sendMessage(Post.START);
                            setGameState(ChallengeState.GAMING);

                            if (waitingDialog != null)
                                waitingDialog.dismiss();

                            game();
                        }

                        break;

                    case ChallengeState.WAIT:
                        if (msg.equals(Post.TIMEOUT)) {
                            setGameState(ChallengeState.WIN);
                            Toast.makeText(BluetoothChallenge.this, R.string.Win,
                                    Toast.LENGTH_SHORT).show();


                            waitingDialog.dismiss();

                            iWin();

                        } else if (msg.equals(Post.GUESSED)) {
                            setGameState(ChallengeState.BALANCE);
                            Toast.makeText(BluetoothChallenge.this, R.string.balance,
                                    Toast.LENGTH_SHORT).show();
                            waitingDialog.dismiss();
                            iDraw();
                        }

                        break;

                    case ChallengeState.TIMEOUT:
                        if (msg.equals(Post.TIMEOUT)) {
                            setGameState(ChallengeState.BALANCE);
                            Toast.makeText(BluetoothChallenge.this, R.string.balance,
                                    Toast.LENGTH_SHORT).show();
                            iDraw();
                        } else if (msg.equals(Post.TIMEOUT)) {
                            setGameState(ChallengeState.LOST);
                            Toast.makeText(BluetoothChallenge.this, R.string.loss,
                                    Toast.LENGTH_SHORT).show();
                            iLose();
                        }

                        break;

                    case ChallengeState.GAMING:

                        if (msg.equals(Post.GUESSED)) {
                            myFriendGuessed = true;
                        } else if (msg.equals((Post.TIMEOUT)))
                            myFriendTimeout = true;

                        break;

                }
                break;
        }
    }


    /**
     * Action to perform when the Challenge is in the state of {@link ChallengeState#GAMING}.
     * In this method is initialized the timer thorgh the class {@link CountDownTimer}
     */
    public void game() {

        timer = new CountDownTimer(180000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvTimer.setText(String.format(getString(R.string.strSecondsRemaining) + ": %d", millisUntilFinished / 1000));
            }

            public void onFinish() {
                btnCheck.setClickable(false);
                if (getGameState().equals(ChallengeState.GAMING)) {

                    if (myFriendTimeout) {
                        setGameState(ChallengeState.BALANCE);
                        Toast.makeText(BluetoothChallenge.this, R.string.balance,
                                Toast.LENGTH_SHORT).show();
                    } else if (myFriendGuessed) {
                        setGameState(ChallengeState.TIMEOUT);
                        Toast.makeText(BluetoothChallenge.this, R.string.loss,
                                Toast.LENGTH_SHORT).show();
                        iLose();
                    } else
                        setGameState(ChallengeState.TIMEOUT);
                }

                sendMessage(Post.TIMEOUT);
            }
        }.start();
    }

    /**
     * Action to perform in case of defeat
     */
    private void iLose() {

        //reset state for new challenge
        gameState = ChallengeState.INIT;

        //stop timer if not already timeout
        if (timer != null) {
            timer.cancel();
        }

        finish();

    }

    /**
     * Action to perform in case of victory
     */
    private void iWin() {

        //reset state for new challenge
        gameState = ChallengeState.INIT;

        //stop timer if not already timeout
        if (timer != null) {
            timer.cancel();
        }

        userInfo.addCredit(1);
    }

    /**
     * Action to perform in case of balance
     */
    private void iDraw() {

        //reset state for new challenge
        gameState = ChallengeState.INIT;
        myFriendGuessed = false;
        myFriendTimeout = false;

        //stop timer if not already timeout
        if (timer != null) {
            timer.cancel();
        }

        //Dialog to propose another challenge
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.strAfterDraw))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.strYes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        if (role.equals(Constants.CLIENT))
                            play(null);

                    }
                });

        builder.setNegativeButton(getString(R.string.strNo), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                finish();

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Sends a message thought {@link BluetoothService#write(byte[])}
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
            Toast.makeText(BluetoothChallenge.this, getString(R.string.strNoConnection),
                    Toast.LENGTH_SHORT).show();

            mService.stop();
            finish();
        }

    }


    /**
     * Establish connection with other device through {@link BluetoothService}
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

    /**
     * Manage operation after the end of the called activity. After the intent for
     * {@link DeviceList} this method analyzes data returned and call {@link #connectDevice(Intent)}
     * to perform devices connection.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                // data contains the MAC address of device
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data);
                }

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
     * The Handler that gets information back from the {@link BluetoothService}
     * The only {@link Message} that are handler are those that carry information about
     * success connection with another device, data in input through the socket, information on
     * the other connected device. Other Message are ignored.
     */
    @SuppressWarnings("all")
    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:

                            if (role == Constants.CLIENT)
                                play(null);

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
                    Toast.makeText(BluetoothChallenge.this, getString(R.string.strConnectedTo)
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;

                case Constants.MESSAGE_TOAST:
/*                    Toast.makeText(BluetoothChallenge.this, msg.getData().getString(Constants.TOAST),
                            Toast.LENGTH_SHORT).show();*/
                    finish();
                    break;

                default:
                    break;
            }
        }
    }


    /**
     * The system shows a dialog to the user. He can choose if wait for a friend's request
     * to play or search available devices in the place.
     * If the user chooses to wait then the device will be the Server in the bluetooth connection.
     * Instead if the user choose to research available devices, its device will be the Client.
     * Depending on its role, the application performs different operation in the challenge.
     * {@link #play(String)}
     */
    private void WaitOrSearch() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.strRequest))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.strWait), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        ensureDiscoverable();
                        if (mService.getState() != BluetoothService.STATE_LISTEN)
                            mService.start();

                        role = Constants.SERVER;

                        //show progress dialog
                        waitingDialog(R.string.strWaitConnection);
                    }
                });

        builder.setNegativeButton(getString(R.string.strSearch), new DialogInterface.OnClickListener() {
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
     * Show in the UI a progress dialog
     */
    private void waitingDialog(int res) {
        //Waiting for a connection request
        waitingDialog = new ProgressDialog(BluetoothChallenge.this);

        waitingDialog.setMessage(getString(res));
        waitingDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.strCancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        waitingDialog.show();
    }

    @Override
    public void onBackPressed() {
        role = null;

        if (timer != null)
            timer.cancel();
        if (mService != null)
            mService.stop();

        this.finish();
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            UserDao.serializza(userInfo);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(BluetoothChallenge.this, R.string.strImpossibleSave,
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            mService.stop();
        }
        if (timer != null)
            timer.cancel();
        finish();
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }


}
