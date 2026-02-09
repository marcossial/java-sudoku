package com.marcossial.model;

public class Cell {
    private Integer actual;
    private final int expected;
    private final boolean fixed;

    public Cell(int expected, boolean fixed) {
        this.expected = expected;
        this.fixed = fixed;
        if (fixed) {
            actual = expected;
        }
    }

    public void clearCell() {
        setActual(null);
    }

    public void setActual(Integer actual) {
        if (fixed) return;
        this.actual = actual;
    }

    public Integer getActual() {
        return actual;
    }

    public int getExpected() {
        return expected;
    }

    public boolean isFixed() {
        return fixed;
    }
}
