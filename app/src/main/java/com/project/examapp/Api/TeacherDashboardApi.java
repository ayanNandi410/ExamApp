package com.project.examapp.Api;

import com.project.examapp.models.Attempt;
import com.project.examapp.models.Exam;
import com.project.examapp.models.Result;
import com.project.examapp.models.Student;
import com.project.examapp.models.Subject;
import com.project.examapp.models.Teacher;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TeacherDashboardApi {

    @GET("/student/{dept}")
    Call<ArrayList<Student>> getStudentsList(@Path("dept") String dept);
    @GET("/exam/{dept}")
    Call<ArrayList<Exam>> getExamList(@Path("dept") String dept);

    @GET("/result/{examID}")
    Call<ArrayList<Result>> getResultsList(@Path("examID") String eid);

    @GET("/subjects/{dept}")
    Call<ArrayList<Subject>> getSubjectsList(@Path("dept") String dept);
}
