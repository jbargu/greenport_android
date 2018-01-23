package yourself.greenport.com.greenportyourself.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


import java.util.ArrayList;
import java.util.List;

import yourself.greenport.com.greenportyourself.ActivityRecognizedService;

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
                int travelMode = cursor.getInt(cursor.getColumnIndexOrThrow("travel_mode"));
                allLocations.add(new LocationObject(id, timestamp, latitude, longitude, travelMode));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return allLocations;
    }


    public void addNewLocationObject(long timestamp, double latitude, double longitude, int travelMode) {
        ContentValues values = new ContentValues();
        values.put(LocationObject.COLUMN_TIMESTAMP, timestamp);
        values.put(LocationObject.COLUMN_LATITUDE, latitude);
        values.put(LocationObject.COLUMN_LONGITUDE, longitude);
        values.put(LocationObject.COLUMN_TRAVEL_MODE, travelMode);
        getDbConnection().insert(LocationObject.TABLE_NAME, null, values);
    }

}
