package yourself.greenport.com.greenportyourself.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAMES = "location_data.db";
    private static final int DATABASE_VERSION = 1;

    public Database(Context context) {
        super(context, DATABASE_NAMES, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCATIONS_TABLE = "CREATE    TABLE " + LocationObject.TABLE_NAME + "(" + LocationObject.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + LocationObject.COLUMN_TIMESTAMP + " INTEGER," + LocationObject.COLUMN_LATITUDE + " REAL, " + LocationObject.COLUMN_LONGITUDE + " REAL" + ")";
        db.execSQL(CREATE_LOCATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + LocationObject.TABLE_NAME);
        onCreate(db);
    }
}
