package com.project.examapp.Adapters;

import static com.google.android.gms.common.api.internal.LifecycleCallback.getFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.project.examapp.R;
import com.project.examapp.models.Attempt;
import com.project.examapp.models.Exam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ExamsAdapter extends ArrayAdapter<Exam> {
     ArrayList<Attempt> attemptArrayList;
     ArrayList<String> attempts;
    public ExamsAdapter(Context context, ArrayList<Exam> examList, ArrayList<Attempt> attemptArrayList) {
        super(context, 0, examList);
        this.attemptArrayList = attemptArrayList;
        this.attempts = new ArrayList<String>();
        createAttemptList();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        // Get the data item for this position
        Exam exam = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.exam_list_item, parent, false);
        }
        // Lookup view for data population
        TextView examName = (TextView) convertView.findViewById(R.id.examListName);
        TextView examDesc = (TextView) convertView.findViewById(R.id.examListDesc);
        TextView examSubject = (TextView) convertView.findViewById(R.id.examListSubject);
        TextView examCond = (TextView) convertView.findViewById(R.id.examListCondition);
        // Populate the data into the template view using the data object
        examName.setText(exam.getExamName());
        examDesc.setText(exam.getDescription());
        examSubject.setText(exam.getSubject_id());

        exam.setAvailable(false);

        if(attempts.contains(exam.getExam_id()))
        {
            examCond.setText("Attempted");
            examCond.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.attempted));
            examCond.setTextColor(Color.WHITE);
        }
        else
        {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String examDate = exam.getDate();

            int value = (currentDate.compareTo(examDate));

            if(value==0) {
                exam.setAvailable(true);
                examCond.setText("Unattempted");
                examCond.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.unattempted));
                examCond.setTextColor(Color.WHITE);
            }
            else if(value>0) {
                examCond.setText("Over");
                examCond.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.over));
            }
            else {
                examCond.setText("Upcoming");
                examCond.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.forthcoming));
                examCond.setTextColor(Color.WHITE);
            }

        }
        // Return the completed view to render on screen
        return convertView;
    }

    private void createAttemptList()
    {
        for(int i=0;i<attemptArrayList.size();i++)
        {
            Attempt attempt = attemptArrayList.get(i);
            String exam_id = attempt.getExam_id();
            attempts.add(exam_id);
        }
    }
}
