package com.sabekur2017.newsviewsv2.service;

import com.sabekur2017.newsviewsv2.models.NewsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiService {
    @GET
    Call<NewsModel> getNewsResponces(@Url String urlString);
}
