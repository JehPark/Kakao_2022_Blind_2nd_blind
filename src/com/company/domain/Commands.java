package com.company.domain;

import java.util.ArrayList;
import java.util.List;

public class Commands {
    private List<User> commands = new ArrayList<>();

    public Commands() {
    }

    public List<User> getCommands() {
        return commands;
    }

    public void addUserInCommands(int userId, int userPoint){
        commands.add(new User(userId, userPoint));
    }

    @Override
    public String toString() {
        return "Commands{" +
                "commands=" + commands +
                '}';
    }
}
