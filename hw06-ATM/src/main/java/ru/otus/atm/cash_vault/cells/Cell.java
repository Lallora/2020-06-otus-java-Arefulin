package ru.otus.atm.cash_vault.cells;

public class Cell implements ICell {

    private final int denomination;
    private int quantity;

    public Cell(int denomination, int quantity) {
        this.denomination = denomination;
        this.quantity = quantity;

    }

    public boolean getBills(int quantity) {
        int tempQuantity = this.quantity;
        tempQuantity -= quantity;
        if (tempQuantity >= 0) {
            this.quantity = tempQuantity;
            return true;
        } else return false;
    }

    public int getDenomination() {
        return denomination;
    }

    public int getCounter() {
        return quantity;
    }
}
