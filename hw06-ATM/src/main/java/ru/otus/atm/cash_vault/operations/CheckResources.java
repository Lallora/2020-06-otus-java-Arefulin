package ru.otus.atm.cash_vault.operations;

import ru.otus.atm.cash_vault.cells.Cell;
import ru.otus.atm.cash_vault.operations.interfaces.IOperation;
import ru.otus.atm.cash_vault.services.ServiceTuple;
import ru.otus.atm.cash_vault.services.Setup;

public class CheckResources implements IOperation {

    public static int execute(ServiceTuple serviceTuple) {
        int checkPossibility = Setup.CHECK_PASSED;
        int tempCheckBillQuantity = Setup.MAX_NUMBER_FOR_OUTPUT;
        int tempNeedCount;
        int tempExistCount;
        int tempAmount = serviceTuple.requiredAmount;
        int maxAmount = GetMaxAmount.execute(serviceTuple.listCellsWithBanknotes);
        String maxAmountCalculation = "";
        for (int i = 0, listOfCellsWithBanknotesSize = serviceTuple.listCellsWithBanknotes.size(); i < listOfCellsWithBanknotesSize; i++) {
            Cell cellWithBanknotes = serviceTuple.listCellsWithBanknotes.get(i);
            if (tempCheckBillQuantity >= 0) {
                if (tempAmount >= 0 && tempAmount <= maxAmount) {
                    // Сколько требуется купюр данного номинала
                    tempNeedCount = tempAmount / cellWithBanknotes.getDenomination();
                    // Сколько есть купюр данного номинала
                    tempExistCount = cellWithBanknotes.getCounter();
                    if (tempExistCount >= tempNeedCount) {
                        tempCheckBillQuantity -= tempNeedCount;
                        tempAmount -= tempNeedCount * cellWithBanknotes.getDenomination();
                        serviceTuple.listIntBillNumbersInCells.add(tempNeedCount);
                        maxAmountCalculation += " + " + tempNeedCount + "*" + cellWithBanknotes.getDenomination();
                    } else {
                        tempCheckBillQuantity -= tempExistCount;
                        tempAmount -= tempExistCount * cellWithBanknotes.getDenomination();
                        serviceTuple.listIntBillNumbersInCells.add(tempExistCount);
                        maxAmountCalculation += " + " + tempExistCount + "*" + cellWithBanknotes.getDenomination();
                    }
                } else {
                    checkPossibility = Setup.CHECK_FAIL_NO_SUCH_AMOUNT;
                    break;
                }
            } else {
                checkPossibility = Setup.CHECK_FAIL_NOT_ENOUGH_BILLS;
                break;
            }
        }
        serviceTuple.listStrMaxAmountCalculation.add(maxAmountCalculation);
        return checkPossibility;
    }
}
