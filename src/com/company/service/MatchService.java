package com.company.service;

import com.company.domain.*;

import java.util.*;

public class MatchService {
    public static final int MAX_POINT = 100000;
    public static final int MIN_POINT = 1000;
    public static final int WINNER_BASE_POINT = 100;
    private static final int BONUS_RATE = 1000;
    private static final int PENALTY_RATE = 1000;
    public static final int LOSER_BASE_POINT = 30;
    private PriorityQueue<User> pq = new PriorityQueue<>(Comparator.comparingInt(User::getGrade));
    private List<User> users;
    private boolean[] pointRange = new boolean[MAX_POINT + 1];
    private List<MatchResult> rate = new ArrayList<>();

    public MatchService(WaitingLine waitingLine, UserInfo userInfo) {
        users = userInfo.getUser_info();
        updateQueue(waitingLine);
        for (int i = 0; i < 901; i++){
            rate.add(new MatchResult(0.0d, 0.0d));
        }
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void updateQueue(WaitingLine waitingLine){
        for (Waiting waiting : waitingLine.getWaiting_line()){
            pq.add(users.get(waiting.getId() - 1));
        }
    }

    public Pairs matchingAlgorithm() {
        final Pairs res = new Pairs();
        while (pq.size() != 0 && pq.size() != 1){
            int id1 = pq.poll().getId();
            int id2 = pq.poll().getId();
            if (Math.abs(users.get(id1 - 1).getGrade() - users.get(id2 - 1).getGrade()) > 5000) continue;
            res.getPairs().add(Arrays.asList(id1, id2));
        }
        return res;
    }

    public Commands initGrade(){
        final Commands commands = new Commands();
        int i = 1;
        int grade = 1000;
        for (User user : users){
            pointRange[grade] = true;
            commands.getCommands().add(new User(i++, grade));
        }
        return commands;
    }

    public Commands updateGrade(GameResults gameResults) {
        Commands commands = new Commands();
        for (Game game : gameResults.getGame_result()) {
            updateWinner(commands, game);
            updateLoser(commands, game);
        }
        return commands;
    }

    private void updateLoser(Commands commands, Game game) {
        final User loser = users.get(game.getLose() - 1);
        final MatchResult loserMatch = rate.get(loser.getId() - 1);
        loserMatch.loseGame();
        int penalty = getPenalty(loserMatch);
        int loserPoint = getLoserPoint(loser, penalty);
        if (loserPoint >= MIN_POINT){
            pointRange[loser.getGrade()] = false;
            pointRange[loserPoint] = true;
            commands.addUserInCommands(loser.getId(), loserPoint);
        }
    }

    private void updateWinner(Commands commands, Game game) {
        final User winner = users.get(game.getWin() - 1);
        final MatchResult winnerMatch = rate.get(winner.getId() - 1);
        winnerMatch.winGame();
        int bonus = getBonus(winnerMatch);
        int winnerPoint = getWinnerPoint(winner, bonus);
        if (winnerPoint <= MAX_POINT){
            pointRange[winner.getGrade()] = false;
            pointRange[winnerPoint] = true;
            commands.addUserInCommands(winner.getId(), winnerPoint);
        }
    }

    private int getLoserPoint(User loser, int penalty) {
        int loserPoint = loser.getGrade() - LOSER_BASE_POINT - penalty;
        if (loserPoint > MAX_POINT) return MAX_POINT + 1;
        while (pointRange[loserPoint]){
            if (loserPoint < MIN_POINT) return MAX_POINT + 1;
            loserPoint -= 1;
        }
        return loserPoint;
    }

    private int getWinnerPoint(User winner, int bonus) {
        int winnerPoint = winner.getGrade() + WINNER_BASE_POINT + bonus;
        if (winnerPoint > MAX_POINT) return MIN_POINT - 1;
        while (pointRange[winnerPoint]){
            winnerPoint += 1;
            if (winnerPoint > MAX_POINT) return MIN_POINT - 1;
        }
        return winnerPoint;
    }

    private int getPenalty(MatchResult loserMatch) {
        int penalty = 0;
        if (loserMatch.isPlayed()) {
            penalty = loserMatch.calculatePenalty(PENALTY_RATE);
        }
        return penalty;
    }

    private int getBonus(MatchResult winnerMatch) {
        int bonus = 0;
        if (winnerMatch.isPlayed()){
            bonus = winnerMatch.calculateBonus(BONUS_RATE);
        }
        return bonus;
    }
}
