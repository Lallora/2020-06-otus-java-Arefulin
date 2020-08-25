package ru.otus.atm.cash_vault.operations;

import ru.otus.atm.cash_vault.cells.Cell;
import ru.otus.atm.cash_vault.operations.interfaces.IOperation;
import ru.otus.atm.cash_vault.services.Setup;

import java.util.List;

public class CreateCashDispenser implements IOperation {
    public static int execute(List<Cell> cellList) {
        cellList.add(new Cell(5000, Setup.MAX_NUMBER_IN_CELL));
        cellList.add(new Cell(1000, Setup.MAX_NUMBER_IN_CELL));
        cellList.add(new Cell(500, Setup.MAX_NUMBER_IN_CELL));
        cellList.add(new Cell(200, Setup.MAX_NUMBER_IN_CELL));
        cellList.add(new Cell(100, Setup.MAX_NUMBER_IN_CELL));
        return Setup.CHECK_PASSED;
    }
}
