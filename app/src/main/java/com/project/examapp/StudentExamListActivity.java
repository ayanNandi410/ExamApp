package com.project.examapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.examapp.Adapters.ExamsAdapter;
import com.project.examapp.Api.ExamApi;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.models.Exam;

import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentExamListActivity extends AppCompatActivity {

    ArrayList<Exam> examList;
    ExamsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_exam_list);

        //Retrofit call
        RetrofitClient client = RetrofitClient.getInstance();
        ExamApi examApi = client.getRetrofit().create(ExamApi.class);
        Call<ArrayList<Exam>> callExamList = examApi.getExamList();
        callExamList.enqueue(new Callback<ArrayList<Exam>>() {
            @Override
            public void onResponse(Call<ArrayList<Exam>> call, Response<ArrayList<Exam>> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(StudentExamListActivity.this, "Exam List fetched", Toast.LENGTH_SHORT).show();
                    examList = response.body();

                    // Create the adapter to convert the array to views
                    adapter = new ExamsAdapter(StudentExamListActivity.this, examList);

                    // Attach the adapter to a ListView
                    ListView listView = (ListView) findViewById(R.id.stdExamListView);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Exam>> call, Throwable t) {
                Log.e("Fetch Exam List","FAILURE");
            }
        });



    }
}