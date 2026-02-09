package com.marcossial.ui.custom.button;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ResetButton extends JButton {
    public ResetButton(final ActionListener actionListener) {
        setText("Reiniciar jogo");
        this.addActionListener(actionListener);
    }
}
