package com.jo.ayo.ayojo.ui.post;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jo.ayo.ayojo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HouseOwnerDescActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnNextToStepTwo)
    void doStepTwo() {
        Intent intent = new Intent(getApplicationContext(), FirstQuestionActivity.class);
        startActivity(intent);
    }
}
