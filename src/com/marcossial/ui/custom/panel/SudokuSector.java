package com.marcossial.ui.custom.panel;

import com.marcossial.ui.custom.input.NumberText;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class SudokuSector extends JPanel {
    public SudokuSector(final List<NumberText> textFields) {
        Dimension dimension = new Dimension(170, 170);
        this.setPreferredSize(dimension);
        this.setBorder(new LineBorder(Color.black, 2, true));
        this.setVisible(true);

        this.setLayout(new GridLayout(3, 3));
        textFields.forEach(this::add);
    }
}
