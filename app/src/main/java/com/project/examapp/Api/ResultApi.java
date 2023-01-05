package com.project.examapp.Api;

import com.project.examapp.models.Question;
import com.project.examapp.models.Result;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ResultApi {
    @GET("question/{examId}")
    Call<ArrayList<Result>> getResults(@Path("examId") String examId);
}
