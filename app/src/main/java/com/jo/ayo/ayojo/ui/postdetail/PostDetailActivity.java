package com.jo.ayo.ayojo.ui.postdetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jo.ayo.ayojo.R;

public class PostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        getPostDetail();
    }

    private void getPostDetail() {

        Bundle intent = getIntent().getExtras();
        String id;
        if (intent == null) {
            id = "0";
        } else {
            id = intent.getString("ID");
        }

        Toast.makeText(this, "id: " + id, Toast.LENGTH_SHORT).show();
    }
}
