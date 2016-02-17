package giava.menmath.ditloid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import giava.menmath.ditloid.Database.DatabaseAccess;
import giava.menmath.ditloid.Database.DatabaseAccessFactory;
import giava.menmath.ditloid.Database.TypeDB;
import giava.menmath.ditloid.User.UserInfo;

/**
 * Created by tizianomenichelli on 03/02/16.
 */

public class FragmentPagerSupport extends FragmentActivity {

    static final int NUM_ITEMS = 5;

    MyAdapter mAdapter;
    ViewPager mPager;

    SharedPreferences prefs = MyApplication.getAppContext().
            getSharedPreferences("giava.menmath.ditloid.app", Context.MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_level_x);

        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
    }

    public class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return ArrayListFragment.newInstance(position);
        }
    }

    public static class ArrayListFragment extends ListFragment {

        UserInfo user = UserInfo.getInstance();

        private int credit = user.getCredit();
        int mNum;

        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        static ArrayListFragment newInstance(int num) {
            ArrayListFragment f = new ArrayListFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        /**
         * The Fragment's UI is just a simple text view showing its
         * instance number.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View v = inflater.inflate(R.layout.activity_game_level, container, false);

            Integer level = 1;
            View tvNewDitloid = v.findViewById(R.id.tvNewDitloid);
            View tvLevel = v.findViewById(R.id.tvLevel);
            final View tvCategory = v.findViewById(R.id.tvCategory);
            final View tvHelp = v.findViewById(R.id.tvHelp);
            final View tvCredits = v.findViewById(R.id.tvCredits);
            final View etSolution = v.findViewById(R.id.etSolution);
            final View btnCheck = v.findViewById(R.id.btnCheck);
            final View btnGetCategory = v.findViewById(R.id.btnGetCategory);
            final View btnGetHint = v.findViewById(R.id.btnGetHint);

            ((TextView) tvLevel).setText("Level " + level + "." + (mNum + 1));
            ((TextView) tvCredits).setText(String.format("%d", credit));


            DatabaseAccessFactory factory = new DatabaseAccessFactory();
            DatabaseAccess databaseAccess = factory.getDatabaseAccess(TypeDB.DB_Game);

            databaseAccess.open();
            ((TextView) tvNewDitloid).setText(databaseAccess.getDitloids().get(mNum));
            ((EditText) etSolution).setText(databaseAccess.getDitloids().get(mNum));
            ((TextView) tvCategory).setText(databaseAccess.getCategory().get(mNum));
            ((TextView) tvHelp).setText(databaseAccess.getHint().get(mNum));

            final String correct = databaseAccess.getSolutions().get(mNum);
            final Integer difficulty = databaseAccess.getDifficulty().get(mNum);

            databaseAccess.close();


            btnCheck.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String check = ((EditText) etSolution).getText().toString().toLowerCase();
                    if (check.equals(correct)) {
                        Toast.makeText(MyApplication.getAppContext(), "Complimenti! Livello superato", Toast.LENGTH_SHORT).show();
                        credit = user.addCredit(difficulty);
                        ((TextView) tvCredits).setText(String.format("%d", credit));
                        btnCheck.setClickable(false);
                        btnGetCategory.setClickable(false);
                        btnGetHint.setClickable(false);
                        ((EditText) etSolution).setKeyListener(null);
                    } else {
                        Toast.makeText(MyApplication.getAppContext(), "Hai sbagliato! Riprova", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btnGetCategory.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (credit >= 1) {
                        credit = user.subCredit(1);
                        ((TextView) tvCredits).setText(String.format("%d", credit));
                        btnGetCategory.setClickable(false);
                        tvCategory.setVisibility(View.VISIBLE);
                        Toast.makeText(MyApplication.getAppContext(), "Non hai abbastanza crediti!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btnGetHint.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (credit >= 5) {
                        credit = user.subCredit(5);
                        ((TextView) tvCredits).setText(String.format("%d", credit));
                        btnGetHint.setClickable(false);
                        tvHelp.setVisibility(View.VISIBLE);
                        Toast.makeText(MyApplication.getAppContext(), "Non hai abbastanza crediti!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return v;
        }
    }
}
