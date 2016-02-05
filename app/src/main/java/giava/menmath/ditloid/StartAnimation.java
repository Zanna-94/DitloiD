package giava.menmath.ditloid;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * Created by simonemattogno on 05/02/16.
 */
public class StartAnimation  extends Activity {

    TextView start, end, center;


    Animation bounce = AnimationUtils.loadAnimation(getApplicationContext(),
            R.layout.bounce);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bounce);

        start = (TextView) findViewById(R.id.tvStartD);
        end = (TextView) findViewById(R.id.tvEndD);
        center = (TextView) findViewById(R.id.tvCenter);


    }
}