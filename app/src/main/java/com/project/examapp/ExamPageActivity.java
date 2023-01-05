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
    List<Question> qsList;
    List<Result> results;
    TextView examName;
    ImageButton back;
    ArrayList<Answer> answers;
    FirebaseAuth mAuth;

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

        examName = findViewById(R.id.ExamName);
        back = findViewById(R.id.backB);
        mAuth = FirebaseAuth.getInstance();
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
                    FirebaseUser user = mAuth.getCurrentUser();

                    answers = new ArrayList<Answer>();
                    for(int i = 0;i < qsList.size();i++){
                        Question q = qsList.get(i);
                        Answer a = new Answer(exam_id, q.getId(), user.getUid());
                        answers.add(a);
                    }
                    showQuestionFragment();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Question>> call, Throwable t) {
                Log.e("Fetch Question List","FAILURE");
                Log.e("Fetch Question list", t.toString());
            }
        });

        Call<ArrayList<Result>> callResultList = resultApi.getResults(exam_id);
        callResultList.enqueue(new Callback<ArrayList<Result>>() {
            @Override
            public void onResponse(Call<ArrayList<Result>> call, Response<ArrayList<Result>> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(ExamPageActivity.this, "Results fetched", Toast.LENGTH_SHORT).show();
                    results = response.body();
                    FirebaseUser user = mAuth.getCurrentUser();

                    answers = new ArrayList<Answer>();
                    showQuestionFragment();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Result>> call, Throwable t) {
                Log.e("Fetch Question List","FAILURE");
                Log.e("Fetch Question list", t.toString());
            }
        });
    }

    public void showQuestionFragment()
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.questionSet, new ExamQuestionFragment(qsList, answers));
        transaction.commit();
    }
}