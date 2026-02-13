package com.marcossial.ui.custom.screen;

import com.marcossial.model.Cell;
import com.marcossial.service.BoardService;
import com.marcossial.service.Event;
import com.marcossial.service.NotifierService;
import com.marcossial.ui.custom.button.CheckGameStatusButton;
import com.marcossial.ui.custom.button.FinishGameButton;
import com.marcossial.ui.custom.button.ResetButton;
import com.marcossial.ui.custom.frame.MainFrame;
import com.marcossial.ui.custom.input.NumberText;
import com.marcossial.ui.custom.panel.MainPanel;
import com.marcossial.ui.custom.panel.SudokuSector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainScreen {
    private final static Dimension dimension = new Dimension(600, 600);
    private final BoardService boardService;
    private final NotifierService notifierService;
    private JButton resetButton;
    private JButton checkGameStatusButton;
    private JButton finishGameButton;

    public MainScreen(final Map<String, String> config) {
        this.boardService = new BoardService(config);
        this.notifierService = new NotifierService();
    }

    public void buildMainScreen() {
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);

        for (int r = 0; r < 9; r += 3) { // Grupos de Linhas
            for (int c = 0; c < 9; c += 3) { // Grupos de Colunas
                int endRow = r + 2;
                int endCol = c + 2;

                List<Cell> cells = getCellsFromSector(boardService.getCells(), c, endCol, r, endRow);
                JPanel sector = generateSection(cells);
                mainPanel.add(sector);
            }
        }
        addResetButton(mainPanel);
        addFinishGameButton(mainPanel);
        addCheckGameStatusButton(mainPanel);

        mainFrame.revalidate();
        mainFrame.repaint();
        mainFrame.setVisible(true);
    }

    private List<Cell> getCellsFromSector(final List<List<Cell>> cells,
                                          final int initCol,
                                          final int endCol,
                                          final int initRow,
                                          final int endRow) {
        List<Cell> cellSector = new ArrayList<>();
        for (int r = initRow; r <= endRow; r++) {
            for (int c = initCol; c <= endCol; c++) {
                // POSSIVEL ERRO
                cellSector.add(cells.get(r).get(c));
            }
        }
        return cellSector;
    }

    private JPanel generateSection(final List<Cell> cells) {
        List<NumberText> fields = new ArrayList<>(cells.stream().map(NumberText::new).toList());
        fields.forEach(t -> notifierService.subscribe(Event.CLEAR_SPACE, t));
        return new SudokuSector(fields);
    }

    private void addFinishGameButton(JPanel mainPanel) {
        finishGameButton = new FinishGameButton(e -> {
            if (boardService.gameIsFinished()) {
                JOptionPane.showMessageDialog(null, "Parabéns você concluir o jogo");
                resetButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null, "Seu jogo contém alguma inconsistência");
            }
        });
        mainPanel.add(finishGameButton);
    }

    private void addResetButton(JPanel mainPanel) {
        resetButton = new ResetButton(e -> {
            var DialogResult = JOptionPane.showConfirmDialog(null,
                    "Deseja realmente reiniciar o jogo?",
                    "Limpar o jogo",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (DialogResult == 0) {
                boardService.reset();
                notifierService.notify(Event.CLEAR_SPACE);
            }
        });
        mainPanel.add(resetButton);
    }

    private void addCheckGameStatusButton(JPanel mainPanel) {
        checkGameStatusButton = new CheckGameStatusButton(e -> {
            var hasErrors = boardService.hasErrors();
            var gameStatus = boardService.getStatus();
            var message = switch (gameStatus){
                case NON_STARTED -> "O jogo não foi iniciado";
                case INCOMPLETE -> "O jogo está incompleto";
                case COMPLETE -> "O jogo está completo";
            };
            message += hasErrors ? " e contém erros" : " e não contém erros";
            JOptionPane.showMessageDialog(null, message);
        });
        mainPanel.add(checkGameStatusButton);
    }
}
