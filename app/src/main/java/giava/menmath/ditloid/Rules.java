package giava.menmath.ditloid;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import giava.menmath.ditloid.User.UserInfo;

/**
 * Created by tizianomenichelli on 02/02/16.
 */

public class Rules extends AppCompatActivity {


    private TextView tvRules;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        tvRules = (TextView) findViewById(R.id.tvRules);
        tvRules.setMovementMethod(new ScrollingMovementMethod());

        String language = Locale.getDefault().getLanguage();
        StringBuilder text = new StringBuilder();
        BufferedReader reader;

        InputStream is;

        try {

            if (language.equals("it"))
                is = getAssets().open("regole.txt");
            else
                is = getAssets().open("rules.txt");

            reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }


        tvRules.setText(text.toString());

    }

}