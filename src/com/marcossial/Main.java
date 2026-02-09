package com.marcossial;

import com.marcossial.model.Board;
import com.marcossial.model.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.marcossial.util.BoardTemplate.BOARD_TEMPLATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);
    private static Board board;
    private final static int BOARD_LIMIT = 9;

    private final static String SEPARATOR = "------------------------------------------";
    private final static String MSG_GAME_NOT_STARTED = "Aviso: O jogo ainda não foi iniciado. Escolha a opção 1.";

    public static void main(String[] args) {
        final Map<String, String> positions = Stream.of(args).collect(Collectors.toMap(
                k -> k.split(";")[0],
                v -> v.split(";")[1]
        ));

        System.out.println("Bem-vindo ao Java Sudoku!");

        while (true) {
            printMenu();

            if (!scanner.hasNextShort()) {
                System.out.println("Erro: Por favor, digite um número válido do menu.");
                scanner.next();
                continue;
            }

            short option = scanner.nextShort();

            switch (option) {
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 0 -> {
                    System.out.println("Obrigado por jogar! Até logo.");
                    System.exit(0);
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("              MENU PRINCIPAL              ");
        System.out.println(SEPARATOR);
        System.out.printf("%d. Iniciar novo jogo\n", 1);
        System.out.printf("%d. Inserir número\n", 2);
        System.out.printf("%d. Remover número\n", 3);
        System.out.printf("%d. Visualizar tabuleiro\n", 4);
        System.out.printf("%d. Verificar status\n", 5);
        System.out.printf("%d. Limpar tabuleiro\n", 6);
        System.out.printf("%d. Finalizar e validar\n", 7);
        System.out.printf("%d. Sair\n", 0);
        System.out.print("Escolha uma opção: ");
    }

    private static void finishGame() {
        if (isNull(board)) {
            System.out.println(MSG_GAME_NOT_STARTED);
            return;
        }

        if (board.gameIsFinished()) {
            System.out.println("\n PARABÉNS! Você resolveu o Sudoku com perfeição!");
            showCurrentGame();
            board = null;
        } else if (board.hasErrors()) {
            System.out.println("\nO tabuleiro contém erros ou números duplicados. Revise suas jogadas!");
        } else {
            System.out.println("\nAinda existem casas vazias. Continue preenchendo!");
        }
    }

    private static void clearGame() {
        if (isNull(board)) {
            System.out.println(MSG_GAME_NOT_STARTED);
            return;
        }

        System.out.print("Tem certeza que deseja reiniciar o tabuleiro? (sim/nao): ");
        var confirm = scanner.next();
        while (!confirm.equalsIgnoreCase("sim") && !confirm.equalsIgnoreCase("nao")) {
            System.out.print("Resposta inválida. Digite 'sim' ou 'nao': ");
            confirm = scanner.next();
        }

        if (confirm.equalsIgnoreCase("sim")) {
            board.reset();
            System.out.println("Tabuleiro limpo com sucesso!");
        }
    }

    private static void showGameStatus() {
        if (isNull(board)) {
            System.out.println(MSG_GAME_NOT_STARTED);
            return;
        }

        System.out.printf("\nStatus atual: %s\n", board.getStatus().getLabel());
        if (board.hasErrors()) {
            System.out.println("Atenção: Existem conflitos no tabuleiro (números repetidos).");
        } else {
            System.out.println("Tudo certo até agora! Nenhum erro detectado.");
        }
    }

    private static void showCurrentGame() {
        if (isNull(board)) {
            System.out.println(MSG_GAME_NOT_STARTED);
            return;
        }

        var args = new Object[81];
        var argPos = 0;

        for (int i = 0; i < BOARD_LIMIT; i++) {
            for (var col : board.getCells()) {
                args[argPos++] = (isNull(col.get(i).getActual()) ? " " : col.get(i).getActual());
            }
        }
        System.out.println("\n--- TABULEIRO ATUAL ---");
        System.out.printf((BOARD_TEMPLATE) + "\n", args);
    }

    private static void removeNumber() {
        if (isNull(board)) {
            System.out.println(MSG_GAME_NOT_STARTED);
            return;
        }

        System.out.println("\nRemover número:");
        int col = runUntilGetValidNumber("Coluna", 1, 9);
        int row = runUntilGetValidNumber("Linha", 1, 9);

        if (board.clearValue(col - 1, row - 1)) {
            System.out.printf("Sucesso: Valor removido da posição [%d, %d].\n", col, row);
        } else {
            System.out.printf("Erro: A posição [%d, %d] contém um valor fixo do desafio.\n", col, row);
        }
    }

    private static void inputNumber() {
        if (isNull(board)) {
            System.out.println(MSG_GAME_NOT_STARTED);
            return;
        }

        System.out.println("\nInserir número:");
        int col = runUntilGetValidNumber("Coluna", 1, 9);
        int row = runUntilGetValidNumber("Linha", 1, 9);
        int value = runUntilGetValidNumber("Valor", 1, 9);

        if (board.changeValue(col - 1, row - 1, value)) {
            System.out.printf("Sucesso: Número %d inserido em [%d, %d].\n", value, col, row);
        } else {
            System.out.printf("Erro: A posição [%d, %d] é fixa e não pode ser alterada.\n", col, row);
        }
    }

    private static void startGame(Map<String, String> positions) {
        if (nonNull(board)) {
            System.out.println("O jogo já está em andamento! Use as outras opções do menu.");
            return;
        }

        List<List<Cell>> cells = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++) {
            cells.add(new ArrayList<>());
            for (int j = 0; j < BOARD_LIMIT; j++) {
                var positionConfig = positions.get("%s,%s".formatted(i, j));
                int expected = Integer.parseInt(positionConfig.split(",")[0]);
                boolean fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                Cell currentCell = new Cell(expected, fixed);
                cells.get(i).add(currentCell);
            }
        }

        board = new Board(cells);
        System.out.println("\nNovo jogo iniciado! Boa sorte!");
    }

    private static int runUntilGetValidNumber(String campo, final int min, final int max) {
        while (true) {
            System.out.printf("%s (%d-%d): ", campo, min, max);

            if (scanner.hasNextInt()) {
                int current = scanner.nextInt();
                if (current >= min && current <= max) {
                    return current;
                }
                System.out.printf("Erro: O valor de %s deve estar entre %d e %d.\n", campo, min, max);
            } else {
                String entradaInvalida = scanner.next();
                System.out.printf("Erro: '%s' não é um número válido.\n", entradaInvalida);
            }
        }
    }
}
