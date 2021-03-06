package yourself.greenport.com.greenportyourself;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import yourself.greenport.com.greenportyourself.database.DatabaseQuery;
import yourself.greenport.com.greenportyourself.helpers.CustomSharedPreference;

public class RouteService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = RouteService.class.getSimpleName();
    public static final String ACTION = ".RouteService";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private double latitudeValue = 0.0;
    private double longitudeValue = 0.0;
    private CustomSharedPreference customSharedPreference;
    private DatabaseQuery query;
    private boolean isServiceRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        customSharedPreference = new CustomSharedPreference(getApplicationContext());
        if (isRouteTrackingOn()) {
            Log.d(TAG, "Current time " + System.currentTimeMillis());
            Log.d(TAG, "Service is running");
        }
        query = new DatabaseQuery(getApplicationContext());
        mLocationRequest = createLocationRequest();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(ActivityRecognition.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isServiceRunning = true;
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "Connection method has been called");
        Intent intent = new Intent(this, ActivityRecognizedService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient, 2000, pendingIntent);

        customSharedPreference.setServiceRunningState(true);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                            if (mLastLocation != null) {
                                latitudeValue = mLastLocation.getLatitude();
                                longitudeValue = mLastLocation.getLongitude();
                                Log.d(TAG, "Latitude 1: " + latitudeValue + " Longitude 1: " + longitudeValue);
                                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, RouteService.this);
                            }
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.d(TAG, "Settings change unavailable.");
                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(200);
        mLocationRequest.setFastestInterval(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Latitude " + location.getLatitude() + " Longitude " + location.getLongitude());
        Log.d(TAG, "SERVICE RUNNING " + isServiceRunning);
        if (isRouteTrackingOn()) {
            latitudeValue = location.getLatitude();
            longitudeValue = location.getLongitude();
            Log.d(TAG, "Latitude " + latitudeValue + " Longitude " + longitudeValue + " Travel mode: " + ActivityRecognizedService.getTravelMode());

            // insert values to local sqlite database
            query.addNewLocationObject(System.currentTimeMillis(), latitudeValue, longitudeValue, ActivityRecognizedService.getTravelMode());

            // send local broadcast receiver to application components
            Intent localBroadcastIntent = new Intent(ACTION);
            localBroadcastIntent.putExtra("RESULT_CODE", "LOCAL");
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localBroadcastIntent);
        }
        // Check whether user requested service to be shutdown
        if (!isRouteTrackingOn()) {
            Log.d(TAG, "SERVICE HAS BEEN STOPPED 1");
            isServiceRunning = false;
            Log.d(TAG, "SERVICE STOPPED " + isServiceRunning);
//            Intent dialogIntent = new Intent(this, RecordResultActivity.class);
//            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            this.startActivity(dialogIntent);
            this.stopSelf();
        }
    }

    private boolean isRouteTrackingOn() {
        return customSharedPreference.isServiceRunningState();
    }

    @Override
    public void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

}
