package com.marcossial.service;

import com.marcossial.model.Board;
import com.marcossial.model.Cell;
import com.marcossial.model.GameStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoardService {
    private final static int BOARD_LIMIT = 9;

    private final Board board;

    public BoardService(final Map<String, String> config) {
        this.board = new Board(initBoard(config));
    }

    public List<List<Cell>> getCells() {
        return board.getCells();
    }

    public void reset() {
        board.reset();
    }

    public boolean hasErrors() {
        return board.hasErrors();
    }

    public GameStatus getStatus() {
        return board.getStatus();
    }

    public boolean gameIsFinished() {
        return board.gameIsFinished();
    }

    private List<List<Cell>> initBoard(Map<String, String> config) {
        List<List<Cell>> cells = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++) {
            cells.add(new ArrayList<>());
            for (int j = 0; j < BOARD_LIMIT; j++) {
                var positionConfig = config.get("%s,%s".formatted(i, j));
                int expected = Integer.parseInt(positionConfig.split(",")[0]);
                boolean fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                Cell currentCell = new Cell(expected, fixed);
                cells.get(i).add(currentCell);
            }
        }

        return board.getCells();
    }
}
