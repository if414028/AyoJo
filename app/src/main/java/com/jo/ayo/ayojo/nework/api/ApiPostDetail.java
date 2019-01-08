package com.jo.ayo.ayojo.nework.api;

import com.jo.ayo.ayojo.data.model.post.detail.ReportDataDetailResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiPostDetail {

    @GET("api/reports/detailMobile/{id}")
    Call<ReportDataDetailResult> getPostDetail(@Header("authorization") String token,
                                               @Path("id") String id);
}
