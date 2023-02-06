package com.project.examapp.Exam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.examapp.Api.ExamApi;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.Dashboard.DashboardActivity;
import com.project.examapp.common.ProgressBarFragment;
import com.project.examapp.R;
import com.project.examapp.models.MCQAnswer;
import com.project.examapp.models.Question;
import com.project.examapp.models.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamPageActivity extends AppCompatActivity {

    RetrofitClient client;
    String exam_id, student_id, type, question;
    ExamApi examApi;
    ArrayList<Question> qsList;
    TextView examName, examTime;
    FirebaseAuth mAuth;
    Integer time;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_page);
        client = RetrofitClient.getInstance();
        examApi = client.getRetrofit().create(ExamApi.class);
        // get exam id
        Intent intent = getIntent();
        exam_id = intent.getStringExtra("exam_id");
        time = intent.getIntExtra("time", 100);
        student_id = intent.getStringExtra("student_id");
        type = intent.getStringExtra("type");
        question = intent.getStringExtra("question");

        examName = findViewById(R.id.ExamName);
        examTime = findViewById(R.id.ExamTime);
        mAuth = FirebaseAuth.getInstance();
        dialog = ProgressDialog.show(ExamPageActivity.this, "",
                "Please wait...", true);
        getQuestions();
    }

    private void getQuestions()
    {
        examName.setText(exam_id);
        showProgressBar();
        Call<ArrayList<Question>> callQuestionList = examApi.getQuestions(exam_id);
        callQuestionList.enqueue(new Callback<java.util.ArrayList<Question>>() {
            @Override
            public void onResponse(Call<ArrayList<Question>> call, Response<ArrayList<Question>> response) {
                if(response.isSuccessful()) {
                    qsList = response.body();
                    if(qsList.size()==0)
                    {
                        Toast.makeText(ExamPageActivity.this, "No questions in server yet", Toast.LENGTH_SHORT).show();
                        endExam();
                    }
                    FirebaseUser user = mAuth.getCurrentUser();
                    dialog.dismiss();
                    showExamFragment();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Question>> call, Throwable t) {
                Log.e("Fetch Question List","FAILURE");
                Toast.makeText(ExamPageActivity.this, "Could not Fetch questions", Toast.LENGTH_SHORT).show();
                endExam();
            }
        });
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


    public void showExamFragment()
    {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            ExamQuestionFragment qsFragment = new ExamQuestionFragment(qsList,time,student_id);
            Bundle bundle = new Bundle();
            bundle.putString("exam_id", exam_id );
            qsFragment.setArguments(bundle);
            transaction.replace(R.id.questionSet, qsFragment);
            transaction.commit();
    }

    public void endExam()
    {
        Intent endExamintent = new Intent(ExamPageActivity.this, DashboardActivity.class);
        this.finish();
        startActivity(endExamintent);
    }
}