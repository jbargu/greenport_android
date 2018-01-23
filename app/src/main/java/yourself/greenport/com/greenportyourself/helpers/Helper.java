package yourself.greenport.com.greenportyourself.helpers;

/**
 * Created by Jure Grabnar <grabnar12@gmail.com> on 23.1.2018.
 */



public class Helper {

    public static final String SERVICE_STATE = "service_state";

    public static final String PREFS_TAG = "prefs";


    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}

