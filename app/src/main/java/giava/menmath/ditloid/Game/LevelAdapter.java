package giava.menmath.ditloid.Game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import giava.menmath.ditloid.Ditloid;
import giava.menmath.ditloid.R;
import giava.menmath.ditloid.User.UserDao;
import giava.menmath.ditloid.User.UserInfo;

/**
 * Created by emanuele on 16/02/16.
 */
class LevelAdapter extends BaseAdapter {

    Context context;
    String[] level;
    private static LayoutInflater inflater = null;

    UserInfo user;

    public LevelAdapter(Context context, String[] level, UserInfo user) {
        this.context = context;
        this.level = level;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.user = user;

    }

    @Override
    public int getCount() {
        return level.length;
    }

    @Override
    public Object getItem(int position) {
        return level[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);

        TextView text = (TextView) vi.findViewById(R.id.tvLevel);
        TextView unlock = (TextView) vi.findViewById(R.id.tvUnlock);
        ImageView image = (ImageView) vi.findViewById(R.id.ivLock);
        ImageView image_whStar1 = (ImageView) vi.findViewById(R.id.ivStar1);
        ImageView image_whStar2 = (ImageView) vi.findViewById(R.id.ivStar2);
        ImageView image_whStar3 = (ImageView) vi.findViewById(R.id.ivStar3);
        ImageView image_whStar4 = (ImageView) vi.findViewById(R.id.ivStar4);
        ImageView image_whStar5 = (ImageView) vi.findViewById(R.id.ivStar5);

        text.setText(level[position]);

        Integer passedLevel = user.getLastPassedLevel();

        if (position <= passedLevel ) {
            unlock.setVisibility(View.GONE);

            image_whStar1.setVisibility(View.VISIBLE);
            image_whStar2.setVisibility(View.VISIBLE);
            image_whStar3.setVisibility(View.VISIBLE);
            image_whStar4.setVisibility(View.VISIBLE);
            image_whStar5.setVisibility(View.VISIBLE);

            ArrayList ditloids = (ArrayList) user.getPassedDitloids(position + 1);
            Integer numberPassed = ditloids != null ? ditloids.size() : 0;

            if (numberPassed > 0)
                image_whStar1.setImageResource(R.drawable.ic_black_star);
            else
                image_whStar1.setImageResource(R.drawable.ic_whitestar_vuota);

            if (numberPassed > 1)
                image_whStar2.setImageResource(R.drawable.ic_black_star);
            else
                image_whStar2.setImageResource(R.drawable.ic_whitestar_vuota);

            if (numberPassed > 2)
                image_whStar3.setImageResource(R.drawable.ic_black_star);
            else
                image_whStar3.setImageResource(R.drawable.ic_whitestar_vuota);

            if (numberPassed > 3)
                image_whStar4.setImageResource(R.drawable.ic_black_star);
            else
                image_whStar4.setImageResource(R.drawable.ic_whitestar_vuota);

            if (numberPassed > 4)
                image_whStar5.setImageResource(R.drawable.ic_black_star);
            else
                image_whStar5.setImageResource(R.drawable.ic_whitestar_vuota);


        } else {
            image_whStar1.setVisibility(View.INVISIBLE);
            image_whStar2.setVisibility(View.INVISIBLE);
            image_whStar3.setVisibility(View.INVISIBLE);
            image_whStar4.setVisibility(View.INVISIBLE);
            image_whStar5.setVisibility(View.INVISIBLE);

            unlock.setVisibility(View.VISIBLE);
            unlock.setText((position * 3) + " more to unlock");

        }

        if (position <= passedLevel )
            image.setImageResource(R.drawable.ic_lock_open_black_24dp);
        else
            image.setImageResource(R.drawable.ic_lock_black_24dp);

        return vi;
    }
}