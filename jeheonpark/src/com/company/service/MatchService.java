package com.company.service;

import com.company.domain.*;

import java.util.*;

public class MatchService {
    private PriorityQueue<User> pq = new PriorityQueue<>(Comparator.comparingInt(User::getGrade));
    private List<User> users;
    private boolean[] visited = new boolean[100001];
    private List<MatchResult> rate = new ArrayList<>();
    private static int pansoo = 0;

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
            pansoo++;
        }
        return res;
    }

    public Commands initGrade(){
        final Commands commands = new Commands();
        int i = 1;
        final Random random = new Random();
        int grade = 1000;
        for (User user : users){
            visited[grade] = true;
            commands.getCommands().add(new User(i++, grade));
            grade += 0;
        }
        return commands;
    }

    public Commands updateGrade(GameResults gameResults) {
        Commands commands = new Commands();
        for (Game game : gameResults.getGame_result()) {
            final User winner = users.get(game.getWin() - 1);
            final User loser = users.get(game.getLose() - 1);
            rate.get(winner.getId() - 1).total++;
            rate.get(winner.getId() - 1).win++;
            rate.get(loser.getId() - 1).total++;
            int diff = Math.abs(winner.getGrade() - loser.getGrade());
            int bonus = 0;
            int penalty = 0;
            if (rate.get(winner.getId() - 1).total > 0){
                bonus = (int) (1000 * rate.get(winner.getId() - 1).win / rate.get(winner.getId() - 1).total);
            }
            if (rate.get(loser.getId() - 1).total > 0){
                penalty = (int) (1000 * rate.get(loser.getId() - 1).win / rate.get(loser.getId() - 1).total);
            }
            int winnerPoint = winner.getGrade() + 100 + bonus;
            if (winnerPoint > 10000) continue;
            while (visited[winnerPoint]){
                winnerPoint += 1;
                if (winnerPoint > 100000) break;
            }
            int loserPoint = loser.getGrade() - 30 - penalty;
            if (loserPoint < 1000){
                visited[winner.getGrade()] = false;
                visited[winnerPoint] = true;
                commands.getCommands().add(new User(winner.getId(), winnerPoint));
            }
            while (visited[loserPoint]){
                if (loserPoint < 1000) break;
                loserPoint -= 1;
            }
            if (winnerPoint > 100000 || loserPoint < 1000) continue;


            visited[loser.getGrade()] = false;
            visited[winner.getGrade()] = false;
            visited[winnerPoint] = true;
            visited[loserPoint] = true;
            commands.getCommands().add(new User(loser.getId(), loserPoint));
            commands.getCommands().add(new User(winner.getId(), winnerPoint));
        }
        return commands;
    }
}
