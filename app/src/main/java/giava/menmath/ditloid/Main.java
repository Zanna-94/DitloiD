package giava.menmath.ditloid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import giava.menmath.ditloid.Bluetooth.BluetoothChallenge;
import giava.menmath.ditloid.Game.Game;

/**
 * Created by MenMath.GiaVa
 */

public class Main extends AppCompatActivity {

    private Button btnPlay, btnChallenge, btnRules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnChallenge = (Button) findViewById(R.id.btnChallenge);
        btnRules = (Button) findViewById(R.id.btnRules);
        MyClick mc = new MyClick();

        btnPlay.setOnClickListener(mc);
        btnChallenge.setOnClickListener(mc);
        btnRules.setOnClickListener(mc);
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
        }
    }
}



