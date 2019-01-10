package com.jo.ayo.ayojo.ui.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jo.ayo.ayojo.R;
import com.jo.ayo.ayojo.data.model.Token;
import com.jo.ayo.ayojo.data.model.user.UserCheckLogin;
import com.jo.ayo.ayojo.data.pref.PrefManager;
import com.jo.ayo.ayojo.nework.MyRetrofitClient;
import com.jo.ayo.ayojo.nework.api.ApiCheckLogin;
import com.jo.ayo.ayojo.ui.login.LoginActivity;
import com.jo.ayo.ayojo.ui.main.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLogin();
            }
        }, 1000);
    }

    private void checkLogin() {
        Token tokenModel = PrefManager.getTokenData(getApplicationContext());
        String token = tokenModel.getToken();
        String[] splitToken = token.split("\\s+");
        String tokenResult = splitToken[1].trim();

        ApiCheckLogin service = MyRetrofitClient.createService(ApiCheckLogin.class);
        Call<UserCheckLogin> call = service.checkLogin(tokenResult);
        call.enqueue(new Callback<UserCheckLogin>() {
            @Override
            public void onResponse(Call<UserCheckLogin> call, Response<UserCheckLogin> response) {
                String statusLogin = String.valueOf(response.code());
                if (statusLogin.equals("200")) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<UserCheckLogin> call, Throwable t) {

            }
        });
    }
}
