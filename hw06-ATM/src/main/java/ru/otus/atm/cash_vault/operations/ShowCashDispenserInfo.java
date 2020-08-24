package ru.otus.atm.cash_vault.operations;

import ru.otus.atm.cash_vault.cells.Cell;
import ru.otus.atm.cash_vault.operations.interfaces.IShowCashDispenserInfo;
import ru.otus.atm.cash_vault.services.Setup;
import ru.otus.atm.cash_vault.util.ToConsole;

import java.util.List;

public class ShowCashDispenserInfo implements IShowCashDispenserInfo {
    public static void execute(boolean executionControl, List<Cell> listCellsWithBanknotes) {
        int fullAmount = 0;
        ToConsole.print(executionControl, Setup.STR_CASH_DISPENSER_STATE);
        ToConsole.print(executionControl, Setup.STR_SEPARATE_LINE);
        ToConsole.print(executionControl, Setup.STR_TABLE_HEAD);
        ToConsole.print(executionControl, Setup.STR_SEPARATE_LINE);
        for (Cell billAcceptorCell : listCellsWithBanknotes) {
            ToConsole.print(executionControl, "| \t" + billAcceptorCell.getDenomination() + " \t |\t"
                    + billAcceptorCell.getCounter() + "/" + Setup.MAX_NUMBER_IN_CELL + "\t|");
            fullAmount += billAcceptorCell.getCounter() * billAcceptorCell.getDenomination();
        }
        ToConsole.print(executionControl, Setup.STR_SEPARATE_LINE);
        ToConsole.print(executionControl, "Total ATM cash: " + fullAmount + "\n");
    }
}
