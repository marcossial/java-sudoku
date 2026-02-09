package com.marcossial.model;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {
    private final List<List<Cell>> cells;

    public Board(List<List<Cell>> cells) {
        this.cells = cells;
    }

    public List<List<Cell>> getCells() {
        return cells;
    }

    public GameStatus getStatus() {
        if (cells.stream().flatMap(Collection::stream)
                .noneMatch(c -> !c.isFixed() && nonNull(c.getActual()))) {
            return GameStatus.NON_STARTED;
        }

        return cells.stream().flatMap(Collection::stream)
                .anyMatch(c -> isNull(c.getActual())) ?
                GameStatus.INCOMPLETE : GameStatus.COMPLETE;
    }

    public boolean hasErrors() {
        if (getStatus() == GameStatus.NON_STARTED) {
            return false;
        }

        return cells.stream().flatMap(Collection::stream)
                .anyMatch(c -> nonNull(c.getActual()) && c.getActual().equals(c.getExpected()));
    }

    public boolean changeValue(final int col, final int row, final int value) {
        Cell cell = cells.get(col).get(row);

        if (cell.isFixed()) {
            return false;
        }

        cell.setActual(value);
        return true;
    }

    public boolean clearValue(final int col, final int row) {
        Cell cell = cells.get(col).get(row);

        if (cell.isFixed()) {
            return false;
        }

        cell.clearCell();
        return true;
    }

    public void reset() {
        cells.forEach(col -> col.forEach(Cell::clearCell));
    }

    public boolean gameIsFinished() {
        return !hasErrors() && getStatus().equals(GameStatus.COMPLETE);
    }

}
