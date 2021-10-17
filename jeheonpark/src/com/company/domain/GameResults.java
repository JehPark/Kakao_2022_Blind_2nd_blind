package com.company.domain;

import java.util.ArrayList;
import java.util.List;

public class GameResults {
    private List<Game> game_result = new ArrayList<>();

    public GameResults() {
    }

    public GameResults(List<Game> game_result) {
        this.game_result = game_result;
    }

    public List<Game> getGame_result() {
        return game_result;
    }

    public void setGame_result(List<Game> game_result) {
        this.game_result = game_result;
    }

    @Override
    public String toString() {
        return "MatchResults{" +
                "game_result=" + game_result +
                '}';
    }
}
