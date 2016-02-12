package giava.menmath.ditloid;

/**
 * Created by tizianomenichelli on 30/01/16.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private MyDatabase db;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.db = new MyDatabase(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = db.getReadableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public List<String> getDitloids() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Enigma from ditloid", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getSolutions() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Soluzione from ditloid", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getCategory() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Categoria from ditloid", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }


    public List<Integer> getDifficulty() {
        List<Integer> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Difficoltà from ditloid", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getInt(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getHint() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Indizio from ditloid", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public Ditloid getById(Integer id) {

        Ditloid ditloid = new Ditloid();
        ditloid.setId(id);

        String query = "SELECT * from ditloid where ROWID ='"+id+"'";

        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToLast();
        if (cursor.getCount() != 1) {
            cursor.close();
            return null;
        }

        ditloid.setCategory(cursor.getString(cursor.getColumnIndex("Categoria")));
        ditloid.setSolution(cursor.getString(cursor.getColumnIndex("Soluzione")));
        ditloid.setEnigma(cursor.getString(cursor.getColumnIndex("Enigma")));
        ditloid.setHint(cursor.getString(cursor.getColumnIndex("Indizio")));
        ditloid.setDifficulty(cursor.getInt(cursor.getColumnIndex("Difficoltà")));

        cursor.close();
        return ditloid;

    }

    public int getCount() {
        return database.rawQuery("SELECT Enigma FROM ditloid", null).getCount();
    }

//    public List<Integer> getLevel() {
//        List<Integer> list = new ArrayList<>();
//        Cursor cursor = database.rawQuery("SELECT Livello from ditloid" , null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            list.add(cursor.getInt(0));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return list;
//    }
//
//   public List<Integer> getRisolto() {
//        List<Integer> list = new ArrayList<>();
//        Cursor cursor = database.rawQuery("SELECT Risolto from ditloid" , null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            list.add(cursor.getInt(0));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return list;
//    }
//
//   public List<Integer> getLevel() {
//        List<Integer> list = new ArrayList<>();
//        Cursor cursor = database.rawQuery("SELECT Categoria_Sbloccata from ditloid" , null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            list.add(cursor.getInt(0));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return list;
//    }
//
//   public List<Integer> getLevel() {
//        List<Integer> list = new ArrayList<>();
//        Cursor cursor = database.rawQuery("SELECT Indizio_Sbloccato from ditloid" , null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            list.add(cursor.getInt(0));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return list;
//    }
}