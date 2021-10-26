package com.company.domain;

public class MatchResult {
    private double total;
    private double win;

    public MatchResult(double total, double win) {
        this.total = total;
        this.win = win;
    }

    public void winGame(){
        total++;
        win++;
    }

    public double getTotal() {
        return total;
    }

    public double getWin() {
        return win;
    }

    public void loseGame(){
        total++;
    }

    public int calculatePenalty(int rate) {
        return (int) (win / total) * rate;
    }

    public int calculateBonus(int rate) {
        return (int) (win / total) * rate;
    }

    public boolean isPlayed(){
        return total > 0;
    }


}
