package giava.menmath.ditloid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import giava.menmath.ditloid.User.UserDao;
import giava.menmath.ditloid.User.UserInfo;

/**
 * Created by emanuele on 16/02/16.
 */
class LevelAdapter extends BaseAdapter {

    Context context;
    String[] level;
    private static LayoutInflater inflater = null;

    UserInfo userInfo;

    public LevelAdapter(Context context, String[] level) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.level = level;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        try {
            userInfo = UserDao.deserializza();
        } catch (IOException e) {
            e.printStackTrace();
            userInfo = UserInfo.getInstance();
        }

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

        Integer passedLevel = userInfo.getLastPassedLevel();

        if (position == 0) { //TODO (position*3)-risolti allora...
            //TODO singoli if per ogni immagine
            unlock.setVisibility(View.GONE);
            image_whStar1.setImageResource(R.drawable.ic_whitestar_vuota);
            image_whStar2.setImageResource(R.drawable.ic_whitestar_vuota);
            image_whStar3.setImageResource(R.drawable.ic_black_star);
            image_whStar4.setImageResource(R.drawable.ic_whitestar);
            image_whStar5.setImageResource(R.drawable.ic_whitestar);
        } else {
            unlock.setVisibility(View.VISIBLE);
            unlock.setText((position*3) + " more to unlock"); //TODO (position*3)-risolti
        }

        if (position <= passedLevel - 1)
            image.setImageResource(R.drawable.ic_lock_open_black_24dp);
        else
            image.setImageResource(R.drawable.ic_lock_black_24dp);

        return vi;
    }
}