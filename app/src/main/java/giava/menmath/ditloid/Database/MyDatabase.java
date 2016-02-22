package giava.menmath.ditloid.Database;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * This class
 */

public class MyDatabase extends SQLiteAssetHelper {

    private static final int DATABASE_VERSION = 1;

    public MyDatabase(Context context, String DbName) {
        super(context, DbName, null, DATABASE_VERSION);
    }

}
