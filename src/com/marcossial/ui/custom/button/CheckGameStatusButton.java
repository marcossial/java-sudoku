package com.marcossial.ui.custom.button;

import javax.swing.JButton;
import java.awt.event.ActionListener;

public class CheckGameStatusButton extends JButton {

    public CheckGameStatusButton(final ActionListener actionListener) {
        setText("Verificar status do jogo");
        this.addActionListener(actionListener);
    }
}
