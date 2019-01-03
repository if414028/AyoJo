package com.jo.ayo.ayojo.ui.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.jo.ayo.ayojo.ui.main.MainActivity;
import com.jo.ayo.ayojo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etUsername)
    EditText etUsername;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.imgShowPassword)
    ImageView imgShowPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @OnTextChanged(R.id.etPassword)
    void onTextChanged() {
        String password = etPassword.getText().toString();
        if (!password.equalsIgnoreCase("")) {
            imgShowPassword.setVisibility(View.VISIBLE);
        } else {
            imgShowPassword.setVisibility(View.GONE);
        }
    }

    @OnTouch(R.id.imgShowPassword)
    boolean onTouch(View v, MotionEvent motionEvent) {
        String password = etPassword.getText().toString();
        final boolean isOutsideView = motionEvent.getX() < 0 ||
                motionEvent.getX() > v.getWidth() ||
                motionEvent.getY() < 0 ||
                motionEvent.getY() > v.getHeight();

        // change input type will reset cursor position, so we want to save it
        final int cursor = etPassword.getSelectionStart();

        if (isOutsideView || MotionEvent.ACTION_UP == motionEvent.getAction()) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }

        etPassword.setSelection(cursor);
        return true;
    }

    @OnClick(R.id.btnLogin)
    void doLogin() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
