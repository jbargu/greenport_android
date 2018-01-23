package yourself.greenport.com.greenportyourself;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

/**
 * Service that recognizes the travel mode in which the user is currently in and saves it to globaly variable.
 *
 * Created by Jure Grabnar <grabnar12@gmail.com> on 23.1.2018.
 */

public class ActivityRecognizedService extends IntentService {

    // Variable that holds the travel mode from the last update
    private static int LATEST_TRAVEL_MODE;

    private static final int THRESHOLD_CONFIDENCE = 75;

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("TAG", intent + "");
        // Check whether this is an activity recognition event
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities(result.getProbableActivities());
        }
    }

    // Callback for activity recognition event
    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        int maxConfidence = 0, maxTravelMode = 0;
        for (DetectedActivity activity : probableActivities) {
            switch (activity.getType()) {
                case DetectedActivity.IN_VEHICLE: {
                    Log.e("ActivityRecogition", "In Vehicle: " + activity.getConfidence());
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    Log.e("ActivityRecogition", "On Bicycle: " + activity.getConfidence());
                    break;
                }
                case DetectedActivity.ON_FOOT: {
                    Log.e("ActivityRecogition", "On Foot: " + activity.getConfidence());
                    break;
                }
                case DetectedActivity.RUNNING: {
                    Log.e("ActivityRecogition", "Running: " + activity.getConfidence());
                    break;
                }
                case DetectedActivity.STILL: {
                    Log.e("ActivityRecogition", "Still: " + activity.getConfidence());
                    break;
                }
                case DetectedActivity.TILTING: {
                    Log.e("ActivityRecogition", "Tilting: " + activity.getConfidence());
                    break;
                }
                case DetectedActivity.WALKING: {
                    Log.e("ActivityRecogition", "Walking: " + activity.getConfidence());
                    break;
                }
                case DetectedActivity.UNKNOWN: {
                    Log.e("ActivityRecogition", "Unknown: " + activity.getConfidence());
                    break;
                }
            }

            // Check for max confidence
            if (activity.getConfidence() > maxConfidence)
                maxTravelMode = activity.getType();
        }

        // Update current travel mode iff the max confidence exceeds
        if (maxConfidence >= THRESHOLD_CONFIDENCE)
            LATEST_TRAVEL_MODE = maxTravelMode;
    }

    // Get the latest travel mode
    public static int getTravelMode() {
        return LATEST_TRAVEL_MODE;
    }
}

