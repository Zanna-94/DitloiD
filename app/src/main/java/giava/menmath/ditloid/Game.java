package giava.menmath.ditloid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giava.menmath.ditloid.User.UserDao;
import giava.menmath.ditloid.User.UserInfo;

/**
 * Created by tizianomenichelli on 28/01/16.
 */

public class Game extends AppCompatActivity {


    /**
     * Number of levels that the the application must contains
     */
    private static final Integer LEVEL_NUM = 10;

    /**
     * Info about the user and his progress
     */
    private UserInfo userInfo;

    private ListView lvLevels;
    private String[] levelsList = new String[LEVEL_NUM];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        lvLevels = (ListView) findViewById(R.id.lvLevels);

        ArrayList<String> alLevels = new ArrayList<>();
        for (int i = 1; i <= LEVEL_NUM; i++){
            alLevels.add("Level " + i);
        }

        levelsList =  alLevels.toArray(levelsList);

/*
        String[] levelsList = new String[]{"Level 1", "Level 2", "Level 3", "Level 4", "Level 5",
                "Level 6", "Level 7", "Level 8", "Level 9", "Level 10"};
*/

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1
                , levelsList);
        lvLevels.setAdapter(adapter);
        lvLevels.setOnItemClickListener(new MyListener());


        try {
            userInfo= UserDao.deserializza();
        } catch (IOException e) {
            e.printStackTrace();
            userInfo = UserInfo.getInstance();
            Toast.makeText(Game.this, "Benvenuto!", Toast.LENGTH_SHORT).show();
        }

    }


    protected class MyListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            int clickedLevel = position + 1;
            if(clickedLevel <= userInfo.getLastPassedLevel()){
                Intent startLevel = new Intent(Game.this, FragmentPagerSupport.class);
                startActivity(startLevel);
                startLevel.putExtra("Level", clickedLevel);
            }

        }
    }


}
