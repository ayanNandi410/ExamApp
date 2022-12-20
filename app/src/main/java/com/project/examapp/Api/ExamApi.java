package com.project.examapp.Api;

import com.project.examapp.models.Exam;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ExamApi {

    @GET("/exam")
    Call<ArrayList<Exam>> getExamList();                                                                      // GET request to get all images
}
