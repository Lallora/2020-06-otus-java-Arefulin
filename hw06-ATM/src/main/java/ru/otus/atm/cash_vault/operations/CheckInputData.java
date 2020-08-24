package ru.otus.atm.cash_vault.operations;

import ru.otus.atm.cash_vault.operations.interfaces.ICheckInputData;
import ru.otus.atm.cash_vault.services.Setup;

public class CheckInputData implements ICheckInputData {
    public static int execute(int requiredAmount) {
        if (requiredAmount >= 0 && requiredAmount % 100 == 0) {
            return Setup.CHECK_PASSED;
        } else {
            return Setup.CHECK_FAIL_INCORRECT_INPUT_DATA;
        }
    }
}
