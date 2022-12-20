package com.project.examapp.Api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private  static final String BASE_URL = "http://192.168.1.35:5000";                          // Base URL
    private static RetrofitClient mInstance;
    private final Retrofit retrofit;
    private final OkHttpClient okHttpClient;

    private RetrofitClient() {
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor()).build();

                retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public FileUploadApi getAPI() {
        return retrofit.create(FileUploadApi.class);
    }

    public Retrofit getRetrofit()
    {
        return retrofit;
    }
}
