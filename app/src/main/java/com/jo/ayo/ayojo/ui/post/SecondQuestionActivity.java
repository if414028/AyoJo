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
import com.jo.ayo.ayojo.data.model.PostData;
import com.jo.ayo.ayojo.data.pref.PrefManager;
import com.jo.ayo.ayojo.ui.main.MainActivity;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class SecondQuestionActivity extends AppCompatActivity {

    @BindView(R.id.btnOptionOneSecondQuestion)
    RelativeLayout btnOptionOne;

    @BindView(R.id.btnOptionTwoSecondQuestion)
    RelativeLayout btnOptionTwo;

    @BindView(R.id.btnOptionThreeSecondQuestion)
    RelativeLayout btnOptionThree;

    @BindView(R.id.btnOptionFourSecondQuestion)
    RelativeLayout btnOptionFour;

    @BindView(R.id.btnOptionFiveSecondQuestion)
    RelativeLayout btnOptionFive;

    @BindView(R.id.txtOptionOneSecondQuestion)
    TextView txtOptionOne;

    @BindView(R.id.txtOptionTwoSecondQuestion)
    TextView txtOptionTwo;

    @BindView(R.id.txtOptionThreeSecondQuestion)
    TextView txtOptionThree;

    @BindView(R.id.txtOptionFourSecondQuestion)
    TextView txtOptionFour;

    @BindView(R.id.txtOptionFiveSecondQuestion)
    TextView txtOptionFive;

    String secondAnswer = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_question);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Pertanyaan kedua");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @OnClick(R.id.btnNextToStepFour)
    void doStepFour() {
        PostData postData = new PostData();
        postData.setSecondAnswer(secondAnswer);

        PrefManager.setSecondanswerkey(getApplicationContext(), postData);

        Intent intent = new Intent(getApplicationContext(), TakePictureActivity.class);
        startActivity(intent);
    }

    @OnTouch(R.id.btnOptionOneSecondQuestion)
    boolean chooseOptionOne(View view, MotionEvent motionEvent) {
        handleViewTouchFeedback(view, motionEvent);
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP: {
                secondAnswer = "1";
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

    @OnTouch(R.id.btnOptionTwoSecondQuestion)
    boolean chooseOptionTWo(View view, MotionEvent motionEvent) {
        handleViewTouchFeedback(view, motionEvent);
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP: {
                secondAnswer = "2";
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

    @OnTouch(R.id.btnOptionThreeSecondQuestion)
    boolean chooseOptionThree(View view, MotionEvent motionEvent) {
        handleViewTouchFeedback(view, motionEvent);
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP: {
                secondAnswer = "3";
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

    @OnTouch(R.id.btnOptionFourSecondQuestion)
    boolean chooseOptionFour(View view, MotionEvent motionEvent) {
        handleViewTouchFeedback(view, motionEvent);
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP: {
                secondAnswer = "4";
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

    @OnTouch(R.id.btnOptionFiveSecondQuestion)
    boolean chooseOptionFive(View view, MotionEvent motionEvent) {
        handleViewTouchFeedback(view, motionEvent);
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP: {
                secondAnswer = "5";
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), FirstQuestionActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), FirstQuestionActivity.class);
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
