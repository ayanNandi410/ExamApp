package com.project.examapp.Api;

import com.project.examapp.models.Exam;
import com.project.examapp.models.Subject;
import com.project.examapp.models.Teacher;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface StudentDashboardApi {

    @GET("/exam/{dept}")
    Call<ArrayList<Exam>> getExamList(@Path("dept") String dept);

    @GET("/teacher/{dept}")
    Call<ArrayList<Teacher>> getTeachersList(@Path("dept") String dept);

    @GET("/subjects/{dept}")
    Call<ArrayList<Subject>> getSubjectsList(@Path("dept") String dept);
}
