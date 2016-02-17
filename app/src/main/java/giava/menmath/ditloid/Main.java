package giava.menmath.ditloid;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import giava.menmath.ditloid.Bluetooth.BluetoothChallenge;

public class Main extends AppCompatActivity {

    private Button btnPlay, btnChallenge, btnRules, btnHelpUs;

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
        btnRules = (Button) findViewById(R.id.btnRules);
        btnHelpUs = (Button) findViewById(R.id.btnHelpUs);

        MyClick mc = new MyClick();

        btnPlay.setOnClickListener(mc);
        btnChallenge.setOnClickListener(mc);
        btnRules.setOnClickListener(mc);
        btnHelpUs.setOnClickListener(mc);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    }


    public class MyClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnPlay) {
                Intent startGame = new Intent(Main.this, Game.class);
                startActivity(startGame);
            }
            if (v.getId() == R.id.btnChallenge) {

                Intent challenge = new Intent(Main.this, BluetoothChallenge.class);
                startActivity(challenge);

            }
            if (v.getId() == R.id.btnRules) {
                Intent readRules = new Intent(Main.this, Rules.class);
                startActivity(readRules);
            }
            if (v.getId() == R.id.btnHelpUs) {
                Intent Help = new Intent(Main.this, HelpUs.class);
                startActivity(Help);
            }
        }
    }


}



