package com.marcossial.ui.custom.button;

import javax.swing.*;
import java.awt.event.ActionListener;

public class FinishGameButton extends JButton {
    public FinishGameButton(final ActionListener actionListener) {
        setText("Concluir jogo");
        this.addActionListener(actionListener);
    }
}
