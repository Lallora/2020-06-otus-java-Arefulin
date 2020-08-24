package ru.otus.atm.cash_vault.operations;

import ru.otus.atm.cash_vault.cells.Cell;
import ru.otus.atm.cash_vault.operations.interfaces.ICreateCashDispenser;
import ru.otus.atm.cash_vault.services.Setup;

import java.util.List;

public class CreateCashDispenser implements ICreateCashDispenser {
    private List<Cell> listOfCellsWithBanknotes;

    public static void execute(List<Cell> cellList) {
        cellList.add(new Cell(5000, Setup.MAX_NUMBER_IN_CELL));
        cellList.add(new Cell(1000, Setup.MAX_NUMBER_IN_CELL));
        cellList.add(new Cell(500, Setup.MAX_NUMBER_IN_CELL));
        cellList.add(new Cell(200, Setup.MAX_NUMBER_IN_CELL));
        cellList.add(new Cell(100, Setup.MAX_NUMBER_IN_CELL));
    }
}
