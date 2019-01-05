package com.jo.ayo.ayojo.ui.post;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.camerakit.CameraKitView;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.Wave;
import com.jo.ayo.ayojo.R;
import com.jo.ayo.ayojo.ui.main.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;

public class TakePictureActivity extends AppCompatActivity {

    private CameraKitView cameraKitView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Ambil gambar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cameraKitView = findViewById(R.id.camera);
        cameraKitView.setPermissions(CameraKitView.PERMISSION_STORAGE);

        Wave wave = new Wave();
        progressDialog = new ProgressDialog(TakePictureActivity.this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setIndeterminateDrawable(wave);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
    }

    @OnTouch(R.id.btnCapture)
    boolean onTouchCapture(View view, MotionEvent motionEvent) {
        handleViewTouchFeedback(view, motionEvent);

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP: {
                progressDialog.show();
                progressDialog.setContentView(R.layout.progressbar_spinkit);
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, byte[] capturedImage) {
                        File savePhoto = new File(Environment.getExternalStorageDirectory(), "/Camera/photo.jpg");
                        try {
                            FileOutputStream outputStream = new FileOutputStream(savePhoto.getPath());
                            outputStream.write(capturedImage);
                            outputStream.close();
                            Intent intent = new Intent(getApplicationContext(), ReviewPostActivity.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            }
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), SecondQuestionActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), SecondQuestionActivity.class);
        startActivity(intent);
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
