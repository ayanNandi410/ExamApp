package com.project.examapp.models;

public class User {
    private String type;
    private Student student = null;
    private Teacher teacher = null;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
