package com.project.examapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.project.examapp.Api.AnswerApi;
import com.project.examapp.Api.GetQuestionApi;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.models.Answer;
import com.project.examapp.models.Question;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamQuestionFragment extends Fragment {

    RetrofitClient client;
    AnswerApi answerApi;
    List<Question> qsArray;
    ArrayList<Answer> answers;
    TextView question, marks;
    Button a, b, c, d, prev, next, submit;
    int pos;

    public ExamQuestionFragment(List<Question> qsArray, ArrayList<Answer> answers) {
       this.qsArray = qsArray;
       this.answers = answers;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = RetrofitClient.getInstance();
        answerApi = client.getRetrofit().create(AnswerApi.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exam_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        question = view.findViewById(R.id.questionTextMark);
        marks = view.findViewById(R.id.qsMarks);
        a = view.findViewById(R.id.choice1Button);
        b = view.findViewById(R.id.choice2Button);
        c = view.findViewById(R.id.choice3Button);
        d = view.findViewById(R.id.choice4Button);
        prev =  view.findViewById(R.id.prevQuestion);
        next =  view.findViewById(R.id.nextQuestion);
        submit = view.findViewById(R.id.submit);

        submit.setVisibility(View.INVISIBLE);

        startExam();

        prev.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevQuestion();
            }
        });

        next.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnswer(a.getText().toString());
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnswer(a.getText().toString());
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnswer(a.getText().toString());
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnswer(a.getText().toString());
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnswers();
            }
        });


    }

    private void startExam(){
        pos = 0;
        setQuestionDetails(qsArray.get(pos));
    }

    private void setQuestionDetails(Question qs){
        question.setText("Q"+ (pos + 1) +". "+qs.getQuestion());
        marks.setText("Marks : "+qs.getMarks());
        a.setText(qs.getA());
        b.setText(qs.getB());
        c.setText(qs.getC());
        d.setText(qs.getD());
    }

    private void nextQuestion(){
        pos = pos + 1;
        if(pos==(qsArray.size()-1))
        {
            next.setVisibility(View.INVISIBLE);
            submit.setVisibility(View.VISIBLE);
        }
        if(pos!=0)
        {
            prev.setVisibility(View.VISIBLE);
        }
        setQuestionDetails(qsArray.get(pos));
    }

    private void prevQuestion(){
        pos = pos - 1;
        if(pos==0)
        {
            prev.setVisibility(View.INVISIBLE);
        }
        if(pos!=(qsArray.size()-1))
        {
            next.setVisibility(View.VISIBLE);
        }
        setQuestionDetails(qsArray.get(pos));
    }

    private void setAnswer(String answer){
        Answer a = answers.get(pos);
        a.setMcq(answer);
        answers.set(pos, a);
    }

    private void submitAnswers(){
        Log.d("Submit answer", answers.toString());
        Call<Integer> callAnswerPost = answerApi.postAnswers(answers);
        callAnswerPost.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getContext(), "Answers submitted", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("Fetch Question List","FAILURE");
                Log.e("Fetch Question list", t.toString());
            }
        });
    }
}