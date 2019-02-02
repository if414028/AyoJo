package com.jo.ayo.ayojo.ui.postdetail;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jo.ayo.ayojo.R;
import com.jo.ayo.ayojo.data.model.Token;
import com.jo.ayo.ayojo.data.model.post.detail.ReportDataDetailResult;
import com.jo.ayo.ayojo.data.pref.PrefManager;
import com.jo.ayo.ayojo.nework.MyRetrofitClient;
import com.jo.ayo.ayojo.nework.api.ApiPostDetail;
import com.jo.ayo.ayojo.ui.login.LoginActivity;
import com.jo.ayo.ayojo.ui.main.MainActivity;
import com.jo.ayo.ayojo.ui.post.SecondQuestionActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailActivity extends AppCompatActivity {

    private SupportMapFragment mapFragment;
    private String id, token;

    @BindView(R.id.tvAddress1)
    TextView tvCoordinate;

    @BindView(R.id.tvAddress2)
    TextView tvAddress;

    @BindView(R.id.tvHouseOwnerMap)
    TextView tvHouseOwner;

    @BindView(R.id.lyDetail)
    FrameLayout lyDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Detail Laporan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Token tokenModel = PrefManager.getTokenData(getApplicationContext());
        Bundle intent = getIntent().getExtras();
        if (intent == null) {
            id = "0";
        } else {
            id = intent.getString("ID");
            token = tokenModel.getToken();
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        getPostDetail();
    }

    private void getPostDetail() {

        //Toast.makeText(this, "id: " + token, Toast.LENGTH_SHORT).show();

        ApiPostDetail service = MyRetrofitClient.createService(ApiPostDetail.class);
        Call<ReportDataDetailResult> call = service.getPostDetail(token, id);
        call.enqueue(new Callback<ReportDataDetailResult>() {
            @Override
            public void onResponse(Call<ReportDataDetailResult> call, Response<ReportDataDetailResult> response) {

                String code = String.valueOf(response.code());

                if (code.equals("200")) {
                    double lat = Double.parseDouble(response.body().getData().getLat());
                    double lng = Double.parseDouble(response.body().getData().getLng());
                    String address = response.body().getData().getAddress1();
                    String addressBySurveyor = response.body().getData().getAddress2();
                    String houseOwner = response.body().getData().getName();

                    mapFragment.getMapAsync(googleMap -> {
                        LatLng markerPosition = new LatLng(lat, lng);
                        googleMap.addMarker(new MarkerOptions().position(markerPosition)
                                .title(houseOwner));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 15f));
                        tvAddress.setText(addressBySurveyor);
                        tvCoordinate.setText(address);
                        tvHouseOwner.setText(houseOwner);
                    });
                } else if (code.equals("401")) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else if (code.equals("400")) {
                    Snackbar snackbar = Snackbar.make(lyDetail, "Username atau password tidak boleh kosong, silahkan coba lagi.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(lyDetail, "Sedang ada gangguan server, silahkan coba lagi.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }

            @Override
            public void onFailure(Call<ReportDataDetailResult> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
