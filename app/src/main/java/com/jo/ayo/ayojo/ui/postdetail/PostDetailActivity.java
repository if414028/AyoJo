package com.jo.ayo.ayojo.ui.postdetail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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

    @BindView(R.id.tvCoordinateMap)
    TextView tvCoordinate;

    @BindView(R.id.tvAddressMap)
    TextView tvAddress;

    @BindView(R.id.tvHouseOwnerMap)
    TextView tvHouseOwner;

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

                double lat = Double.parseDouble(response.body().getData().getLat());
                double lng = Double.parseDouble(response.body().getData().getLng());
                String address = response.body().getData().getAddress1();
                String houseOwner = response.body().getData().getName();

                mapFragment.getMapAsync(googleMap -> {
                    LatLng markerPosition = new LatLng(lat, lng);
                    googleMap.addMarker(new MarkerOptions().position(markerPosition)
                            .title(houseOwner));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 15f));
                    tvAddress.setText(address);
                    tvHouseOwner.setText(houseOwner);
                });

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
