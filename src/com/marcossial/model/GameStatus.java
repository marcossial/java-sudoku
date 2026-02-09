package com.marcossial.model;

public enum GameStatus {
    NON_STARTED("não iniciado"),
    INCOMPLETE("incompleto"),
    COMPLETE("completo");

    private String label;

    GameStatus(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
