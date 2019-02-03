package com.jo.ayo.ayojo.ui.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.jo.ayo.ayojo.R;
import com.jo.ayo.ayojo.data.adapter.ReportAdapter;
import com.jo.ayo.ayojo.data.model.post.list.ReportDataResult;
import com.jo.ayo.ayojo.data.model.Token;
import com.jo.ayo.ayojo.data.pref.PrefManager;
import com.jo.ayo.ayojo.nework.MyRetrofitClient;
import com.jo.ayo.ayojo.nework.api.ApiPostList;
import com.jo.ayo.ayojo.ui.login.LoginActivity;
import com.jo.ayo.ayojo.ui.post.HouseOwnerDescActivity;
import com.jo.ayo.ayojo.ui.post.TakePictureActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;

    private String cursor;

    @BindView(R.id.fabCreatePost)
    FloatingActionButton fabCreatePost;

    @BindView(R.id.rvPost)
    RecyclerView rvPost;

    @BindView(R.id.lyMain)
    RelativeLayout lyMain;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.lyEmptyData)
    LinearLayout lyEmptyData;

    private ReportAdapter adapter;
    private LinearLayoutManager layoutManager;

    ProgressBar progressDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rekap Laporan");

        fabCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TakePictureActivity.class);
                startActivity(intent);
            }
        });

        ThreeBounce bounce = new ThreeBounce();
        progressDialog = this.findViewById(R.id.main_loading);
        progressDialog.setIndeterminateDrawable(bounce);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(() -> getPostList());

        getPostList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                getPostList();
                return true;
            case R.id.item2:
                getPostListFiltered("Day");
                return true;
            case R.id.item3:
                getPostListFiltered("Week");
                return true;
            case R.id.item4:
                getPostListFiltered("Month");
                return true;
            case R.id.item5:
                Token tokenObject = new Token();
                tokenObject.setToken("");
                PrefManager.setToken(getApplicationContext(), tokenObject);
                finish();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
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

    private void getPostListFiltered(String filter) {
        progressDialog.setVisibility(View.VISIBLE);
        Token tokenModel = PrefManager.getTokenData(getApplicationContext());
        String token = tokenModel.getToken();

        ApiPostList service = MyRetrofitClient.createService(ApiPostList.class);
        Call<ReportDataResult> call = service.getPostFiltered(token,
                30,
                "id",
                "DESC",
                filter);
        call.enqueue(new Callback<ReportDataResult>() {
            @Override
            public void onResponse(Call<ReportDataResult> call, Response<ReportDataResult> response) {

                String code = String.valueOf(response.code());

                if (code.equals("200")) {
                    progressDialog.setVisibility(View.GONE);
                    ReportDataResult result = response.body();
                    final int rowSize = result.getData().getRows().size();
                    //cursor = result.getData().getRows().get(rowSize - 1).getId();

                    adapter = new ReportAdapter();
                    adapter.setReportList(result.getData().getRows());
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    rvPost.setLayoutManager(layoutManager);
                    rvPost.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                } else if (code.equals("401")) {
                    progressDialog.setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else if (code.equals("400")) {
                    progressDialog.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(lyMain, "Username atau password tidak boleh kosong, silahkan coba lagi.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    progressDialog.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(lyMain, "Sedang ada gangguan server, silahkan coba lagi.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }

            @Override
            public void onFailure(Call<ReportDataResult> call, Throwable t) {
                progressDialog.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(lyMain, "Anda tidak terhubung dengan internet, Silahkan coba lagi", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void getPostList() {
        progressDialog.setVisibility(View.VISIBLE);
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
                    progressDialog.setVisibility(View.GONE);
                    ReportDataResult result = response.body();
                    final int rowSize = result.getData().getRows().size();
                    //cursor = result.getData().getRows().get(rowSize - 1).getId();

                    if (result.getData().getRows().isEmpty()) {
                        lyEmptyData.setVisibility(View.VISIBLE);
                        rvPost.setVisibility(View.GONE);
                    } else {
                        lyEmptyData.setVisibility(View.GONE);
                        rvPost.setVisibility(View.VISIBLE);
                        adapter = new ReportAdapter();
                        adapter.setReportList(result.getData().getRows());
                        layoutManager = new LinearLayoutManager(getApplicationContext());
                        rvPost.setLayoutManager(layoutManager);
                        rvPost.setAdapter(adapter);
                    }
                    swipeRefreshLayout.setRefreshing(false);
                } else if (code.equals("401")) {
                    progressDialog.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else if (code.equals("400")) {
                    swipeRefreshLayout.setRefreshing(false);
                    progressDialog.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(lyMain, "Username atau password tidak boleh kosong, silahkan coba lagi.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    progressDialog.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(lyMain, "Sedang ada gangguan server, silahkan coba lagi.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ReportDataResult> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                progressDialog.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(lyMain, "Sedang ada gangguan server, silahkan coba lagi.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

    }

}
