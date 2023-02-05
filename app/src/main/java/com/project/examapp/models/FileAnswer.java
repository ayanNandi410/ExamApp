package com.project.examapp.models;

public class FileAnswer {
    private String exam_id;
    private String question_id;
    private String student_id;
    private String answer;
    private String timestamp;
    private String file_name;

    public FileAnswer(String exam_id, String student_id) {
        this.exam_id = exam_id;
        this.student_id = student_id;
    }


    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getAnswer() {
        return answer;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}
