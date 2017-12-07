package edu.uiowa.cs.similarity;

import javax.validation.constraints.NotNull;

public class Value implements Comparable<Value> {
    private double k;
    private String v;

    public Value(double key, String value) {
        this.k = key;
        this.v = value;
    }

    public double getKey() {
        return this.k;
    }

    public String getValue() {
        return this.v;
    }

    @Override
    public int compareTo(@NotNull Value o) {
        return Double.compare(this.getKey(), o.getKey());
    }
}