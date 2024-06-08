package main.java.models;

import java.io.Serial;
import java.io.Serializable;

public class Seat implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int row;
    private int column;
    private boolean isBooked;

    // Конструкторы, геттеры и сеттеры
    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
        this.isBooked = false;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "row=" + row +
                ", column=" + column +
                ", isBooked=" + isBooked +
                '}';
    }
}