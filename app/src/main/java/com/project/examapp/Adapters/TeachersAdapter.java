package com.project.examapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.examapp.R;
import com.project.examapp.models.Exam;
import com.project.examapp.models.Teacher;

import java.util.ArrayList;

public class TeachersAdapter extends ArrayAdapter<Teacher> {
    public TeachersAdapter(Context context, ArrayList<Teacher> teachersList) {
        super(context, 0, teachersList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Teacher teacher = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.teacher_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tchName = (TextView) convertView.findViewById(R.id.listTeacherName);
        TextView tchEmail = (TextView) convertView.findViewById(R.id.listTeacherEmail);
        // Populate the data into the template view using the data object
        tchName.setText(teacher.getName());
        tchEmail.setText(teacher.getEmail());


        //clicking an exam


        // Return the completed view to render on screen
        return convertView;
    }
}
