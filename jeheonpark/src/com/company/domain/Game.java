package com.company.domain;

public class Game {
    private int win;
    private int lose;
    private int taken;

    public Game() {
    }

    public Game(int win, int lose, int taken) {
        this.win = win;
        this.lose = lose;
        this.taken = taken;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public int getTaken() {
        return taken;
    }

    public void setTaken(int taken) {
        this.taken = taken;
    }

    @Override
    public String toString() {
        return "Match{" +
                "win=" + win +
                ", lose=" + lose +
                ", taken=" + taken +
                '}';
    }
}
