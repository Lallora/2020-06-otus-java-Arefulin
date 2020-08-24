package ru.otus.atm.cash_vault.cells;

public interface ICell {

    boolean getBills(int quantity);

    int getDenomination();

    int getCounter();

}
