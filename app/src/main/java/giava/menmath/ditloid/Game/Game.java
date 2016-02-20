package giava.menmath.ditloid.Game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TooManyListenersException;

import giava.menmath.ditloid.R;
import giava.menmath.ditloid.User.UserDao;
import giava.menmath.ditloid.User.UserInfo;


public class Game extends AppCompatActivity {


    /**
     * Number of levels that the the application must contains
     */
    private static final Integer LEVEL_NUM = 10;

    /**
     * Info about the user and his progress
     */
    private UserInfo user;

    private ListView lvLevels;
    private String[] levelsList = new String[LEVEL_NUM];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        lvLevels = (ListView) findViewById(R.id.lvLevels);

        ArrayList<String> alLevels = new ArrayList<>();
        for (int i = 1; i <= LEVEL_NUM; i++) {
            alLevels.add("Level " + i);
        }

        levelsList = alLevels.toArray(levelsList);

    }

    @Override
    protected void onResume() {
        super.onResume();

        deserializza();

        LevelAdapter myAdapter = new LevelAdapter(this, levelsList, user);
        lvLevels.setAdapter(myAdapter);
        lvLevels.setOnItemClickListener(new MyListener());

    }


    public void serializza() {
        if (user != null) {
            try {
                UserDao.serializza(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void deserializza() {
        try {
            user = UserDao.deserializza();
            System.out.println("deserializza ok");
        } catch (IOException e) {
            e.printStackTrace();
            user = UserInfo.getInstance();
            System.out.println("deserializza failed");
        }
    }

    protected class MyListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (position <= user.getLastPassedLevel()) {
                Intent startLevel = new Intent(Game.this, FragmentPagerSupport.class);
                startLevel.putExtra("Level", position + 1);
                startActivity(startLevel);
            }

        }
    }


}
