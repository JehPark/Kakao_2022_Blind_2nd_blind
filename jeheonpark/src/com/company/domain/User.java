package com.company.domain;

import javafx.util.Pair;

public class User{
    private int id;
    private int grade;

    public User() {
    }

    public User(int id, int grade) {
        this.id = id;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", grade=" + grade +
                '}';
    }
}
