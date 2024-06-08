package main.java.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

public class Hall implements Serializable {
    private static final long serialVersionUID = 1L;

    private int hallNumber;
    private int[][] layout;

    // Константы для типов мест
    public static final int EMPTY = 0;
    public static final int OCCUPIED = 1;
    public static final int EMPTY_SPACE = 3;
    public static final int VIP = 4;
    public static final int ACCESSIBLE = 5;

    // Конструктор, геттеры и сеттеры
    public Hall(int hallNumber, int rows, int columns) {
        this.hallNumber = hallNumber;
        this.layout = new int[rows][columns];
    }

    public int getHallNumber() {
        return hallNumber;
    }

    public void setHallNumber(int hallNumber) {
        this.hallNumber = hallNumber;
    }

    public int[][] getLayout() {
        return layout;
    }

    public void setLayout(int[][] layout) {
        this.layout = layout;
    }

    // Методы для пометки мест
    public void markAsVIP(int row, int column) {
        if (isValidPosition(row, column)) {
            layout[row][column] = VIP;
        }
    }

    public void markAsAccessible(int row, int column) {
        if (isValidPosition(row, column)) {
            layout[row][column] = ACCESSIBLE;
        }
    }

    public void markAsEmptySpace(int row, int column) {
        if (isValidPosition(row, column)) {
            layout[row][column] = EMPTY_SPACE;
        }
    }

    private boolean isValidPosition(int row, int column) {
        return row >= 0 && row < layout.length && column >= 0 && column < layout[0].length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : layout) {
            sb.append(Arrays.toString(row)).append("\n");
        }

        return "Hall{" +
                "hallNumber=" + hallNumber +
                ", layout=" + '\n' + sb +
                '}';
    }
}

