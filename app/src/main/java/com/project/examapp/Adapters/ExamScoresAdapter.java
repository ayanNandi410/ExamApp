package com.project.examapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.project.examapp.R;
import com.project.examapp.models.Attempt;
import com.project.examapp.models.Exam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ExamScoresAdapter extends ArrayAdapter<Exam> {

    ArrayList<Exam> displayExamList;

    public ExamScoresAdapter(@NonNull Context context, ArrayList<Exam> examList) {
        super(context,0, examList);
        displayExamList = examList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the data item for this position
        Exam exam = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.see_exam_list_item, parent, false);
        }
        // Lookup view for data population
        TextView examName = (TextView) convertView.findViewById(R.id.examSListName);
        TextView examDesc = (TextView) convertView.findViewById(R.id.examSListDesc);
        TextView examSubject = (TextView) convertView.findViewById(R.id.examSListSubject);
        TextView examType = (TextView) convertView.findViewById(R.id.examSListType);

        // Populate the data into the template view using the data object
        examName.setText(exam.getExamName());
        examDesc.setText(exam.getDescription());
        examSubject.setText(exam.getSubject_id());
        examType.setText(exam.getType().toUpperCase());

        // Return the completed view to render on screen
        return convertView;
    }
}
