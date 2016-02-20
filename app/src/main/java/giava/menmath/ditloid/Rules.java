package giava.menmath.ditloid;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import giava.menmath.ditloid.User.UserInfo;

/**
 * Created by tizianomenichelli on 02/02/16.
 */

public class Rules extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
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
}
