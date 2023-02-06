package com.project.examapp.Exam;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.examapp.Api.ExamApi;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.R;
import com.project.examapp.models.Attempt;
import com.project.examapp.models.MCQAnswer;
import com.project.examapp.models.MCQChoice;
import com.project.examapp.models.Question;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamQuestionFragment extends Fragment {

    RetrofitClient client;
    FirebaseAuth mAuth;
    ExamApi examApi;
    ArrayList<Question> qsArray;
    MCQAnswer answerSet;
    ArrayList<MCQChoice> MCQAnswers;
    TextView question, marks, examTime, progress;
    List<Button> selectedList;
    Button a, b, c, d, prev, next, submit, clearChoice;
    static int pos;
    String exam_id, student_id;
    Integer time, hr, min, sec, size;
    long endTime;
    Handler handler;
    ProgressBar progressBar;
    ProgressDialog dialog;
    static int attempts;

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
        this.size = qsArray.size();

        this.MCQAnswers = new ArrayList<MCQChoice>();
        this.answerSet = new MCQAnswer(exam_id,student_id);
        this.answerSet.setChoiceList(MCQAnswers);
        selectedList  = new ArrayList<Button>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = RetrofitClient.getInstance();
        examApi = client.getRetrofit().create(ExamApi.class);
        exam_id = this.getArguments().getString("exam_id");
        pos = 0;

        attempts=0;
        answerSet.setExamId(exam_id);
        answerSet.setStudentId(student_id);
        for(int i = 0;i < qsArray.size();i++){
            Question q = qsArray.get(i);
            MCQChoice choice = new MCQChoice(q.getId());
            MCQAnswers.add(choice);
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
        clearChoice = view.findViewById(R.id.clearChoice);
        submit.setVisibility(View.INVISIBLE);
        prev.setVisibility(View.INVISIBLE);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progress = (TextView) view.findViewById(R.id.progress);
        dialog = ProgressDialog.show(getContext(), "",
                "Submitting Answers.. Please wait...", true);
        dialog.dismiss();

        startTimer(time);
        progressBar.setMax(size);
        String prog = "1/"+String.valueOf(size);
        progress.setText(prog);

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

        clearChoice.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearButtonChoice();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(UpdateTimeTask);
        toDashBoardActivity();
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

    private void clearButtonChoice()
    {
        Button selected = selectedList.get(pos);
        selected.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        selectedList.set(pos,null);
        MCQChoice a = MCQAnswers.get(pos);
        a.setMcq("");
        MCQAnswers.set(pos, a);
    }

    private void EndExamAlert() {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Set the message show for the Alert time
        int ct = countNull();
        String message = "Do you wish to submit Answers and end exam ?";
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
            selectedList.get(pos).setBackgroundColor(getResources().getColor(R.color.selected));
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
        progressBar.setProgress(pos+1);
        String prog = String.valueOf(pos+1)+"/"+String.valueOf(size);
        progress.setText(prog);
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
        progressBar.setProgress(pos+1);
        String prog = String.valueOf(pos+1)+"/"+String.valueOf(size);
        progress.setText(prog);
    }

    private void setAnswer(String answer,Button slct){
        if(selectedList.get(pos)!=null){
            selectedList.get(pos).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        selectedList.set(pos, slct);
        MCQChoice a = MCQAnswers.get(pos);
        a.setMcq(answer);
        MCQAnswers.set(pos, a);
        slct.setBackgroundColor(getResources().getColor(R.color.selected));
    }

    private void submitAnswers()
    {
        dialog.show();

        String currDateTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        answerSet.setTimestamp(currDateTime);

        Call<ResponseBody> callAnswerPost = examApi.postAnswers(answerSet);
        callAnswerPost.enqueue(new Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Log.e("Submit Answers","SUCCESS");
                    handler.removeCallbacks(UpdateTimeTask);
                    registerAttemptAndSubmit();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Submit Answers","FAILURE");
                dialog.dismiss();
                Toast.makeText(getContext(), "Failed to submit Answers", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void toDashBoardActivity()
    {
        dialog.dismiss();
        ((ExamPageActivity)getActivity()).endExam();
    }

    private void registerAttemptAndSubmit()
    {
        Attempt attempt = new Attempt();
        attempt.setExam_id(exam_id);
        attempt.setStudent_id(student_id);
        Call<ResponseBody> callAttemptPost = examApi.postAttempt(attempt);
        callAttemptPost.enqueue(new Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Log.e("Attempt Registration","SUCCESS");
                    Toast.makeText(getContext(), "Answers submitted", Toast.LENGTH_LONG).show();
                    toDashBoardActivity();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Submit Answers","FAILURE");
                Log.e("Submit Answers",t.toString());
                attempts++;
                if(attempts<20)
                {
                    registerAttemptAndSubmit();
                }
                else
                {
                    dialog.dismiss();
                }
                Toast.makeText(getContext(), "Failed to register attempt", Toast.LENGTH_LONG).show();
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