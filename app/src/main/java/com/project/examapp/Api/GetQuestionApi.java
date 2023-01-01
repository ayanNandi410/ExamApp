package com.project.examapp.Api;

import com.project.examapp.models.Question;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetQuestionApi {

    @GET("question/{examId}")
    Call<ArrayList<Question>> getQuestions(@Path("examId") String examId);
}
