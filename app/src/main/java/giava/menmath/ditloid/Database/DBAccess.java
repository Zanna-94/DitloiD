package giava.menmath.ditloid.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import giava.menmath.ditloid.Ditloid;

/**
 * @author Emanuele Vannacci
 *
 * Interface for {@link DatabaseAccess}. It
 */
public interface DBAccess {

    void open();

    void close();

    List<String> getDitloids();

    List<String> getSolutions();

    List<String> getCategory();

    List<Integer> getDifficulty();

    List<String> getHint();

    Ditloid getById(Integer id);

    ArrayList<Ditloid> getByLevel(Integer level);

    int getCount();

}
