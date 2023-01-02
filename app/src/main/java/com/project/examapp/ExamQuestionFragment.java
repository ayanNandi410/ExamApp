package com.project.examapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.project.examapp.models.Question;

import java.util.List;

public class ExamQuestionFragment extends Fragment {

    List<Question> qsArray;
    List<String> answers;
    TextView question, marks;
    Button a, b, c, d, prev, next, submit;
    int pos;

    public ExamQuestionFragment(List<Question> qsArray) {
       this.qsArray = qsArray;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        submit = view.findViewById(R.id.Submit);

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
}