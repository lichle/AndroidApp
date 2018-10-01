package mobile.lichle.com.trackme.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import mobile.lichle.com.trackme.R;
import mobile.lichle.com.trackme.utils.DeviceUtils;

public class MainActivity extends BaseActivity implements IFloatButtonVisibility {

    public static final String TAG = "MainActivity";

    public static final int PERMISSIONS_REQUEST_ACCESS_LOCATION = 100;
    public static final int REQUEST_LOCATION = 101;

    private View mMainLayout;
    private GoogleApiClient mGoogleApiClient;

    private FloatingActionButton mFloatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainLayout = findViewById(R.id.activity_main_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TrackListFragment is default view;
        TrackListFragment trackListFragment = new TrackListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content_main, trackListFragment);
        transaction.addToBackStack("TrackListFragment");
        transaction.commit();

        mFloatingButton = findViewById(R.id.fab);
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAccessLocationPermission();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFloatingButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {// Press Back key from Main fragment
            finish();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 2) {// Press Back key from other fragment to Main fragment
                setTitle(getString(R.string.app_name));
                mFloatingButton.setVisibility(View.VISIBLE);
            }
            super.onBackPressed();
        }
    }

    public void checkGPS() {
        boolean deviceHasGps = DeviceUtils.isDeviceHasGPS(MainActivity.this.getApplicationContext());
        if (!deviceHasGps) {
            String message = getString(R.string.device_not_support_gps);
            showMessage(mMainLayout, message, Snackbar.LENGTH_SHORT);
            return;
        }
        boolean isGPSEnable = DeviceUtils.isGPSEnable(MainActivity.this.getApplicationContext());
        if (!isGPSEnable) {
            requestEnableGPS();
        } else {
            showMapFragment();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null != mGoogleApiClient && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    checkGPS();
                } else {
                    // permission denied
                    String message = getString(R.string.access_location_is_not_granted_warning);
                    showMessage(mMainLayout, message, Snackbar.LENGTH_SHORT);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // GPS is enabled
                        showMapFragment();
                        break;
                    case Activity.RESULT_CANCELED:
                        // GPS is disable
                        String message = getString(R.string.device_not_enable_gps_warning);
                        showMessage(mMainLayout, message, Snackbar.LENGTH_SHORT);
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private void requestAccessLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_LOCATION);

        } else {
            // Permission has already been granted, continue check GPS.
            Log.d(TAG, "Location permission is granted");
            checkGPS();
        }
    }

    public void showMapFragment() {
        MapFragment mapFragment = new MapFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, mapFragment);
        transaction.addToBackStack("MapFragment");
        transaction.commit();
    }


    private void requestEnableGPS() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            mGoogleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("TrackLocation error", "TrackLocation error " + connectionResult.getErrorCode());
                        }
                    }).build();
            mGoogleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            });
        }
    }


    @Override
    public void setFloatButtonVisibility(boolean visible) {
        if (visible) {
            mFloatingButton.setVisibility(View.VISIBLE);
        } else {
            mFloatingButton.setVisibility(View.INVISIBLE);
        }
    }
}
