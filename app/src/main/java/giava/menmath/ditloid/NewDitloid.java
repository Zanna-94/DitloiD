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

public class NewDitloid extends AppCompatActivity {

    private Button btnNewSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ditloid);

        btnNewSend = (Button) findViewById(R.id.btnNewSends);

        btnNewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent NewDitloidSend = new Intent(NewDitloid.this, NewDitloidSend.class);
                startActivity(NewDitloidSend);
            }
        });
    }

}
