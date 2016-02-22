package giava.menmath.ditloid.Database;

import android.content.Context;

import java.util.Locale;

import giava.menmath.ditloid.MyApplication;

/**
 * This class provide an implementation of the pattern Factory method.
 * It istantiates the class {@link DatabaseAccess} selecting the right database.
 *
 * @author MenMath.GiaVa
 */
public class DatabaseAccessFactory {

    Context context;
    String language;

    public DatabaseAccessFactory() {
        context = MyApplication.getAppContext();
        language = Locale.getDefault().getLanguage();
    }

    public DatabaseAccess getDatabaseAccess(TypeDB type) {
        switch (type) {
            case DB_Game:
                if (language.equals("it"))
                    return new DatabaseAccess(context, "db_ita");
                else
                    return new DatabaseAccess(context, "db_eng");

            case DB_CHALLENGE:
                if (language.equals("it"))
                    return new DatabaseAccess(context, "db_sfide_ita");
                else
                    return new DatabaseAccess(context, "db_sfide_eng");

            default:
                return new DatabaseAccess(context, "db_eng");
        }
    }

}

