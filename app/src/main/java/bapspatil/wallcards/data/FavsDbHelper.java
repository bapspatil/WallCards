package bapspatil.wallcards.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bapspatil
 */

public class FavsDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    public FavsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " + FavsContract.FavsEntry.TABLE_NAME + " (" +
                FavsContract.FavsEntry._ID + " TEXT PRIMARY KEY, " +
                FavsContract.FavsEntry.COLUMN_COLOR + " TEXT, " +
                FavsContract.FavsEntry.COLUMN_LIKES + " INTEGER, " +
                FavsContract.FavsEntry.COLUMN_URL + " TEXT NOT NULL, " +
                FavsContract.FavsEntry.COLUMN_USER_PIC + " TEXT, " +
                FavsContract.FavsEntry.COLUMN_USER_NAME + " TEXT);";
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavsContract.FavsEntry.TABLE_NAME);
        onCreate(db);
    }
}
