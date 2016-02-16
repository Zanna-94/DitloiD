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
        ImageView image = (ImageView) vi.findViewById(R.id.ivLock);

        text.setText(level[position]);

        Integer passedLevel = userInfo.getLastPassedLevel();

        if (position <= passedLevel - 1)
            image.setImageResource(R.drawable.ic_lock_open_black_24dp);
        else
            image.setImageResource(R.drawable.ic_lock_black_24dp);


        return vi;
    }
}