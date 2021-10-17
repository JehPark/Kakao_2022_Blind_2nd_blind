package com.company.domain;

import java.util.ArrayList;
import java.util.List;

public class WaitingLine {

    private List<Waiting> waiting_line = new ArrayList<>();

    public WaitingLine() {
    }

    public WaitingLine(List<Waiting> waiting_line) {
        this.waiting_line = waiting_line;
    }

    public List<Waiting> getWaiting_line() {
        return waiting_line;
    }

    public void setWaiting_line(List<Waiting> waiting_line) {
        this.waiting_line = waiting_line;
    }

    @Override
    public String toString() {
        return "WaitingLine{" +
                "waiting_line=" + waiting_line +
                '}';
    }
}
