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
    String type;

    public ScoresAdapter(@NonNull Context context, ArrayList<Result> resultList,String type) {
        super(context,0, resultList);
        this.resultList = resultList;
        this.type = type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        Result result = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.score_list_item, parent, false);
        }

        TextView examID = (TextView) convertView.findViewById(R.id.scoreExamId);
        TextView stName = (TextView) convertView.findViewById(R.id.scoreStudentName);
        TextView stMarks = (TextView) convertView.findViewById(R.id.scoreMarks);
        TextView stTimestamp = (TextView) convertView.findViewById(R.id.scoreTimestamp);

        examID.setText(result.getExamId());
        stName.setText(result.getStudent_name());
        stTimestamp.setText(result.getTimestamp());
        if(type.equals("mcq"))
        {
            String score = String.valueOf(result.getMarks())+"/"+String.valueOf(result.getTotal());
            stMarks.setText(score);
        }
        else
        {
            stMarks.setVisibility(View.INVISIBLE);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
