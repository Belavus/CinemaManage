package main.java.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Seat implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int row;
    private int column;
    private boolean isBooked;

    // Constructors, getters, setters
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

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return row == seat.row && column == seat.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
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