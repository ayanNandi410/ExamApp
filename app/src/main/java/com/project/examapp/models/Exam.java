package com.project.examapp.models;

public class Exam {
    private String exam_id;
    private String name;
    private Integer time;
    private String date;
    private String description;
    private String subject_id;
    private String dept;
    private boolean isAvailable;
    private String type;
    private String question = "";

    public Exam(String name,String description, String subject_id)
    {
        this.name = name;
        this.description = description;
        this.subject_id = subject_id;
    }

    public void setDescription(String desc)
    {
        this.description = desc;
    }

    public String getDescription()
    {
        return description;
    }

    public String getExamName()
    {
        return name;
    }

    public String getSubject_id()
    {
        return subject_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }


    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
