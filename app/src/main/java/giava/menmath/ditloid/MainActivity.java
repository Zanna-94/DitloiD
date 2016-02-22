package giava.menmath.ditloid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by MenMath.GiaVa
 *
 *  This class is the first one to be called
 *  Implement the start animation
 */

public class MainActivity  extends AppCompatActivity {

    private TextView start, end, center, touch;
    private Animation transition1, transition2, fade1, fade2;
    private RelativeLayout screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bounce);

        start = (TextView) findViewById(R.id.tvStartD);
        end = (TextView) findViewById(R.id.tvEndD);
        center = (TextView) findViewById(R.id.tvCenter);
        touch = (TextView) findViewById(R.id.tvTouch);
        screen = (RelativeLayout) findViewById(R.id.screen);

        screen.setOnClickListener(new MyClick());

        transition1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce_1);
        transition2 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce_2);
        fade1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce_3);
        fade2 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce_4);


        start.startAnimation(transition1);
        end.startAnimation(transition2);
        center.setVisibility(View.VISIBLE);
        center.startAnimation(fade1);
        touch.startAnimation(fade2);
    }

    public class MyClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.screen) {
                Intent start = new Intent(MainActivity.this, Main.class);
                startActivity(start);
                finish();
            }
        }
    }
}




