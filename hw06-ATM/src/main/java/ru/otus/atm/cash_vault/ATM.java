package ru.otus.atm.cash_vault;

import ru.otus.atm.cash_vault.cells.Cell;
import ru.otus.atm.cash_vault.operations.*;
import ru.otus.atm.cash_vault.services.ServiceTuple;
import ru.otus.atm.cash_vault.services.Setup;
import ru.otus.atm.cash_vault.util.ToConsole;

import java.util.ArrayList;
import java.util.List;

import static ru.otus.atm.cash_vault.services.Setup.GREEN;

public class ATM implements IATM {
    private boolean executionControl = false;
    private final List<Cell> listCellsWithBanknotes = new ArrayList<>();

    public ATM() {
        CreateCashDispenser.execute(listCellsWithBanknotes);
    }

    public ATM(boolean executionControl) {
        this.executionControl = executionControl;
        CreateCashDispenser.execute(listCellsWithBanknotes);
    }

    public int getAmount(int requiredAmount) {
        String maxAmountCalculation;
        ServiceTuple serviceTuple = new ServiceTuple();
        List<Integer> listBillNumbersInCells = new ArrayList<>();
        List<String> listStrMaxAmountCalculation = new ArrayList<>();
        int checkInputData = CheckInputData.execute(requiredAmount);
        if (checkInputData == Setup.CHECK_PASSED) {
            serviceTuple.executionControl = executionControl;
            serviceTuple.requiredAmount = requiredAmount;
            serviceTuple.listIntBillNumbersInCells = listBillNumbersInCells;
            serviceTuple.listCellsWithBanknotes = listCellsWithBanknotes;
            serviceTuple.listStrMaxAmountCalculation = listStrMaxAmountCalculation;

            int checkResources = CheckResources.execute(serviceTuple);
            switch (checkResources) {
                case Setup.CHECK_PASSED:
                    ToConsole.print(Setup.STR_OPERATION_APPROVED, GREEN);
                    ToConsole.print(Setup.STR_PREPARED_ISSUANCE +
                            serviceTuple.listStrMaxAmountCalculation.get(0).substring(3) + " = " + requiredAmount + "\n" +
                            Setup.STR_ISSUANCE);
                    return IssueBills.execute(serviceTuple);
                case Setup.CHECK_FAIL_NO_SUCH_AMOUNT:
                    ShowCashDispenserInfo.execute(listCellsWithBanknotes);
                    return Setup.CHECK_FAIL_NO_SUCH_AMOUNT;
                case Setup.CHECK_FAIL_NOT_ENOUGH_BILLS:
                    return Setup.CHECK_FAIL_NOT_ENOUGH_BILLS;
            }
        } else if (checkInputData == Setup.CHECK_FAIL_INCORRECT_INPUT_DATA) {
            return Setup.CHECK_FAIL_INCORRECT_INPUT_DATA;
        }
        return Setup.CHECK_PASSED;
    }
}
