package com.project.examapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.examapp.R;
import com.project.examapp.models.Exam;
import com.project.examapp.models.Subject;

import java.util.ArrayList;

public class SubjectsAdapter extends ArrayAdapter<Subject> {
    public SubjectsAdapter(Context context, ArrayList<Subject> subjectList) {
        super(context, 0,subjectList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        // Get the data item for this position
        Subject subject = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.subject_list_item, parent, false);
        }
        // Lookup view for data population
        TextView subName = (TextView) convertView.findViewById(R.id.subListName);
        TextView subid = (TextView) convertView.findViewById(R.id.subListId);
        TextView subDept = (TextView) convertView.findViewById(R.id.subListDept);
        // Populate the data into the template view using the data object
        subDept.setText(subject.getDept());
        subid.setText(subject.getSubject_id());
        subName.setText(subject.getName());

        // Return the completed view to render on screen
        return convertView;
    }
}
