package com.project.examapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.examapp.R;
import com.project.examapp.models.Student;
import com.project.examapp.models.Teacher;

import java.util.ArrayList;

public class StudentsAdapter extends ArrayAdapter<Student> {
    public StudentsAdapter(Context context, ArrayList<Student> studentsList) {
        super(context, 0, studentsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Student student = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.teacher_list_item, parent, false);
        }
        // Lookup view for data population
        TextView Name = (TextView) convertView.findViewById(R.id.listTeacherName);
        TextView Email = (TextView) convertView.findViewById(R.id.listTeacherEmail);
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        icon.setImageResource(R.drawable.student);
        // Populate the data into the template view using the data object
        Name.setText(student.getName());
        Email.setText(student.getEmail());


        //clicking an exam


        // Return the completed view to render on screen
        return convertView;
    }
}
