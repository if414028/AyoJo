package com.jo.ayo.ayojo.ui.main;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jo.ayo.ayojo.R;
import com.jo.ayo.ayojo.ui.post.HouseOwnerDescActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fabCreatePost)
    FloatingActionButton fabCreatePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fabCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HouseOwnerDescActivity.class);
                startActivity(intent);
            }
        });
    }
}
