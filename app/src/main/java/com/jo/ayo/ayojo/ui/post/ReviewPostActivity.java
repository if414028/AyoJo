package com.jo.ayo.ayojo.ui.post;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.jo.ayo.ayojo.data.model.post.create.ImageUpload;
import com.jo.ayo.ayojo.data.model.pref.PostData;
import com.jo.ayo.ayojo.data.model.post.create.PostDataResult;
import com.jo.ayo.ayojo.data.model.Token;
import com.jo.ayo.ayojo.data.pref.PrefManager;
import com.jo.ayo.ayojo.nework.MyRetrofitClient;
import com.jo.ayo.ayojo.nework.api.ApiCreatePost;
import com.jo.ayo.ayojo.nework.api.ApiImageUpload;
import com.jo.ayo.ayojo.ui.login.LoginActivity;
import com.jo.ayo.ayojo.ui.main.MainActivity;
import com.jo.ayo.ayojo.utils.ResultHolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Headers;

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

    private String imageUrl;

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

        Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory("AyoJo").getPath() + "/photo.jpg");
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
        uploadImage();
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private File createTempFile(Bitmap bitmap) {
        //write the bytes in file
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + "_image.jpg");
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapData = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private void uploadImage() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progressbar_spinkit);

        Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory("AyoJo").getPath() + "/photo.jpg");
//        File video = new File(uri.getPath());
//
//        Bitmap image = ResultHolder.getImage();
        File file = new File(uri.getPath());

        RequestBody imageRequest = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part imageBody = MultipartBody.Part.createFormData("file", file.getName(), imageRequest);

        ApiImageUpload service = MyRetrofitClient.createService(ApiImageUpload.class);
        Call<ImageUpload> call = service.uploadImage(imageBody);
        call.enqueue(new Callback<ImageUpload>() {
            @Override
            public void onResponse(Call<ImageUpload> call, Response<ImageUpload> response) {
                String code = String.valueOf(response.code());
                Toast.makeText(ReviewPostActivity.this, "code: " + code, Toast.LENGTH_SHORT).show();

                if (code.equals("200")) {
                    imageUrl = response.body().getResult();
                    createPost();
                } else if (code.equals("401")) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Snackbar snackbar = Snackbar.make(lyPostPreview, "Sedang ada gangguan server, silahkan coba lagi", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ImageUpload> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar snackbar = Snackbar.make(lyPostPreview, "Gagal", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void createPost() {

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
                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    progressDialog.dismiss();
                } else if (code.equals("401")) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
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
