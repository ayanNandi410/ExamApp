package com.project.examapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.examapp.Api.GetQuestionApi;
import com.project.examapp.Api.ResultApi;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.models.Answer;
import com.project.examapp.models.Question;
import com.project.examapp.models.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamPageActivity extends AppCompatActivity {

    RetrofitClient client;
    String exam_id;
    GetQuestionApi questionApi;
    ResultApi resultApi;
    ArrayList<Question> qsList;
    List<Result> results;
    TextView examName, examTime;
    ArrayList<Answer> answers;
    FirebaseAuth mAuth;
    Integer time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_page);
        client = RetrofitClient.getInstance();
        questionApi = client.getRetrofit().create(GetQuestionApi.class);
        resultApi = client.getRetrofit().create(ResultApi.class);
        // get exam id
        Intent intent = getIntent();
        exam_id = intent.getStringExtra("exam_id");
        time = intent.getIntExtra("time", 100);

        //startTime = time*60*1000;
        //startTimer(time);

        client = RetrofitClient.getInstance();
        questionApi = client.getRetrofit().create(GetQuestionApi.class);

        examName = findViewById(R.id.ExamName);
        examTime = findViewById(R.id.ExamTime);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        examName.setText(exam_id);
        showProgressBar();
        Call<ArrayList<Question>> callQuestionList = questionApi.getQuestions(exam_id);
        callQuestionList.enqueue(new Callback<java.util.ArrayList<Question>>() {
            @Override
            public void onResponse(Call<ArrayList<Question>> call, Response<ArrayList<Question>> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(ExamPageActivity.this, "Question List fetched", Toast.LENGTH_SHORT).show();
                    qsList = response.body();
                    Log.e("QS List", String.valueOf(qsList.size()));
                    FirebaseUser user = mAuth.getCurrentUser();

                    showQuestionFragment();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Question>> call, Throwable t) {
                Log.e("Fetch Question List","FAILURE");
            }
        });
        /*Call<ArrayList<Result>> callResultList = resultApi.getResults(exam_id);
        callResultList.enqueue(new Callback<ArrayList<Result>>() {
            @Override
            public void onResponse(Call<ArrayList<Result>> call, Response<ArrayList<Result>> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(ExamPageActivity.this, "Results fetched", Toast.LENGTH_SHORT).show();
                    results = response.body();
                    FirebaseUser user = mAuth.getCurrentUser();

                    //answers = new ArrayList<Answer>();
                    showQuestionFragment();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Result>> call, Throwable t) {
                Log.e("Fetch Question List","FAILURE");
                Log.e("Fetch Question list", t.toString());
            }
        });

         */
    }

    private void showProgressBar(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.questionSet, new ProgressBarFragment());
        transaction.commit();
    }

    public void setExamTime(String t)
    {
        examTime.setText(t);
    }


    public void showQuestionFragment()
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        ExamQuestionFragment qsFragment = new ExamQuestionFragment(qsList,time);
        Bundle bundle = new Bundle();
        bundle.putString("message", exam_id );
        qsFragment.setArguments(bundle);
        transaction.replace(R.id.questionSet, qsFragment);
        transaction.commit();
    }
}