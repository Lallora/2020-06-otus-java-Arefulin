package ru.otus.atm.cash_vault.services;

import ru.otus.atm.cash_vault.cells.Cell;

import java.util.List;

public class ServiceTuple {
    public int requiredAmount;
    public List<Cell> listCellsWithBanknotes;
    public List<Integer> listIntBillNumbersInCells;
    public List<String> listStrMaxAmountCalculation;
}
