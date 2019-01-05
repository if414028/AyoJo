package com.jo.ayo.ayojo.ui.post;

import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jo.ayo.ayojo.R;
import com.jo.ayo.ayojo.data.model.pref.PostData;
import com.jo.ayo.ayojo.data.pref.PrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class FirstQuestionActivity extends AppCompatActivity {

    @BindView(R.id.btnOptionOne)
    RelativeLayout btnOptionOne;

    @BindView(R.id.btnOptionTwo)
    RelativeLayout btnOptionTwo;

    @BindView(R.id.btnOptionThree)
    RelativeLayout btnOptionThree;

    @BindView(R.id.btnOptionFour)
    RelativeLayout btnOptionFour;

    @BindView(R.id.btnOptionFive)
    RelativeLayout btnOptionFive;

    @BindView(R.id.txtOptionOne)
    TextView txtOptionOne;

    @BindView(R.id.txtOptionTwo)
    TextView txtOptionTwo;

    @BindView(R.id.txtOptionThree)
    TextView txtOptionThree;

    @BindView(R.id.txtOptionFour)
    TextView txtOptionFour;

    @BindView(R.id.txtOptionFive)
    TextView txtOptionFive;

    private String answer = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_question);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Pertanyaan pertama");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnTouch(R.id.btnOptionOne)
    boolean chooseOptionOne(View view, MotionEvent motionEvent) {
        handleViewTouchFeedback(view, motionEvent);
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP: {
                answer = "1";
                btnOptionOne.setBackgroundResource(R.drawable.bg_blue_rounded);
                btnOptionTwo.setBackgroundResource(R.drawable.bg_grey_rounded);
                btnOptionThree.setBackgroundResource(R.drawable.bg_grey_rounded);
                btnOptionFour.setBackgroundResource(R.drawable.bg_grey_rounded);
                btnOptionFive.setBackgroundResource(R.drawable.bg_grey_rounded);

                txtOptionOne.setTextColor(getResources().getColor(R.color.colorWhite));
                txtOptionTwo.setTextColor(getResources().getColor(R.color.colorText));
                txtOptionThree.setTextColor(getResources().getColor(R.color.colorText));
                txtOptionFour.setTextColor(getResources().getColor(R.color.colorText));
                txtOptionFive.setTextColor(getResources().getColor(R.color.colorText));
                break;
            }
        }
        return true;
    }

    @OnTouch(R.id.btnOptionTwo)
    boolean chooseOptionTwo(View view, MotionEvent motionEvent) {
        handleViewTouchFeedback(view, motionEvent);
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP: {
                answer = "2";
                btnOptionTwo.setBackgroundResource(R.drawable.bg_blue_rounded);
                btnOptionOne.setBackgroundResource(R.drawable.bg_grey_rounded);
                btnOptionThree.setBackgroundResource(R.drawable.bg_grey_rounded);
                btnOptionFour.setBackgroundResource(R.drawable.bg_grey_rounded);
                btnOptionFive.setBackgroundResource(R.drawable.bg_grey_rounded);

                txtOptionTwo.setTextColor(getResources().getColor(R.color.colorWhite));
                txtOptionOne.setTextColor(getResources().getColor(R.color.colorText));
                txtOptionThree.setTextColor(getResources().getColor(R.color.colorText));
                txtOptionFour.setTextColor(getResources().getColor(R.color.colorText));
                txtOptionFive.setTextColor(getResources().getColor(R.color.colorText));
                break;
            }
        }
        return true;
    }

    @OnTouch(R.id.btnOptionThree)
    boolean chooseOptionThree(View view, MotionEvent motionEvent) {
        handleViewTouchFeedback(view, motionEvent);
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP: {
                answer = "3";
                btnOptionTwo.setBackgroundResource(R.drawable.bg_grey_rounded);
                btnOptionOne.setBackgroundResource(R.drawable.bg_grey_rounded);
                btnOptionThree.setBackgroundResource(R.drawable.bg_blue_rounded);
                btnOptionFour.setBackgroundResource(R.drawable.bg_grey_rounded);
                btnOptionFive.setBackgroundResource(R.drawable.bg_grey_rounded);

                txtOptionTwo.setTextColor(getResources().getColor(R.color.colorText));
                txtOptionOne.setTextColor(getResources().getColor(R.color.colorText));
                txtOptionThree.setTextColor(getResources().getColor(R.color.colorWhite));
                txtOptionFour.setTextColor(getResources().getColor(R.color.colorText));
                txtOptionFive.setTextColor(getResources().getColor(R.color.colorText));
                break;
            }
        }
        return true;
    }

    @OnTouch(R.id.btnOptionFour)
    boolean chooseOptionFour(View view, MotionEvent motionEvent) {
        handleViewTouchFeedback(view, motionEvent);
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP: {
                answer = "4";
                btnOptionTwo.setBackgroundResource(R.drawable.bg_grey_rounded);
                btnOptionOne.setBackgroundResource(R.drawable.bg_grey_rounded);
                btnOptionThree.setBackgroundResource(R.drawable.bg_grey_rounded);
                btnOptionFour.setBackgroundResource(R.drawable.bg_blue_rounded);
                btnOptionFive.setBackgroundResource(R.drawable.bg_grey_rounded);

                txtOptionTwo.setTextColor(getResources().getColor(R.color.colorText));
                txtOptionOne.setTextColor(getResources().getColor(R.color.colorText));
                txtOptionThree.setTextColor(getResources().getColor(R.color.colorText));
                txtOptionFour.setTextColor(getResources().getColor(R.color.colorWhite));
                txtOptionFive.setTextColor(getResources().getColor(R.color.colorText));
                break;
            }
        }
        return true;
    }

    @OnTouch(R.id.btnOptionFive)
    boolean chooseOptionFive(View view, MotionEvent motionEvent) {
        handleViewTouchFeedback(view, motionEvent);
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP: {
                answer = "5";
                btnOptionTwo.setBackgroundResource(R.drawable.bg_grey_rounded);
                btnOptionOne.setBackgroundResource(R.drawable.bg_grey_rounded);
                btnOptionThree.setBackgroundResource(R.drawable.bg_grey_rounded);
                btnOptionFour.setBackgroundResource(R.drawable.bg_grey_rounded);
                btnOptionFive.setBackgroundResource(R.drawable.bg_blue_rounded);

                txtOptionTwo.setTextColor(getResources().getColor(R.color.colorText));
                txtOptionOne.setTextColor(getResources().getColor(R.color.colorText));
                txtOptionThree.setTextColor(getResources().getColor(R.color.colorText));
                txtOptionFour.setTextColor(getResources().getColor(R.color.colorText));
                txtOptionFive.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
            }
        }
        return true;
    }

    @OnClick(R.id.btnNextToStepThree)
    void doStepThree() {
        PostData postData = new PostData();
        postData.setFirstAnsewr(answer);

        PrefManager.setFirstanswer(getApplicationContext(), postData);

        Intent intent = new Intent(getApplicationContext(), SecondQuestionActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), HouseOwnerDescActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), HouseOwnerDescActivity.class);
        startActivity(intent);
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
