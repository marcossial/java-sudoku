package com.marcossial.ui.custom.screen;

import com.marcossial.model.GameStatus;
import com.marcossial.service.BoardService;
import com.marcossial.ui.custom.button.CheckGameStatusButton;
import com.marcossial.ui.custom.button.FinishGameButton;
import com.marcossial.ui.custom.button.ResetButton;
import com.marcossial.ui.custom.frame.MainFrame;
import com.marcossial.ui.custom.panel.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class MainScreen {
    private final static Dimension dimension = new Dimension(600, 600);
    private final BoardService boardService;
    private JButton resetButton;
    private JButton checkGameStatusButton;
    private JButton finishGameButton;

    public MainScreen(final Map<String, String> config) {
        this.boardService = new BoardService(config);
    }

    public void buildMainScreen() {
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);
        addResetButton(mainPanel);
        addFinishGameButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
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
