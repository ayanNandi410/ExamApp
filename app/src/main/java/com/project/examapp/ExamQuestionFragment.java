package com.project.examapp;

import static androidx.fragment.app.FragmentManager.TAG;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.examapp.Api.AnswerApi;
import com.project.examapp.Api.GetQuestionApi;
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
    FirebaseAuth mAuth;
    AnswerApi answerApi;
    GetQuestionApi questionApi;
    ArrayList<Question> qsArray;
    ArrayList<Answer> answers;
    TextView question, marks, examTime;
    List<Button> selectedList;
    Button a, b, c, d, prev, next, submit;
    int pos;
    String exam_id, student_id;
    Integer time, hr, min, sec;
    long endTime;
    Handler handler;

    Runnable UpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            final long end = endTime;
            long millis = end - SystemClock.uptimeMillis();

            if(millis<0)
            {
                stopTimer();
            }
            else{
                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds     = seconds % 60;

                if (seconds < 10) {
                    ((ExamPageActivity) getActivity()).setExamTime("" + minutes + ":0" + seconds);
                } else {
                    ((ExamPageActivity) getActivity()).setExamTime("" + minutes + ":" + seconds);
                }
                handler.postAtTime(this,SystemClock.uptimeMillis()+1000 );
            }
        }
    };

    public ExamQuestionFragment(ArrayList<Question> qsArray,Integer time, String student_id) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        handler = new Handler();

        this.student_id = student_id;
        this.time = time;
        this.qsArray = qsArray;
        this.answers = new ArrayList<Answer>();
       selectedList  = new ArrayList<Button>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = RetrofitClient.getInstance();
        answerApi = client.getRetrofit().create(AnswerApi.class);
        questionApi = client.getRetrofit().create(GetQuestionApi.class);
        exam_id = this.getArguments().getString("exam_id");
        for(int i = 0;i < qsArray.size();i++){
            Question q = qsArray.get(i);
            Answer a = new Answer(exam_id, q.getId(), student_id);
            Log.d("Answer value",a.getExamId());
            answers.add(a);
        }
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
        prev.setVisibility(View.INVISIBLE);

        startTimer(time);

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

    private void startTimer(Integer time){
        long interval = (Integer)(time * 60 * 1000);
        endTime = SystemClock.uptimeMillis()+interval;
        handler.removeCallbacks(UpdateTimeTask);
        handler.postDelayed(UpdateTimeTask, 100);
    }

    private void stopTimer()
    {
        submitAnswers();
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
        if(pos<=0)
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
        handler.removeCallbacks(UpdateTimeTask);
        ProgressDialog dialog = ProgressDialog.show(getContext(), "",
                "Submitting answers.. Please wait...", true);
        dialog.show();
        Call<ResponseBody> callAnswerPost = answerApi.postAnswers(answers);
        callAnswerPost.enqueue(new Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Log.e("Submit answers","SUCCESS");
                    Toast.makeText(getContext(), "Answers submitted", Toast.LENGTH_LONG).show();
                    Intent endExamintent = new Intent(getContext(), DashboardActivity.class);
                    dialog.dismiss();
                    getActivity().finish();
                    startActivity(endExamintent);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Submit answers","FAILURE");
                Log.e("Submit answers",t.toString());
                dialog.dismiss();
                Toast.makeText(getContext(), "Failed to submit answers", Toast.LENGTH_LONG).show();
            }
        });

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