package com.jo.ayo.ayojo.ui.post;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jo.ayo.ayojo.R;
import com.jo.ayo.ayojo.data.model.PostData;
import com.jo.ayo.ayojo.data.pref.PrefManager;
import com.jo.ayo.ayojo.ui.main.MainActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewPostActivity extends AppCompatActivity {

    @BindView(R.id.imgPreview)
    ImageView imgPreview;

    @BindView(R.id.tvHouseOwner)
    TextView tvHouseOwner;

    @BindView(R.id.tvFirstAnswer)
    TextView tvFirstAnswer;

    @BindView(R.id.tvSecondAnswer)
    TextView tvSecondAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_post);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Review laporan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Camera/photo.jpg");
        imgPreview.setImageURI(uri);

        PostData postData = PrefManager.getPostData(getApplicationContext());
        String houseOwner = postData.getHouseOwner();
        float lat = postData.getLat();
        float lng = postData.getLng();
        String firstAnswer = postData.getFirstAnsewr();
        String secondAnswer = postData.getSecondAnswer();

        tvHouseOwner.setText(houseOwner);
        tvFirstAnswer.setText(firstAnswer);
        tvSecondAnswer.setText(secondAnswer);

        Toast.makeText(this, "lat:" + lat + " lng: " + lng, Toast.LENGTH_SHORT).show();
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
}
