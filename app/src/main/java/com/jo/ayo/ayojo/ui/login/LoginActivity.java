package com.jo.ayo.ayojo.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.ybq.android.spinkit.style.Wave;
import com.jo.ayo.ayojo.data.model.Token;
import com.jo.ayo.ayojo.data.model.user.UserDataResult;
import com.jo.ayo.ayojo.data.pref.PrefManager;
import com.jo.ayo.ayojo.nework.MyRetrofitClient;
import com.jo.ayo.ayojo.nework.api.ApiLogin;
import com.jo.ayo.ayojo.ui.main.MainActivity;
import com.jo.ayo.ayojo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etUsername)
    EditText etUsername;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.imgShowPassword)
    ImageView imgShowPassword;

    @BindView(R.id.lyLogin)
    LinearLayout lyLogin;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Wave wave = new Wave();
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setIndeterminateDrawable(wave);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
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
        login();
    }

    private void login() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progressbar_spinkit);
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        ApiLogin service = MyRetrofitClient.createService(ApiLogin.class);
        Call<UserDataResult> call = service.login(username, password);
        call.enqueue(new Callback<UserDataResult>() {
            @Override
            public void onResponse(Call<UserDataResult> call, Response<UserDataResult> response) {
                String code = String.valueOf(response.code());

                if (code.equals("200")) {
                    String tokenData = response.headers().get("authorization");
                    String[] splited = tokenData.split("\\s+");
                    String token = "Bearer " + splited[1].trim();

                    Token tokenObject = new Token();
                    tokenObject.setToken(token);

                    PrefManager.setToken(getApplicationContext(), tokenObject);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    progressDialog.dismiss();
                } else if (code.equals("401")) {
                    progressDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(lyLogin, "Username atau password salah, silahkan coba lagi", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (code.equals("400")) {
                    progressDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(lyLogin, "Username atau password tidak boleh kosong, silahkan coba lagi.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    progressDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(lyLogin, "Sedang ada gangguan server, silahkan coba lagi.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<UserDataResult> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar snackbar = Snackbar.make(lyLogin, "Anda tidak tersambung dengan internet", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }
}
