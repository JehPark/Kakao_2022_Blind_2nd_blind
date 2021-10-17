package com.company.domain;

public class Waiting {
    private int id;
    private int from;

    public Waiting() {
    }

    public Waiting(int id, int from) {
        this.id = id;
        this.from = from;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }


    @Override
    public String toString() {
        return "Waiting{" +
                "id=" + id +
                ", from=" + from +
                '}';
    }
}
