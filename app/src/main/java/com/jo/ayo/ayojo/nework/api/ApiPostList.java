package com.jo.ayo.ayojo.nework.api;

import com.jo.ayo.ayojo.data.model.post.list.ReportDataResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiPostList {

    @GET("api/otherReports/mobile")
    Call<ReportDataResult> getPostLis(@Header("authorization") String token,
                                      @Query("limit") int limit,
                                      @Query("sortby") String sortby,
                                      @Query("order") String order);

    @GET("api/otherReports/mobile")
    Call<ReportDataResult> getPostFiltered(@Header("authorization") String token,
                                        @Query("limit") int limit,
                                        @Query("sortby") String sortby,
                                        @Query("order") String order,
                                        @Query("filterbydate") String filter);
}
