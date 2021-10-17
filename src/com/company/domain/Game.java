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

    public int getLose() {
        return lose;
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
