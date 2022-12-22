package com.project.examapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.examapp.R;
import com.project.examapp.models.Exam;

import java.util.ArrayList;

public class ExamsAdapter extends ArrayAdapter<Exam> {
    public ExamsAdapter(Context context, ArrayList<Exam> examList) {
        super(context, 0, examList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
        // Populate the data into the template view using the data object
        examName.setText(exam.getExamName());
        examDesc.setText(exam.getDescription());
        examSubject.setText(exam.getSubject_id());

        //clicking an exam


        // Return the completed view to render on screen
        return convertView;
    }
}
