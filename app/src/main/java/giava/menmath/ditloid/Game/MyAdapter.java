package giava.menmath.ditloid.Game;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * This class provide to create instances for fragments
 *
 * @author MenMath.GiaVa
 */

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

    @Override
    public int getItemPosition(Object object) {
        ArrayListFragment f = (ArrayListFragment) object;
        if (f != null) {
            f.update();
        }
        return super.getItemPosition(object);
    }
}