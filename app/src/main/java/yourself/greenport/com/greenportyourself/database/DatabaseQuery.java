package yourself.greenport.com.greenportyourself.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


import java.util.ArrayList;
import java.util.List;

public class DatabaseQuery extends DatabaseObject {


    public DatabaseQuery(Context context) {
        super(context);
    }

    public List<LocationObject> getAllLocationObjects() {
        List<LocationObject> allLocations = new ArrayList<LocationObject>();
        String query = "Select * from " + LocationObject.TABLE_NAME;
        Cursor cursor = this.getDbConnection().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow("timestamp"));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
                allLocations.add(new LocationObject(id, timestamp, latitude, longitude));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return allLocations;
    }


    public void addNewLocationObject(long timestamp, double latitude, double longitude) {
        ContentValues values = new ContentValues();
        values.put(LocationObject.COLUMN_TIMESTAMP, timestamp);
        values.put(LocationObject.COLUMN_LATITUDE, latitude);
        values.put(LocationObject.COLUMN_LONGITUDE, longitude);
        getDbConnection().insert(LocationObject.TABLE_NAME, null, values);
    }

}
