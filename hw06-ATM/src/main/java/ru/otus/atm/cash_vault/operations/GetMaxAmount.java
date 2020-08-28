package ru.otus.atm.cash_vault.operations;

import ru.otus.atm.cash_vault.cells.Cell;
import ru.otus.atm.cash_vault.operations.interfaces.IOperation;

import java.util.List;

import static ru.otus.atm.cash_vault.services.Setup.MAX_NUMBER_FOR_OUTPUT;

public class GetMaxAmount implements IOperation {

    public static int execute(List<Cell> listCellsWithBanknotes) {
        int numberForOutput = MAX_NUMBER_FOR_OUTPUT;
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
