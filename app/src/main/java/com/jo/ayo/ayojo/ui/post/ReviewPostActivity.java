package com.jo.ayo.ayojo.ui.post;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Wave;
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
import com.jo.ayo.ayojo.data.model.post.create.ImageUpload;
import com.jo.ayo.ayojo.data.model.pref.PostData;
import com.jo.ayo.ayojo.data.model.post.create.PostDataResult;
import com.jo.ayo.ayojo.data.model.Token;
import com.jo.ayo.ayojo.data.pref.PrefManager;
import com.jo.ayo.ayojo.nework.MyRetrofitClient;
import com.jo.ayo.ayojo.nework.api.ApiCreatePost;
import com.jo.ayo.ayojo.nework.api.ApiImageUpload;
import com.jo.ayo.ayojo.ui.login.LoginActivity;
import com.jo.ayo.ayojo.ui.main.MainActivity;
import com.jo.ayo.ayojo.utils.ResultHolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Headers;

public class ReviewPostActivity extends AppCompatActivity {

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

    private static final String TAG = ReviewPostActivity.class.getSimpleName();

    @BindView(R.id.imgPreview)
    ImageView imgPreview;

    @BindView(R.id.lyPostPreview)
    LinearLayout lyPostPreview;

    private String imageUrl;

    private String houseOwner, firstAnswer, secondAnswer, work, address, age, sex;
    private String lat, lng;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_post);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Review laporan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
        imgPreview.setImageURI(uri);

        initLocationUpdate(this, getApplicationContext());
        requestLocationPermission();

        Wave wave = new Wave();
        progressDialog = new ProgressDialog(ReviewPostActivity.this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setIndeterminateDrawable(wave);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
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
                    startLocationUpdate(ReviewPostActivity.this, getApplicationContext());

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }
        }
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(ReviewPostActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            //showPermissionDialog();

            if (ActivityCompat.shouldShowRequestPermissionRationale(ReviewPostActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(ReviewPostActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            mRequestingLocationUpdates = true;
            startLocationUpdate(ReviewPostActivity.this, getApplicationContext());
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

                lat = String.valueOf(mCurrentLocation.getLatitude());
                lng = String.valueOf(mCurrentLocation.getLongitude());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), TakePictureActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @OnClick(R.id.btnSubmit)
    void doPost() {
        uploadImage();
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private File createTempFile(Bitmap bitmap) {
        //write the bytes in file
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + "_image.jpg");
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapData = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private void uploadImage() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progressbar_spinkit);

        Bitmap image = ResultHolder.getImage();
        File file = createTempFile(image);

        RequestBody imageRequest = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part imageBody = MultipartBody.Part.createFormData("file", file.getName(), imageRequest);

        ApiImageUpload service = MyRetrofitClient.createService(ApiImageUpload.class);
        Call<ImageUpload> call = service.uploadImage(imageBody);
        call.enqueue(new Callback<ImageUpload>() {
            @Override
            public void onResponse(Call<ImageUpload> call, Response<ImageUpload> response) {
                String code = String.valueOf(response.code());
                Toast.makeText(ReviewPostActivity.this, "code: " + code, Toast.LENGTH_SHORT).show();

                if (code.equals("200")) {
                    imageUrl = response.body().getResult();
                    createPost();
                } else if (code.equals("401")) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Snackbar snackbar = Snackbar.make(lyPostPreview, "Sedang ada gangguan server, silahkan coba lagi", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ImageUpload> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar snackbar = Snackbar.make(lyPostPreview, "Gagal", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void createPost() {

        Token tokenModel = PrefManager.getTokenData(getApplicationContext());
        String token = tokenModel.getToken();

        ApiCreatePost service = MyRetrofitClient.createService(ApiCreatePost.class);
        Call<PostDataResult> call = service.createPost(
                token,
                lat,
                lng,
                imageUrl
        );
        call.enqueue(new Callback<PostDataResult>() {
            @Override
            public void onResponse(Call<PostDataResult> call, Response<PostDataResult> response) {
                String code = String.valueOf(response.code());

                if (code.equals("200")) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    progressDialog.dismiss();
                    finish();
                } else if (code.equals("401")) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else if (code.equals("400")) {
                    progressDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(lyPostPreview, "Tidak boleh ada kolom yang kosong, silahkan coba lagi", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    progressDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(lyPostPreview, "Gagal mengirim data, silahkan coba lagi", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<PostDataResult> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar snackbar = Snackbar.make(lyPostPreview, "Gagal mengirim data", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }
}
