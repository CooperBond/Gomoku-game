import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class Gomoku {
    public static int SIZE = 15; // переменная для размера массива
    public static int WIN_COUNT = 5; //переменная для количества символов для победы

    public static char NO_WINNER = 0;


    public static char gameTable[][] = new char[SIZE][SIZE]; // массив 15х15
    public static JLabel cells[][];

    public static int count = 0;
    public static int[][] winningCoordinates = new int[WIN_COUNT][2];

    public static void init() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                gameTable[i][j] = Characters.EMPTY; // доступ к элементам массива 15х15 и заполнение его пустыми значениями
            }
        }
    }

    public static void makeTurn(int i, int j, char figure) {
        gameTable[i][j] = figure;
        drawFigure(i, j);
    }

    public static void drawFigure(int i, int j) {
        cells[i][j].setText(String.valueOf(gameTable[i][j]));
    }

    public static boolean isCellFree(int i, int j) {
        return gameTable[i][j] == Characters.EMPTY;
    }

    public static void makeHumanTurn(int i, int j) {
        makeTurn(i, j, Characters.HUMAN);
    }

    public static boolean hasEmptyCells() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameTable[i][j] == Characters.EMPTY) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void clear() {
        count = 0;
    }

    public static void addWinningCoordinate(int row, int col) {
        winningCoordinates[count][0] = row;
        winningCoordinates[count][1] = col;
        count++;
    }

    public static void markWinningCombinationByRedColor() {
        for (int k = 0; k < WIN_COUNT; k++) {
            int row = winningCoordinates[k][0];
            int col = winningCoordinates[k][1];
            cells[row][col].setForeground(Color.RED);
            cells[row][col].setFont(new Font(Font.SERIF, Font.BOLD, 35));
        }
    }

    public static void makeComputerTurn() {//переименовать методы и сделать умнее
        int emptyCells = 1;
        for (int count = WIN_COUNT - 1; count > 0; count--) {
            if (tryToMakeATurnByRows(count, Characters.COMPUTER, 1)) {
                return;
            } else if (tryToMakeATurnByColumns(count, Characters.COMPUTER, 1)) {
                return;
            } else if (tryToMakeATurnByMainDiagonal(count, Characters.COMPUTER,
                    1)) {
                return;
            } else if (tryToMakeATurnByNotMainDiagonal(count,
                    Characters.COMPUTER, 1)) {
                return;
            }
            if (count >= 3) {
                if (tryToMakeATurnByRows(count, Characters.HUMAN, emptyCells)) {
                    return;
                } else if (tryToMakeATurnByColumns(count, Characters.HUMAN,
                        emptyCells)) {
                    return;
                } else if (tryToMakeATurnByMainDiagonal(count, Characters.HUMAN,
                        emptyCells)) {
                    return;
                } else if (tryToMakeATurnByNotMainDiagonal(count,
                        Characters.HUMAN, emptyCells)) {
                    return;
                }
                emptyCells++;
            }
        }
        makeARandomTurn();
    }

    public static void makeARandomTurn() {//через 2мер массив переделать и убрать цикл
        while (true) {
            int min = SIZE - SIZE;
            int max = SIZE - 1;
            int i = min + (int) (Math.random() * max);
            int mini = SIZE - SIZE;
            int maxi = SIZE - 1;
            int j = mini + (int) (Math.random() * maxi);
            if (gameTable[i][j] == Characters.EMPTY) {
                makeTurn(i, j, Characters.COMPUTER);
                return;
            }
        }
    }

    public static boolean findWinnerByRow(char figure) {
        for (int i = 0; i < SIZE; i++) {
            clear();
            int count = 0;
            for (int j = 0; j < SIZE; j++) {
                if (gameTable[i][j] == figure) {
                    addWinningCoordinate(i, j);
                    count++;
                    if (count == WIN_COUNT) {
                        markWinningCombinationByRedColor();
                        return true;
                    }

                } else {
                    count = 0;
                    clear();
                }
            }
        }
        return false;
    }

    public static boolean findWinnerByColumn(char figure) {
        for (int i = 0; i < SIZE; i++) {
            clear();
            int count = 0;
            for (int k = 0; k < SIZE; k++) {
                if (gameTable[k][i] == figure) {
                    addWinningCoordinate(k, i);
                    count++;
                    if (count == WIN_COUNT) {
                        markWinningCombinationByRedColor();
                        return true;
                    }
                } else {
                    count = 0;
                    clear();
                }
            }
        }
        return false;
    }

    public static boolean findWinnerByMainDiagonal(char figure) {
        for (int j = 0; j <= SIZE - WIN_COUNT; j++) {
            for (int i = 0; i <= SIZE - WIN_COUNT; i++) {
                clear();
                int count = 0;
                for (int k = 0; k < WIN_COUNT; k++) {
                    if (gameTable[k + j][k + i] == figure) {
                        addWinningCoordinate(k + j, k + i);
                        count++;
                        if (count == WIN_COUNT) {
                            markWinningCombinationByRedColor();
                            return true;
                        }
                    } else {
                        clear();
                        count = 0;
                    }
                }
            }
        }
        return false;
    }

    public static boolean findWinnerByNotMainDiagonal(char figure) {
        for (int j = WIN_COUNT - 1; j < SIZE; j++) {
            for (int i = 0; i <= SIZE - WIN_COUNT; i++) {
                clear();
                int count = 0;
                for (int k = 0; k < WIN_COUNT; k++) {
                    if (gameTable[j - k][k + i] == figure) {
                        addWinningCoordinate(j - k, k + i);
                        count++;
                        if (count == WIN_COUNT) {
                            markWinningCombinationByRedColor();
                            return true;
                        }
                    } else {
                        clear();
                        count = 0;
                    }
                }
            }
        }
        return false;
    }

    public static boolean findWinner(char figure) {
        if (findWinnerByRow(figure)) {
            return true;
        } else if (findWinnerByColumn(figure)) {
            return true;
        } else if (findWinnerByMainDiagonal(figure)) {
            return true;
        } else if (findWinnerByNotMainDiagonal(figure)) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        GUIGomoku.main(args);
    }

    public static boolean tryToMakeATurnByRows(int count, char figure, int checkEmpty) {
        int emptyCells = 0;
        int computer = 0;
        int columns = 0;
        int rows = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j <= SIZE - WIN_COUNT; j++) {
                for (int k = j; k < j + WIN_COUNT; k++) {
                    if (emptyCells <= 1) {
                        if (gameTable[i][k] == figure) {
                            computer++;
                        } else if (gameTable[i][k] == Characters.EMPTY) {
                            emptyCells++;
                            columns = i;
                            rows = k;
                        } else {
                            computer = 0;
                            emptyCells = 0;
                        }
                        if (computer == count && emptyCells == checkEmpty) {
                            makeTurn(columns, rows, Characters.COMPUTER);
                            return true;
                        }
                    }
                }
                computer = 0;
                emptyCells = 0;
            }
        }
        return false;
    }

    public static boolean tryToMakeATurnByColumns(int count, char figure, int checkEmpty) {
        int emptyCells = 0;
        int computer = 0;
        int columns = 0;
        int rows = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j <= SIZE - WIN_COUNT; j++) {
                for (int k = j; k < j + WIN_COUNT; k++) {
                    if (emptyCells <= 1) {
                        if (gameTable[k][i] == figure) {
                            computer++;
                        } else if (gameTable[k][i] == Characters.EMPTY) {
                            emptyCells++;
                            columns = k;
                            rows = i;
                        } else {
                            computer = 0;
                            emptyCells = 0;
                        }
                        if (computer == count && emptyCells == checkEmpty) {
                            makeTurn(columns, rows, Characters.COMPUTER);
                            return true;
                        }
                    }
                }
                computer = 0;
                emptyCells = 0;
            }

        }

        return false;
    }

    public static boolean tryToMakeATurnByMainDiagonal(int count, char figure, int checkEmpty) {
        int emptyCells = 0;
        int computer = 0;
        int columns = 0;
        int rows = 0;
        for (int j = 0; j <= SIZE - WIN_COUNT; j++) {
            for (int i = 0; i <= SIZE - WIN_COUNT; i++) {
                for (int k = 0; k < WIN_COUNT; k++) {
                    if (emptyCells <= 1) {
                        if (gameTable[j + k][i + k] == figure) {
                            computer++;
                        } else if (gameTable[j + k][i + k] == Characters.EMPTY) {
                            emptyCells++;
                            columns = j + k;
                            rows = i + k;
                        } else {
                            computer = 0;
                            emptyCells = 0;
                        }
                        if (computer == count && emptyCells == checkEmpty) {
                            makeTurn(columns, rows, Characters.COMPUTER);
                            return true;
                        }
                    }
                }

                computer = 0;
                emptyCells = 0;
            }
        }
        return false;
    }

    public static boolean tryToMakeATurnByNotMainDiagonal(int count, char figure, int ckeckEmpty) {
        int emptyCells = 0;
        int computer = 0;
        int columns = 0;
        int rows = 0;
        for (int j = 0; j < SIZE - WIN_COUNT; j++) {
            for (int i = WIN_COUNT - 1; i < SIZE; i++) {
                for (int k = 0; k < WIN_COUNT; k++) {
                    if (emptyCells <= 1) {
                        if (gameTable[j + k][i - k] == figure) {
                            computer++;
                        } else if (gameTable[j + k][i - k] == Characters.EMPTY) {
                            emptyCells++;
                            columns = j + k;
                            rows = i - k;
                        } else {
                            computer = 0;
                            emptyCells = 0;
                        }
                        if (computer == count && emptyCells == ckeckEmpty) {
                            makeTurn(columns, rows, Characters.COMPUTER);
                            return true;
                        }
                    }
                }
                computer = 0;
                emptyCells = 0;
            }
        }
        return false;
    }
}