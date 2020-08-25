package ru.otus.atm.cash_vault.operations;

import ru.otus.atm.cash_vault.cells.Cell;
import ru.otus.atm.cash_vault.operations.interfaces.IOperation;
import ru.otus.atm.cash_vault.services.Setup;
import ru.otus.atm.cash_vault.util.ToConsole;

import java.util.List;

public class ShowCashDispenserInfo implements IOperation {
    public static int execute(List<Cell> listCellsWithBanknotes) {
        int fullAmount = 0;
        ToConsole.print(Setup.STR_CASH_DISPENSER_STATE);
        ToConsole.print(Setup.STR_SEPARATE_LINE);
        ToConsole.print(Setup.STR_TABLE_HEAD);
        ToConsole.print(Setup.STR_SEPARATE_LINE);
        for (Cell billAcceptorCell : listCellsWithBanknotes) {
            ToConsole.print("| \t" + billAcceptorCell.getDenomination() + " \t |\t"
                    + billAcceptorCell.getCounter() + "/" + Setup.MAX_NUMBER_IN_CELL + "\t|");
            fullAmount += billAcceptorCell.getCounter() * billAcceptorCell.getDenomination();
        }
        ToConsole.print(Setup.STR_SEPARATE_LINE);
        ToConsole.print("Total ATM cash: " + fullAmount + "\n");
        return Setup.CHECK_PASSED;
    }
}
