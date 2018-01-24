Greenport yourself
============

Android app for tracking users movements and aggregating user statistics.

Download
--------

1. Install Android Studio.

2. Git clone this project and open it Android Studio.

3. Use either emulated device or a real device to run the code (USB debugging has to be enabled).

4. Generate Google API key [here](https://developers.google.com/maps/documentation/javascript/get-api-key#step-1-get-an-api-key-from-the-google-api-console).

5. Create file `src/debug/res/google_maps_api.xml` with the following content

```xml
<resources>
    <string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">
         API_KEY
    </string>
</resources>
```

where API_KEY is the API key obtained in Step 4.

Notes
--------

Carbon footprint is currently calculated based on foot movements instead of vehicle for demo purposes and monthly report is hardcoded.
