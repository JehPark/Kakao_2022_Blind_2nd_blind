package com.company.domain;

import java.util.ArrayList;
import java.util.List;

public class Commands {
    private List<User> commands = new ArrayList<>();

    public Commands() {
    }

    public Commands(List<User> commands) {
        this.commands = commands;
    }

    public List<User> getCommands() {
        return commands;
    }

    public void setCommands(List<User> commands) {
        this.commands = commands;
    }

    @Override
    public String toString() {
        return "Commands{" +
                "commands=" + commands +
                '}';
    }
}
