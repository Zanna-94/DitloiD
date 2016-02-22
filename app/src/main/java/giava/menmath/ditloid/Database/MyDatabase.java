package giava.menmath.ditloid.Database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * @author MenMath.GiaVa
 *         <p/>
 *         This class represent the database. It's impelement {@link SQLiteAssetHelper} to manage
 *         assets file .db and version control.
 */

public class MyDatabase extends SQLiteAssetHelper {

    private static final int DATABASE_VERSION = 1;

    public MyDatabase(Context context, String DbName) {
        super(context, DbName, null, DATABASE_VERSION);
    }

}
