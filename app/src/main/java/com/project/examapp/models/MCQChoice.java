package com.project.examapp.models;

public class MCQChoice {
    private String question_id;
    private String mcq;

    public MCQChoice(String question_id)
    {
        this.question_id = question_id;
        this.mcq = "";
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getMcq() {
        return mcq;
    }

    public void setMcq(String mcq) {
        this.mcq = mcq;
    }
}
