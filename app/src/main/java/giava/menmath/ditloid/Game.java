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

import java.util.List;

/**
 * Created by tizianomenichelli on 28/01/16.
 */

public class Game extends AppCompatActivity {

    private ListView lvLevels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.lvLevels = (ListView) findViewById(R.id.lvLevels);
        String[] levelsList = new String[] {"Level 1", "Level 2", "Level 3", "Level 4", "Level 5", "Level 6", "Level 7", "Level 8", "Level 9", "Level 10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, levelsList);
        this.lvLevels.setAdapter(adapter);

        lvLevels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent startLevel = new Intent(Game.this, GameLevel.class);
                    startActivity(startLevel);
                }
            }
        });

    }


}
