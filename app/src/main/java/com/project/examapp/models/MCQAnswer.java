package com.project.examapp.models;

import java.util.ArrayList;

public class MCQAnswer {
    private String exam_id;
    private String student_id;
    private String timestamp;
    private ArrayList<MCQChoice> choiceList;

    public MCQAnswer(String exam_id, String student_id) {
        this.exam_id = exam_id;
        this.student_id = student_id;
    }

    public String getExamId() {
        return exam_id;
    }

    public void setExamId(String exam_id) {
        this.exam_id = exam_id;
    }

    public String getStudentId() {
        return student_id;
    }

    public void setStudentId(String student_id) {
        this.student_id = student_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<MCQChoice> getChoiceList() {
        return choiceList;
    }

    public void setChoiceList(ArrayList<MCQChoice> choiceList) {
        this.choiceList = choiceList;
    }
}
