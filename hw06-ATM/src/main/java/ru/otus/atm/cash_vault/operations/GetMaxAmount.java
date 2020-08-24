package ru.otus.atm.cash_vault.operations;

import ru.otus.atm.cash_vault.cells.Cell;
import ru.otus.atm.cash_vault.operations.interfaces.IGetMaxAmount;
import ru.otus.atm.cash_vault.services.Setup;

import java.util.List;

public class GetMaxAmount implements IGetMaxAmount {

    public static int execute(List<Cell> listCellsWithBanknotes) {
        int numberForOutput = Setup.MAX_NUMBER_FOR_OUTPUT;
        int availableNumberOfBills;
        int maxAmount = 0;

        for (Cell cellWithBanknotes : listCellsWithBanknotes) {
            availableNumberOfBills = cellWithBanknotes.getCounter();
            if (availableNumberOfBills >= numberForOutput) {
                maxAmount += numberForOutput * cellWithBanknotes.getDenomination();
                break;
            } else {
                numberForOutput -= availableNumberOfBills;
                maxAmount += availableNumberOfBills * cellWithBanknotes.getDenomination();
            }
        }
        return maxAmount;
    }
}
