package giava.menmath.ditloid;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Database Column{ Enigma: TEXT ; Soluzione: TEXT ; Categoria: TEXT; Difficoltà: INTEGER ; Indizio: TEXT}
 */

public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "ditloid.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
