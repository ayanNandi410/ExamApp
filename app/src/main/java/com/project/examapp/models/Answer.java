package com.project.examapp.models;

public class Answer {
    private String mcq;
    private String answer;
    private String exam_id;
    private String question_id;
    private String student_id;

    public Answer(String exam_id, String question_id, String student_id) {
        this.exam_id = exam_id;
        this.question_id = question_id;
        this.student_id = student_id;
        this.mcq = "";
        this.answer = "";
    }

    public String getMcq() {
        return mcq;
    }

    public void setMcq(String mcq) {
        this.mcq = mcq;
    }

    public String getExamId() {
        return exam_id;
    }

    public void setExamId(String exam_id) {
        this.exam_id = exam_id;
    }

    public String getQuestionId() {
        return question_id;
    }

    public void setQuestionId(String question_id) {
        this.question_id = question_id;
    }

    public String getStudentId() {
        return student_id;
    }

    public void setStudentId(String student_id) {
        this.student_id = student_id;
    }
}
