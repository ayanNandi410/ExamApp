package com.project.examapp.Api;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.examapp.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private  static final String BASE_URL = Constants.URL;  // Base URL
    private static RetrofitClient mInstance;
    private final Retrofit retrofit;
    private final FirebaseAuth mAuth;

    private RetrofitClient() {
        mAuth = FirebaseAuth.getInstance();
        //requestBuilder.header("x-id", user.getEmail());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor()).addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder requestBuilder = chain.request().newBuilder();
                        FirebaseUser user = mAuth.getCurrentUser();
                        //requestBuilder.header("x-id", user.getEmail());
                        return chain.proceed(requestBuilder.build());
                    }
                }).build();

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
