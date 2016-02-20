package giava.menmath.ditloid;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Filter;

import giava.menmath.ditloid.Bluetooth.BluetoothChallenge;
import giava.menmath.ditloid.Game.Game;
import giava.menmath.ditloid.User.UserInfo;

public class Main extends AppCompatActivity {

    private Button btnPlay, btnChallenge, btnRules, btnHelpUs;
    private TextView tvCredits;

    /**
     * Int value that provides request code for Bluetooth activation intent
     */
    private static final int REQUEST_ENABLE_BT = 1;

    private BluetoothAdapter mBluetoothAdapter;
    private Menu mMenu;

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.mMenu = menu;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);

        MenuItem myMenuItem = mMenu.findItem(R.id.tvCredits);
        myMenuItem.setTitle(UserInfo.getInstance().getCredit().toString());

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        //if (id == R.id.action_settings) {
        //return true;
        //}
        //return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.action_favorite:
                Toast.makeText(getBaseContext(), "Numero di crediti rimanenti: " + UserInfo.getInstance().getCredit(), Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(getBaseContext(), "Clicca per le impostazioni", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
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



