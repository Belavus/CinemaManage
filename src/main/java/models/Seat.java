package main.java.models;

public class Seat {
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
}

