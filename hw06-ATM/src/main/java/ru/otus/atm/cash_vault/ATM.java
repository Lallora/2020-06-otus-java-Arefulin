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
    private final List<Cell> listCellsWithBanknotes = new ArrayList<>();

    public ATM() {
        CreateCashDispenser.execute(listCellsWithBanknotes);
    }

    public int getAmount(int requiredAmount) {
        int checkInputData = CheckInputData.execute(requiredAmount);
        return tryGetAmount(checkInputData, requiredAmount);
    }

    private int tryGetAmount(int checkInputData, int requiredAmount) {
        if (checkInputData == Setup.CHECK_PASSED) {
            List<Integer> listBillNumbersInCells = new ArrayList<>();
            List<String> listStrMaxAmountCalculation = new ArrayList<>();
            ServiceTuple serviceTuple = new ServiceTuple();
            fillServiceTuple(serviceTuple, requiredAmount, listBillNumbersInCells, listStrMaxAmountCalculation);
            int checkResources = CheckResources.execute(serviceTuple);
            return tryIssueBills(checkResources, serviceTuple);
        } else if (checkInputData == Setup.CHECK_FAIL_INCORRECT_INPUT_DATA) {
            return Setup.CHECK_FAIL_INCORRECT_INPUT_DATA;
        }
        return Setup.SOMETHING_WRONG;
    }

    private int tryIssueBills(int checkResources, ServiceTuple serviceTuple) {
        switch (checkResources) {
            case Setup.CHECK_PASSED:
                printApprovedOperation(serviceTuple, serviceTuple.requiredAmount);
                return IssueBills.execute(serviceTuple);
            case Setup.CHECK_FAIL_NO_SUCH_AMOUNT:
                ShowCashDispenserInfo.execute(listCellsWithBanknotes);
                return Setup.CHECK_FAIL_NO_SUCH_AMOUNT;
            case Setup.CHECK_FAIL_NOT_ENOUGH_BILLS:
                return Setup.CHECK_FAIL_NOT_ENOUGH_BILLS;
            default:
                return Setup.SOMETHING_WRONG;
        }
    }

    private void printApprovedOperation(ServiceTuple serviceTuple, int requiredAmount) {
        ToConsole.print(Setup.STR_OPERATION_APPROVED, GREEN);
        ToConsole.print(Setup.STR_PREPARED_ISSUANCE +
                serviceTuple.listStrMaxAmountCalculation.get(0).substring(3) + " = " + requiredAmount + "\n" +
                Setup.STR_ISSUANCE);
    }

    private void fillServiceTuple(ServiceTuple serviceTuple,
                                  int requiredAmount,
                                  List<Integer> listBillNumbersInCells,
                                  List<String> listStrMaxAmountCalculation) {
        serviceTuple.requiredAmount = requiredAmount;
        serviceTuple.listIntBillNumbersInCells = listBillNumbersInCells;
        serviceTuple.listCellsWithBanknotes = listCellsWithBanknotes;
        serviceTuple.listStrMaxAmountCalculation = listStrMaxAmountCalculation;
    }
}
