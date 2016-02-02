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
import java.util.List;

/**
 * Created by tizianomenichelli on 02/02/16.
 */
public class GameLevel extends AppCompatActivity {

    private Button btnCheck, btnGetCategory, btnGetHint;
    private TextView tvCategory, tvHelp, tvNewDitloid, tvCredits, tvStars;
    private EditText etSolution;
    private String Correct;
    private Integer difficulty;

    int credits = 10; //TODO
    int stars = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_level);

        tvCategory = (TextView) findViewById(R.id.tvCategory);
        tvHelp = (TextView) findViewById(R.id.tvHelp);
        tvNewDitloid = (TextView) findViewById(R.id.tvNewDitloid);
        tvCredits = (TextView) findViewById(R.id.tvCredits);
        tvStars = (TextView) findViewById(R.id.tvStars);
        etSolution = (EditText) findViewById(R.id.etSolution);
        btnCheck = (Button) findViewById(R.id.btnCheck);
        btnGetCategory = (Button) findViewById(R.id.btnGetCategory);
        btnGetHint = (Button) findViewById(R.id.btnGetHint);

        MyClick mc = new MyClick();

        btnCheck.setOnClickListener(mc);
        btnGetCategory.setOnClickListener(mc);
        btnGetHint.setOnClickListener(mc);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<String> ditloids = databaseAccess.getDitloids();

        tvNewDitloid.setText(ditloids.get(44));
        Correct = databaseAccess.getSolutions().get(44);
        tvCategory.setText(databaseAccess.getCategory().get(44));
        tvHelp.setText(databaseAccess.getHint().get(44));
        tvCredits.setText(String.format("%d", credits));
        tvStars.setText(String.format("%d",stars));
        difficulty = databaseAccess.getDifficulty().get(44);

        databaseAccess.close();
    }

    public class MyClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnCheck) {
                checkSolution();

            }
            if (v.getId() == R.id.btnGetCategory) {
                if (credits >= 1) {
                    credits -= 1;
                    tvCredits.setText(String.format("%d", credits));
                    btnGetCategory.setClickable(false);
                    tvCategory.setVisibility(View.VISIBLE);
                }
            }
            if (v.getId() == R.id.btnGetHint) {
                if (credits >= 3) {
                    credits -= 3;
                    tvCredits.setText(String.format("%d", credits));
                    btnGetHint.setClickable(false);
                    tvHelp.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void checkSolution() {
        EditText sol1 = (EditText) (findViewById(R.id.etSolution));
        String check = sol1.getText().toString().toLowerCase();
        if (check.equals(Correct)) {
            Toast.makeText(this, "Complimenti! Livello superato", Toast.LENGTH_SHORT).show();
            stars += difficulty;
            tvStars.setText(String.format("%d",stars));
            btnCheck.setClickable(false);
            //TODO avanzamento fragment, blocco livello
        } else {
            Toast.makeText(this, "Hai sbagliato! Riprova", Toast.LENGTH_SHORT).show();
        }
    }
}


