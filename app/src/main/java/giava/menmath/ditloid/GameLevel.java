package giava.menmath.ditloid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.awt.font.TextAttribute;

/**
 * Created by tizianomenichelli on 02/02/16.
 */
public class GameLevel extends AppCompatActivity {

    private Button btnCheck, btnGetCategory, btnGetHint;
    private TextView tvCategory, tvHelp;
    private EditText etSolution;
    int credits = 10; //TODO

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_level);

        tvCategory = (TextView) findViewById(R.id.tvCategory);
        tvHelp = (TextView) findViewById(R.id.tvHelp);
        etSolution = (EditText) findViewById(R.id.etSolution);
        btnCheck = (Button) findViewById(R.id.btnCheckSolution);
        btnGetCategory = (Button) findViewById(R.id.btnGetCategory);
        btnGetHint = (Button) findViewById(R.id.btnGetHint);

        MyClick mc = new MyClick();

        btnCheck.setOnClickListener(mc);
        btnGetCategory.setOnClickListener(mc);
        btnGetHint.setOnClickListener(mc);
    }

    public class MyClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnCheckSolution) {
                checkSolution();
            }
            if (v.getId() == R.id.btnGetCategory) {
                if (credits >= 1) {
                    credits -= 1;
                    btnGetCategory.setClickable(false);
                    tvCategory.setVisibility(View.VISIBLE);
                }
            }
            if (v.getId() == R.id.btnGetHint) {
                if (credits >= 3) {
                    credits -= 3;
                    btnGetHint.setClickable(false);
                    tvHelp.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void checkSolution() {
        EditText sol1 = (EditText) (findViewById(R.id.etSolution));
        String check = sol1.getText().toString().toLowerCase();
        String correct = "365 giorni in un anno"; //TODO database
        if (check.equals(correct)) {
            Toast.makeText(this, "Complimenti", Toast.LENGTH_SHORT).show();
            //TODO avanzamento fragment, blocco livello
        } else {
            Toast.makeText(this, "Hai sbagliato! Riprova", Toast.LENGTH_SHORT).show();
        }
    }
}


