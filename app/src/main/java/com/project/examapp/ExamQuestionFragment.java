package com.project.examapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.project.examapp.Api.AnswerApi;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.Dashboard.DashboardActivity;
import com.project.examapp.models.Answer;
import com.project.examapp.models.Question;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamQuestionFragment extends Fragment {

    RetrofitClient client;
    AnswerApi answerApi;
    List<Question> qsArray;
    ArrayList<Answer> answers;
    TextView question, marks;
    List<Button> selectedList;
    Button a, b, c, d, prev, next, submit;
    int pos;

    public ExamQuestionFragment(List<Question> qsArray, ArrayList<Answer> answers) {
       this.qsArray = qsArray;
       this.answers = answers;
       selectedList  = new ArrayList<Button>();
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

        setupSelected();
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
                setAnswer("a",a);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnswer("b",b);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnswer("c", c);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnswer("d",d);
            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndExamAlert();
            }
        });


    }

    private void setupSelected(){
        for(int i = 0;i < qsArray.size();i++){
            selectedList.add(null);
        }
    }

    private void EndExamAlert() {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Set the message show for the Alert time
        int ct = countNull();
        String message = "Do you wish to submit answers and end exam ?";
        if(ct!=0){
            if(ct==1) {
                message = ct + " question not yet answered\n" + message;
            }
            else{
                message  = ct + " questions not yet answered\n" + message;
            }
        }

        builder.setMessage(message);

        // Set Alert Title
        builder.setTitle("End Exam");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Submit", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            submitAnswers();
        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }

    private void startExam(){
        pos = 0;
        setQuestionDetails(qsArray.get(pos));
    }

    private void setQuestionDetails(Question qs){
        question.setText("Q"+ (pos + 1) +". "+qs.getQuestion());
        marks.setText("Marks : "+qs.getMarks());
        a.setText(qs.getA());
        a.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        b.setText(qs.getB());
        b.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        c.setText(qs.getC());
        c.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        d.setText(qs.getD());
        d.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        if(selectedList.get(pos)!=null){
            selectedList.get(pos).setBackgroundColor(getResources().getColor(R.color.purple_700));
        }
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

    private void setAnswer(String answer,Button slct){
        if(selectedList.get(pos)!=null){
            selectedList.get(pos).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        selectedList.set(pos, slct);
        Answer a = answers.get(pos);
        a.setMcq(answer);
        answers.set(pos, a);
        slct.setBackgroundColor(getResources().getColor(R.color.purple_700));
    }

    private void submitAnswers(){
        Log.d("Submit answer", answers.toString());
        Call<ResponseBody> callAnswerPost = answerApi.postAnswers(answers);
        callAnswerPost.enqueue(new Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Log.e("Submit answers","SUCCESS");
                    Toast.makeText(getContext(), "Answers submitted", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Submit answers","FAILURE");
            }
        });
        Intent endExamintent = new Intent(getContext(), DashboardActivity.class);
        getActivity().finish();
        startActivity(endExamintent);
    }

    private int countNull(){
        int ct=0;
        for(int i=0;i<selectedList.size();i++)
        {
            if(selectedList.get(i)==null){
                ct++;
            }
        }
        return ct;
    }
}