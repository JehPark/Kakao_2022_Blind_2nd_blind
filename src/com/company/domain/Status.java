package com.company.domain;

public class Status {
    private String status;
    private int time;

    public Status() {
    }

    public Status(String status, int time) {
        this.status = status;
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Status{" +
                "status='" + status + '\'' +
                ", time=" + time +
                '}';
    }
}
