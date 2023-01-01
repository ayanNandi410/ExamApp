package com.project.examapp.models;

public class Exam {
    private String exam_id;
    private String name;
    private String description;
    private String subject_id;

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
}
