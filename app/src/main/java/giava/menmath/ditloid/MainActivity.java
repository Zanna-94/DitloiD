package giava.menmath.ditloid;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnPlay, btnChallenge, btnTrophies, btnRules, btnHelpUs;

    /**
     * Int value that provides request code for Bluetooth activation intent
     */
    private static final int REQUEST_ENABLE_BT = 1;

    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnChallenge = (Button) findViewById(R.id.btnChallenge);
        btnTrophies = (Button) findViewById(R.id.btnTrophies);
        btnRules = (Button) findViewById(R.id.btnRules);
        btnHelpUs = (Button) findViewById(R.id.btnHelpUs);

        MyClick mc = new MyClick();

        btnPlay.setOnClickListener(mc);
        btnChallenge.setOnClickListener(mc);
        btnTrophies.setOnClickListener(mc);
        btnRules.setOnClickListener(mc);
        btnHelpUs.setOnClickListener(mc);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // messaggio.startAnimation(bounce);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnPlay) {
                Intent startGame = new Intent(MainActivity.this, Game.class);
                startActivity(startGame);
            }
            if (v.getId() == R.id.btnChallenge) {

               /* it makes the local device  discoverable to other devices and active
                bluetooth if it isn't actived*/
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivityForResult(discoverableIntent, REQUEST_ENABLE_BT);

//                See onActivityResult

            }
            if (v.getId() == R.id.btnTrophies) {
                Intent Trophies = new Intent(MainActivity.this, Trophies.class);
                startActivity(Trophies);
            }
            if (v.getId() == R.id.btnRules) {
                Intent readRules = new Intent(MainActivity.this, Rules.class);
                startActivity(readRules);
            }
            if (v.getId() == R.id.btnHelpUs) {
                Intent Help = new Intent(MainActivity.this, HelpUs.class);
                startActivity(Help);
            }
        }
    }

    /**
     * Methos check out bluetooth connection. If the device don't support bluetooth
     * it shows an alert dialog.
     */
    private void CheckBluetoothSupport() {
        if (mBluetoothAdapter == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Bluetooth no supported")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT)
//            if (resultCode ==  RESULT_OK)
                if (mBluetoothAdapter.isEnabled()) {
                    Intent startChallenge = new Intent(MainActivity.this, Challenge.class);
                    startActivity(startChallenge);
                }

    }
}
