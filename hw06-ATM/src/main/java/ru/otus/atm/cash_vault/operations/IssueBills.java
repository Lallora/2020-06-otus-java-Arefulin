package ru.otus.atm.cash_vault.operations;

import ru.otus.atm.cash_vault.cells.Cell;
import ru.otus.atm.cash_vault.operations.interfaces.IOperation;
import ru.otus.atm.cash_vault.services.ServiceTuple;
import ru.otus.atm.cash_vault.services.Setup;
import ru.otus.atm.cash_vault.util.ToConsole;

public class IssueBills implements IOperation {

    public static int execute(ServiceTuple serviceTuple) {
        int issuedAmount = 0;
        for (int i = 0, listOfCellsWithBanknotesSize = serviceTuple.listCellsWithBanknotes.size(); i < listOfCellsWithBanknotesSize; i++) {
            Cell cellWithBanknotes = serviceTuple.listCellsWithBanknotes.get(i);
            int billNumber = serviceTuple.listIntBillNumbersInCells.get(i);
            cellWithBanknotes.getBills(billNumber);
            int tempExistCount = cellWithBanknotes.getCounter();
            ToConsole.print(billNumber + "/" + tempExistCount + " bills of " +
                    cellWithBanknotes.getDenomination() + " for the amount of " +
                    billNumber * cellWithBanknotes.getDenomination());
            issuedAmount += billNumber * cellWithBanknotes.getDenomination();
        }
        if (serviceTuple.requiredAmount == issuedAmount) {
            return Setup.CHECK_PASSED;
        } else {
            return Setup.SOMETHING_WRONG;
        }
    }
}
