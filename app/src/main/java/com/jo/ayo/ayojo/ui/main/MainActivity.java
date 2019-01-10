package com.jo.ayo.ayojo.ui.main;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jo.ayo.ayojo.R;
import com.jo.ayo.ayojo.data.adapter.ReportAdapter;
import com.jo.ayo.ayojo.data.model.post.list.ReportDataResult;
import com.jo.ayo.ayojo.data.model.Token;
import com.jo.ayo.ayojo.data.pref.PrefManager;
import com.jo.ayo.ayojo.nework.MyRetrofitClient;
import com.jo.ayo.ayojo.nework.api.ApiPostList;
import com.jo.ayo.ayojo.ui.login.LoginActivity;
import com.jo.ayo.ayojo.ui.post.HouseOwnerDescActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;

    private String cursor;

    private ReportAdapter adapter;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.fabCreatePost)
    FloatingActionButton fabCreatePost;

    @BindView(R.id.rvPost)
    RecyclerView rvPost;

    @BindView(R.id.lyMain)
    RelativeLayout lyMain;

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

        getPostList();
    }

    private void getPostList() {

        Token tokenModel = PrefManager.getTokenData(getApplicationContext());
        String token = tokenModel.getToken();

        ApiPostList service = MyRetrofitClient.createService(ApiPostList.class);
        Call<ReportDataResult> call = service.getPostLis(token,
                30,
                "id",
                "DESC");
        call.enqueue(new Callback<ReportDataResult>() {
            @Override
            public void onResponse(Call<ReportDataResult> call, Response<ReportDataResult> response) {

                String code = String.valueOf(response.code());

                if (code.equals("200")) {
                    ReportDataResult result = response.body();
                    final int rowSize = result.getData().getRows().size();
                    //cursor = result.getData().getRows().get(rowSize - 1).getId();

                    adapter = new ReportAdapter();
                    adapter.setReportList(result.getData().getRows());
                    layoutManager = new LinearLayoutManager(MainActivity.this);
                    rvPost.setLayoutManager(layoutManager);
                    rvPost.setAdapter(adapter);
                } else if (code.equals("401")) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else if (code.equals("400")) {
                    Snackbar snackbar = Snackbar.make(lyMain, "Username atau password tidak boleh kosong, silahkan coba lagi.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(lyMain, "Sedang ada gangguan server, silahkan coba lagi.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }


            }

            @Override
            public void onFailure(Call<ReportDataResult> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

}
