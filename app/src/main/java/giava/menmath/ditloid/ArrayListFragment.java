package giava.menmath.ditloid;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import giava.menmath.ditloid.Database.DatabaseAccess;
import giava.menmath.ditloid.Database.DatabaseAccessFactory;
import giava.menmath.ditloid.Database.TypeDB;
import giava.menmath.ditloid.User.UserInfo;


public class ArrayListFragment extends ListFragment {

    private static int level;

    //ditloids for the right level
    ArrayList<Ditloid> ditloids;

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
    private int credit;


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

        FragmentPagerSupport activity = (FragmentPagerSupport) getActivity();
        user = activity.getUser();

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

        tvLevel.setText(String.format("Level %d.%d", level, mNum + 1));
        tvCredits.setText(String.format("%d", user.getCredit()));


        //Obtain DatabaseAccess object to communicate with db
        DatabaseAccessFactory factory = new DatabaseAccessFactory();
        DatabaseAccess databaseAccess = factory.getDatabaseAccess(TypeDB.DB_Game);
        databaseAccess.open();

        ditloids = databaseAccess.getByLevel(level);

/*
        tvNewDitloid.setText(databaseAccess.getDitloids().get(mNum));
        etSolution.setText(databaseAccess.getDitloids().get(mNum));
        tvCategory.setText(databaseAccess.getCategory().get(mNum));
        tvHelp.setText(databaseAccess.getHint().get(mNum));

        String correct = databaseAccess.getSolutions().get(mNum);
        Integer difficulty = databaseAccess.getDifficulty().get(mNum);
*/

        databaseAccess.close();

        tvNewDitloid.setText(ditloids.get(mNum).getEnigma());
        etSolution.setText(ditloids.get(mNum).getEnigma());
        tvCategory.setText(ditloids.get(mNum).getCategory());
        tvHelp.setText(ditloids.get(mNum).getHint());


        btnCheck.setOnClickListener(new CheckListener());
        btnGetCategory.setOnClickListener(new CategoryListener());
        btnGetHint.setOnClickListener(new HintListener());

        return v;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FragmentPagerSupport activity = (FragmentPagerSupport) getActivity();
        activity.serializza();
    }


    private class CheckListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            String check = etSolution.getText().toString().toLowerCase().trim();
            String correct = ditloids.get(mNum).getSolution().toLowerCase().trim();

            if (check.equals(correct)) {
                Toast.makeText(MyApplication.getAppContext(), R.string.strLevelPassed,
                        Toast.LENGTH_SHORT).show();

                user.addCredit(ditloids.get(mNum).getDifficulty());

                tvCredits.setText(String.format("%d", user.getCredit()));

                btnCheck.setClickable(false);
                btnGetCategory.setClickable(false);
                btnGetHint.setClickable(false);
                etSolution.setKeyListener(null);

            } else {
                Toast.makeText(MyApplication.getAppContext(), R.string.strLevelNotPassed,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class CategoryListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (user.getCredit() >= 1) {

                user.subCredit(1);

                tvCredits.setText(String.format("%d", user.getCredit()));

                btnGetCategory.setClickable(false);
                tvCategory.setVisibility(View.VISIBLE);

            } else
                Toast.makeText(MyApplication.getAppContext(), R.string.strNotEnoughCredit,
                        Toast.LENGTH_SHORT).show();

        }
    }

    private class HintListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (user.getCredit() >= 5) {

                user.subCredit(5);

                tvCredits.setText(String.format("%d", user.getCredit()));
                btnGetHint.setClickable(false);
                tvHelp.setVisibility(View.VISIBLE);

            } else
                Toast.makeText(MyApplication.getAppContext(), R.string.strNotEnoughCredit,
                        Toast.LENGTH_SHORT).show();

        }
    }



}

