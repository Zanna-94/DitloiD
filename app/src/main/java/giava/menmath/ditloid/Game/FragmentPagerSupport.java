package giava.menmath.ditloid.Game;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import java.io.IOException;

import giava.menmath.ditloid.R;
import giava.menmath.ditloid.User.UserDao;
import giava.menmath.ditloid.User.UserInfo;


public class FragmentPagerSupport extends FragmentActivity {

    private static MyAdapter mAdapter;
    private ViewPager mPager;

    private static UserInfo  user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_level_x);

        Bundle extras = getIntent().getExtras();
        Integer value = 1;
        if (extras != null)
            value =  extras.getInt("Level");

        mAdapter = new MyAdapter(getSupportFragmentManager(), value);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        deserializza();

    }

    protected void onPause(){
        super.onPause();
        serializza();
    }

    public static void serializza(){

        if(user!=null){
            try {
                UserDao.serializza(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deserializza(){
        try {
            user = UserDao.deserializza();
            System.out.println("deserializza ok");
        } catch (IOException e) {
            e.printStackTrace();
            user = UserInfo.getInstance();
            System.out.println("deserializza failed");
        }
    }

    public static UserInfo getUser(){
        return user;
    }

    public static void refresh(){
        mAdapter.notifyDataSetChanged();
    }

}
