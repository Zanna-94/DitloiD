package giava.menmath.ditloid.Game;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import giava.menmath.ditloid.Game.Game;

public class MyAdapter extends FragmentPagerAdapter {

    private static final int NUM_ITEMS = 5;
    private int level;


    public MyAdapter(FragmentManager fm, Integer level) {
        super(fm);
        this.level = level;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return ArrayListFragment.newInstance(position, level);
    }

}