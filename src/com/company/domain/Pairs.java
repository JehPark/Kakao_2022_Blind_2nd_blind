package com.company.domain;

import java.util.ArrayList;
import java.util.List;

public class Pairs {
    private List<List<Integer>> pairs = new ArrayList<>();

    public Pairs() {
    }

    public Pairs(List<List<Integer>> pairs) {
        this.pairs = pairs;
    }

    public List<List<Integer>> getPairs() {
        return pairs;
    }

    @Override
    public String toString() {
        return "Pairs{" +
                "pairs=" + pairs +
                '}';
    }
}
