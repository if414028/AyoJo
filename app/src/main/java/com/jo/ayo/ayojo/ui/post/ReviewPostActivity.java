package com.jo.ayo.ayojo.ui.post;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Wave;
import com.jo.ayo.ayojo.R;
import com.jo.ayo.ayojo.data.model.pref.PostData;
import com.jo.ayo.ayojo.data.model.post.create.PostDataResult;
import com.jo.ayo.ayojo.data.model.Token;
import com.jo.ayo.ayojo.data.pref.PrefManager;
import com.jo.ayo.ayojo.nework.MyRetrofitClient;
import com.jo.ayo.ayojo.nework.api.ApiCreatePost;
import com.jo.ayo.ayojo.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewPostActivity extends AppCompatActivity {

    @BindView(R.id.imgPreview)
    ImageView imgPreview;

    @BindView(R.id.tvHouseOwner)
    TextView tvHouseOwner;

    @BindView(R.id.tvFirstAnswer)
    TextView tvFirstAnswer;

    @BindView(R.id.tvSecondAnswer)
    TextView tvSecondAnswer;

    @BindView(R.id.tvPekerjaan)
    TextView tvWork;

    @BindView(R.id.tvAlamat)
    TextView tvAddress;

    @BindView(R.id.tvUsia)
    TextView tvAge;

    @BindView(R.id.tvJenisKelamin)
    TextView tvSex;

    @BindView(R.id.lyPostPreview)
    LinearLayout lyPostPreview;

    private String imageUrl = "http://wajahriau.com/wp-content/uploads/2017/12/RSLH.jpg";

    private String houseOwner, firstAnswer, secondAnswer, work, address, age, sex;
    private String lat, lng;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_post);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Review laporan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Camera/photo.jpg");
        imgPreview.setImageURI(uri);

        //get data from preferences
        PostData postData = PrefManager.getPostData(getApplicationContext());

        houseOwner = postData.getHouseOwner();
        lat = String.valueOf(postData.getLat());
        lng = String.valueOf(postData.getLng());
        firstAnswer = postData.getFirstAnsewr();
        secondAnswer = postData.getSecondAnswer();
        work = postData.getWork();
        address = postData.getAddress();
        age = postData.getAge();
        sex = postData.getSex();

        //set data to text view
        tvHouseOwner.setText(houseOwner);
        tvFirstAnswer.setText(firstAnswer);
        tvSecondAnswer.setText(secondAnswer);
        tvWork.setText(work);
        tvAddress.setText(address);
        tvAge.setText(age);
        tvSex.setText(sex);

        Toast.makeText(this, "lat:" + lat + " lng: " + lng, Toast.LENGTH_SHORT).show();

        Wave wave = new Wave();
        progressDialog = new ProgressDialog(ReviewPostActivity.this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setIndeterminateDrawable(wave);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), TakePictureActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @OnClick(R.id.btnSubmit)
    void doPost() {
        createPost();
    }

    private void createPost() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progressbar_spinkit);

        Token tokenModel = PrefManager.getTokenData(getApplicationContext());
        String token = tokenModel.getToken();

        ApiCreatePost service = MyRetrofitClient.createService(ApiCreatePost.class);
        Call<PostDataResult> call = service.createPost(
                token,
                houseOwner,
                address,
                work,
                age,
                sex,
                lat,
                lng,
                imageUrl,
                firstAnswer,
                secondAnswer
        );
        call.enqueue(new Callback<PostDataResult>() {
            @Override
            public void onResponse(Call<PostDataResult> call, Response<PostDataResult> response) {
                String code = String.valueOf(response.code());

                if (code.equals("200")) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    progressDialog.dismiss();
                } else if (code.equals("400")) {
                    progressDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(lyPostPreview, "Tidak boleh ada kolom yang kosong, silahkan coba lagi", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    progressDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(lyPostPreview, "Sedang ada gangguan server, silahkan coba lagi", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<PostDataResult> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar snackbar = Snackbar.make(lyPostPreview, "Anda tidak tersambung dengan internet", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }
}
