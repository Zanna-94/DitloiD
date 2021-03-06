package giava.menmath.ditloid.Game;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import giava.menmath.ditloid.Database.DatabaseAccess;
import giava.menmath.ditloid.Database.DatabaseAccessFactory;
import giava.menmath.ditloid.Database.TypeDB;
import giava.menmath.ditloid.Ditloid;
import giava.menmath.ditloid.MyApplication;
import giava.menmath.ditloid.R;
import giava.menmath.ditloid.User.UserInfo;

/**
 * his class retrieves {@link UserInfo} data to build dynamically the fragment's view
 *
 * @author MenMath.GiaVa
 */

public class ArrayListFragment extends ListFragment {

    private static int level;

    //ditloids for the right level
    Ditloid ditloid;

    private TextView tvNewDitloid;
    private TextView tvLevel;
    private TextView tvCategory;
    private TextView tvHelp;
    private TextView tvCredits;
    private TextView etSolution;
    private TextView btnCheck;
    private TextView btnGetCategory;
    private TextView btnGetHint;

    //number that represents fragment's position, passed from the adapter
    private int mNum;

    private UserInfo user;

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    static ArrayListFragment newInstance(int num, int value) {

        level = value;

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

        user = FragmentPagerSupport.getUser();

        //Obtain DatabaseAccess object to communicate with db
        DatabaseAccessFactory factory = new DatabaseAccessFactory();
        DatabaseAccess databaseAccess = factory.getDatabaseAccess(TypeDB.DB_Game);
        databaseAccess.open();

        ditloid = databaseAccess.getByLevel(level).get(mNum);

        databaseAccess.close();

    }

    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_game_level, container, false);

        //Initialize view
        tvNewDitloid = (TextView) v.findViewById(R.id.tvNewDitloid);
        tvLevel = (TextView) v.findViewById(R.id.tvLevel);
        tvCategory = (TextView) v.findViewById(R.id.tvCategory);
        tvHelp = (TextView) v.findViewById(R.id.tvHelp);
        tvCredits = (TextView) v.findViewById(R.id.tvCredits);
        etSolution = (TextView) v.findViewById(R.id.etSolution);
        btnCheck = (Button) v.findViewById(R.id.btnCheck);
        btnGetCategory = (Button) v.findViewById(R.id.btnGetCategory);
        btnGetHint = (Button) v.findViewById(R.id.btnGetHint);

        tvLevel.setText(String.format(getString(R.string.strLevel) + " %d.%d", level, mNum + 1));

        tvCredits.setText(String.format("%d", user.getCredit()));
        tvNewDitloid.setText(ditloid.getEnigma());
        tvCategory.setText(ditloid.getCategory());
        tvHelp.setText(ditloid.getHint());
        etSolution.setText(ditloid.getEnigma());


        //SetListener must be here because they make button clickable
        btnCheck.setOnClickListener(new CheckListener());
        btnGetCategory.setOnClickListener(new CategoryListener());
        btnGetHint.setOnClickListener(new HintListener());


        //if Level already passed by user
        if (user.getPassedLevel() != null &&
                user.getPassedDitloids(level) != null &&
                user.getPassedDitloids(level).contains(mNum)) {

            etSolution.setText(ditloid.getSolution());
            btnCheck.setClickable(false);
            btnGetCategory.setClickable(false);
            btnGetHint.setClickable(false);
            etSolution.setKeyListener(null);
        }

        if (user.getHintGet() != null &&
                user.getHintGet().containsKey(level)) {

            ArrayList<Integer> positions = user.getHintGet().get(level);
            if (positions != null && positions.contains(mNum)) {
                tvHelp.setVisibility(View.VISIBLE);
                btnGetHint.setClickable(false);
            }
        }

        if (user.getCategoryGet() != null &&
                user.getCategoryGet().containsKey(level)) {

            ArrayList<Integer> positions = user.getCategoryGet().get(level);
            if (positions != null && positions.contains(mNum)) {
                tvCategory.setVisibility(View.VISIBLE);
                btnGetCategory.setClickable(false);
            }
        }


        return v;

    }

    /**
     * This class controls if the user answered is correct
     * If it is it set all button not clickable, hide the keyboard and updates credits
     */
    private class CheckListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            String check = etSolution.getText().toString().toLowerCase().trim();
            String correct = ditloid.getSolution().toLowerCase().trim();

            if (check.equals(correct)) {


                Toast.makeText(MyApplication.getAppContext(), getString(R.string.strLevelPassed) +
                                " " + Integer.toString(ditloid.getDifficulty()),
                        Toast.LENGTH_SHORT).show();

                btnCheck.setClickable(false);
                btnGetCategory.setClickable(false);
                btnGetHint.setClickable(false);
                etSolution.setKeyListener(null);

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etSolution.getWindowToken(), 0);

                //Update user data
                user.addCredit(ditloid.getDifficulty());
                tvCredits.setText(String.format("%d", user.getCredit()));
                try {
                    user.addPassedDitloid(level, mNum);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MyApplication.getAppContext(), R.string.strImpossibleSave,
                            Toast.LENGTH_SHORT).show();
                }

                FragmentPagerSupport.refresh();

            } else {
                Toast.makeText(MyApplication.getAppContext(), R.string.strLevelNotPassed,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This class provides to show in the UI Ditloid's category
     * updating credits
     */
    private class CategoryListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (user.getCredit() >= 1) {

                btnGetCategory.setClickable(false);
                tvCategory.setVisibility(View.VISIBLE);

                //Update Data user
                user.subCredit(1);
                user.addCategoryGet(level, mNum);

                tvCredits.setText(String.format("%d", user.getCredit()));
                FragmentPagerSupport.refresh();

            } else
                Toast.makeText(MyApplication.getAppContext(), R.string.strNotEnoughCredit,
                        Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This class provides to show in the UI Ditloid's hint
     * updating credits
     */

    private class HintListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (user.getCredit() >= 5) {

                btnGetHint.setClickable(false);
                tvHelp.setVisibility(View.VISIBLE);

                user.subCredit(5);
                user.addHintGet(level, mNum);

                tvCredits.setText(String.format("%d", user.getCredit()));
                FragmentPagerSupport.refresh();

            } else
                Toast.makeText(MyApplication.getAppContext(), R.string.strNotEnoughCredit,
                        Toast.LENGTH_SHORT).show();
        }
    }


    public void update() {
        tvCredits.setText(user.getCredit().toString());
    }


}

