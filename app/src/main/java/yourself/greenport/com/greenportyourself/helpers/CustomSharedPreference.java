package yourself.greenport.com.greenportyourself.helpers;

/**
 * Created by Jure Grabnar <grabnar12@gmail.com> on 23.1.2018.
 */


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CustomSharedPreference {

    private SharedPreferences sharedPref;

    private Gson gson;

    public CustomSharedPreference(Context context) {
        sharedPref = context.getSharedPreferences(Helper.PREFS_TAG, Context.MODE_PRIVATE);
        gson = new Gson();
    }
    public void setServiceRunningState(boolean state) {
        sharedPref.edit().putBoolean(Helper.SERVICE_STATE, state).apply();
    }

    public boolean isServiceRunningState() {
        return sharedPref.getBoolean(Helper.SERVICE_STATE, false);
    }
}

