package giava.menmath.ditloid.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import giava.menmath.ditloid.Ditloid;

/**
 * Class provides method to access to the database and retrieve record.
 * It's comunicate with database represented by {@link giava.menmath.ditloid.Database.MyDatabase
 *
 * @author MenMath.GiaVa
 */
public class DatabaseAccess implements DBAccess {

    private MyDatabase db;
    private SQLiteDatabase database;

    /**
     * Default constructor
     *
     * @param context : Application context
     * @param DbName  : Database name in assets directory
     */
    public DatabaseAccess(Context context, String DbName) {
        this.db = new MyDatabase(context, DbName);
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

    /**
     * @param id : id in Database for researched ditloid
     * @return {@link Ditloid}
     */
    public Ditloid getById(Integer id) {

        Ditloid ditloid = new Ditloid();
        ditloid.setId(id);

        String query = "SELECT * from ditloid where ROWID ='" + id + "'  LIMIT 0,1";

        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToLast();
        if (cursor.getCount() != 1) {
            cursor.close();
            return null;
        }

        ditloid.setCategory(cursor.getString(cursor.getColumnIndex("Categoria")));
        ditloid.setSolution(cursor.getString(cursor.getColumnIndex("Soluzione")));
        ditloid.setEnigma(cursor.getString(cursor.getColumnIndex("Enigma")));

        // in case of Challenge database not contains these columns
        if (cursor.getColumnIndex("Indizio") == -1 || cursor.getColumnIndex("Difficoltà") == -1 ||
                cursor.getColumnIndex("Livello") == -1) {

            cursor.close();
            return ditloid;
        }

        ditloid.setHint(cursor.getString(cursor.getColumnIndex("Indizio")));
        ditloid.setDifficulty(cursor.getInt(cursor.getColumnIndex("Difficoltà")));
        ditloid.setLevel(cursor.getInt(cursor.getColumnIndex("Livello")));

        cursor.close();
        return ditloid;

    }

    @Override
    public ArrayList<Ditloid> getByLevel(Integer level) {

        ArrayList<Ditloid> ditloids = new ArrayList<>();

        String query = "SELECT * from ditloid where Livello ='" + level + "'  LIMIT 5";
        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Ditloid dit = new Ditloid();

            dit.setCategory(cursor.getString(cursor.getColumnIndex("Categoria")));
            dit.setSolution(cursor.getString(cursor.getColumnIndex("Soluzione")));
            dit.setEnigma(cursor.getString(cursor.getColumnIndex("Enigma")));

            // in case of Challenge database not contains these columns
            if (cursor.getColumnIndex("Indizio") == -1 || cursor.getColumnIndex("Difficoltà") == -1 ||
                    cursor.getColumnIndex("Livello") == -1) {

                break;
            }

            dit.setHint(cursor.getString(cursor.getColumnIndex("Indizio")));
            dit.setDifficulty(cursor.getInt(cursor.getColumnIndex("Difficoltà")));
            dit.setLevel(cursor.getInt(cursor.getColumnIndex("Livello")));


            ditloids.add(dit);
            cursor.moveToNext();
        }
        cursor.close();

        return ditloids;

    }

    public int getCount() {
        return database.rawQuery("SELECT Enigma FROM ditloid", null).getCount();
    }

}
