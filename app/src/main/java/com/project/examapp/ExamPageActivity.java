package com.project.examapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.project.examapp.Api.GetQuestionApi;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.models.Question;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamPageActivity extends AppCompatActivity {

    RetrofitClient client;
    String exam_id;
    GetQuestionApi questionApi;
    List<Question> qsList;
    TextView examName;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_page);
        client = RetrofitClient.getInstance();
        questionApi = client.getRetrofit().create(GetQuestionApi.class);
        // get exam id
        Intent intent = getIntent();
        exam_id = intent.getStringExtra("exam_id");

        examName = findViewById(R.id.ExamName);
        back = findViewById(R.id.backB);

    }

    @Override
    protected void onResume() {
        super.onResume();
        examName.setText(exam_id);
        back.setVisibility(View.INVISIBLE);


        Call<ArrayList<Question>> callQuestionList = questionApi.getQuestions(exam_id);
        callQuestionList.enqueue(new Callback<java.util.ArrayList<Question>>() {
            @Override
            public void onResponse(Call<ArrayList<Question>> call, Response<ArrayList<Question>> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(ExamPageActivity.this, "Question List fetched", Toast.LENGTH_SHORT).show();
                    qsList = response.body();
                    showQuestionFragment();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Question>> call, Throwable t) {
                Log.e("Fetch Question List","FAILURE");
            }
        });
    }

    public void showQuestionFragment()
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.questionSet, new ExamQuestionFragment(qsList));
        transaction.commit();
    }
}