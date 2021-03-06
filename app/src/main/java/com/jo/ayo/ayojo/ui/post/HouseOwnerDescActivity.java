package com.jo.ayo.ayojo.ui.post;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.jo.ayo.ayojo.BuildConfig;
import com.jo.ayo.ayojo.R;
import com.jo.ayo.ayojo.data.model.pref.PostData;
import com.jo.ayo.ayojo.data.pref.PrefManager;
import com.jo.ayo.ayojo.ui.main.MainActivity;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

public class HouseOwnerDescActivity extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private Boolean mRequestingLocationUpdates;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final String TAG = HouseOwnerDescActivity.class.getSimpleName();

    private float lat, lng;

    private String sex = "PRIA";

    @BindView(R.id.etHouseOwner)
    EditText etHouseOwner;

    @BindView(R.id.etPekerjaan)
    EditText etWork;

    @BindView(R.id.etAlamat)
    EditText etAddress;

    @BindView(R.id.etUsia)
    EditText etAge;

    @BindView(R.id.btnMale)
    RelativeLayout btnMale;

    @BindView(R.id.btnFemale)
    RelativeLayout btnFemale;

    @BindView(R.id.txtMale)
    TextView txtMale;

    @BindView(R.id.txtFemale)
    TextView txtFemale;

    @BindView(R.id.btnNextToStepTwo)
    Button btnNextStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        ButterKnife.bind(this);

        initLocationUpdate(this, getApplicationContext());
        requestLocationPermission();

        getSupportActionBar().setTitle("Informasi pemilik rumah");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnTouch(R.id.btnMale)
    boolean chooseMale(View view, MotionEvent motionEvent) {
        handleViewTouchFeedback(view, motionEvent);
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP: {
                sex = "PRIA";
                btnMale.setBackgroundResource(R.drawable.bg_blue_rounded);
                txtMale.setTextColor(getResources().getColor(R.color.colorWhite));

                btnFemale.setBackgroundResource(R.drawable.bg_grey_rounded);
                txtFemale.setTextColor(getResources().getColor(R.color.colorText));
                break;
            }
        }
        return true;
    }

    @OnTextChanged({R.id.etHouseOwner, R.id.etPekerjaan, R.id.etAlamat, R.id.etUsia})
    void formValidation() {
        String houseOwner = etHouseOwner.getText().toString();
        String pekerjaan = etWork.getText().toString();
        String alamat = etAddress.getText().toString();
        String usia = etAge.getText().toString();

        if (houseOwner.equals("") || pekerjaan.equals("") || alamat.equals("") || usia.equals("")) {
            btnNextStep.setEnabled(false);
            btnNextStep.setBackgroundResource(R.drawable.bt_rounded_disable);
            btnNextStep.setTextColor(getResources().getColor(R.color.colorWhite));
        } else {
            btnNextStep.setEnabled(true);
            btnNextStep.setBackgroundResource(R.drawable.bt_rounded);
            btnNextStep.setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    @OnTouch(R.id.btnFemale)
    boolean chooseFemale(View view, MotionEvent motionEvent) {
        handleViewTouchFeedback(view, motionEvent);
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP: {
                sex = "WANITA";
                btnMale.setBackgroundResource(R.drawable.bg_grey_rounded);
                txtMale.setTextColor(getResources().getColor(R.color.colorText));

                btnFemale.setBackgroundResource(R.drawable.bg_blue_rounded);
                txtFemale.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
            }
        }
        return true;
    }

    @OnClick(R.id.btnNextToStepTwo)
    void doStepTwo() {
        String houseOwner = etHouseOwner.getText().toString();
        String work = etWork.getText().toString();
        String address = etAddress.getText().toString();
        String age = etAge.getText().toString();

        PostData postData = new PostData();
        postData.setHouseOwner(houseOwner);
        postData.setLat(lat);
        postData.setLng(lng);
        postData.setWork(work);
        postData.setAddress(address);
        postData.setAge(age);
        postData.setSex(sex);

        PrefManager.setHouseOwner(getApplicationContext(), postData);

        Intent intent = new Intent(getApplicationContext(), FirstQuestionActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    mRequestingLocationUpdates = true;
                    startLocationUpdate(HouseOwnerDescActivity.this, getApplicationContext());

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }
        }
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(HouseOwnerDescActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            //showPermissionDialog();

            if (ActivityCompat.shouldShowRequestPermissionRationale(HouseOwnerDescActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(HouseOwnerDescActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            mRequestingLocationUpdates = true;
            startLocationUpdate(HouseOwnerDescActivity.this, getApplicationContext());
        }
    }

    public void initLocationUpdate(Activity activity, Context context) {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        mSettingsClient = LocationServices.getSettingsClient(activity);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                lat = (float) mCurrentLocation.getLatitude();
                lng = (float) mCurrentLocation.getLongitude();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();

    }

    public void openSettings(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public void startLocationUpdate(Activity activity, Context context) {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener(activity, e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings ");
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sie) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Log.e(TAG, errorMessage);

                            //Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    boolean handleViewTouchFeedback(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                touchDownAnimation(view);
                return true;
            }

            case MotionEvent.ACTION_UP: {
                touchUpAnimation(view);
                return true;
            }

            default: {
                return true;
            }
        }
    }

    void touchDownAnimation(View view) {
        view.animate()
                .scaleX(0.88f)
                .scaleY(0.88f)
                .setDuration(300)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }

    void touchUpAnimation(View view) {
        view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(300)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }

    void changeViewImageResource(final ImageView imageView, @DrawableRes final int resId) {
        imageView.setRotation(0);
        imageView.animate()
                .rotationBy(360)
                .setDuration(400)
                .setInterpolator(new OvershootInterpolator())
                .start();

        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(resId);
            }
        }, 120);
    }
}
