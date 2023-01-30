package com.project.examapp.Adapters;

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
import com.project.examapp.models.Exam;
import com.project.examapp.models.Result;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ScoresAdapter extends ArrayAdapter<Result> {
    ArrayList<Result> resultList;

    public ScoresAdapter(@NonNull Context context, ArrayList<Result> resultList) {
        super(context,0, resultList);
        this.resultList = resultList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the data item for this position
        Result result = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.score_list_item, parent, false);
        }
        // Lookup view for data population
        TextView examID = (TextView) convertView.findViewById(R.id.scoreExamId);
        TextView stName = (TextView) convertView.findViewById(R.id.scoreStudentName);
        TextView stMarks = (TextView) convertView.findViewById(R.id.scoreMarks);
        // Populate the data into the template view using the data object
        examID.setText(result.getExamId());
        String score = String.valueOf(result.getMarks())+"/"+String.valueOf(result.getTotal());
        stMarks.setText(score);
        stName.setText(result.getStudent_name());

        // Return the completed view to render on screen
        return convertView;
    }
}
