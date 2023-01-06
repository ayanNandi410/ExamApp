package com.project.examapp.models;

public class Result {
    private String student_id;
    private String exam_id;
    private int marks;

    public String getStudentId() {
        return student_id;
    }

    public void setStudentId(String student_id) {
        this.student_id = student_id;
    }

    public String getExamId() {
        return exam_id;
    }

    public void setExamId(String exam_id) {
        this.exam_id = exam_id;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }
}
