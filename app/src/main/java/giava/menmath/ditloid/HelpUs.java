package giava.menmath.ditloid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


/**
 * Created by tizianomenichelli on 30/01/16.
 */
public class HelpUs extends AppCompatActivity {

    private Button btnNewDitloid, btnSignalError, btnGiveTip, btnEstimate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        btnNewDitloid = (Button) findViewById(R.id.btnNewDitloid);
        btnSignalError = (Button) findViewById(R.id.btnSignalError);
        btnGiveTip = (Button) findViewById(R.id.btnGiveTip);
        btnEstimate = (Button) findViewById(R.id.btnEstimate);

        MyClick mc = new MyClick();

        btnNewDitloid.setOnClickListener(mc);
        btnSignalError.setOnClickListener(mc);
        btnGiveTip.setOnClickListener(mc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
            if (v.getId() == R.id.btnNewDitloid) {
                Intent NewDitloid = new Intent(HelpUs.this, NewDitloid.class);
                startActivity(NewDitloid);
            }
            if (v.getId() == R.id.btnSignalError) {
                Intent SignalError = new Intent(HelpUs.this, SignalError.class);
                startActivity(SignalError);
            }
            if (v.getId() == R.id.btnGiveTip) {
                Intent GiveTip = new Intent(HelpUs.this, GiveTip.class);
                startActivity(GiveTip);
            }
            if (v.getId() == R.id.btnEstimate) {
                Intent GiveTip = new Intent(HelpUs.this, GiveTip.class);
                startActivity(GiveTip);
            }
        }
    }
}
