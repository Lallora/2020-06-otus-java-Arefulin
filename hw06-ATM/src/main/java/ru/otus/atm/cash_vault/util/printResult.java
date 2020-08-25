package ru.otus.atm.cash_vault.util;

import ru.otus.atm.cash_vault.operations.interfaces.IOperation;
import ru.otus.atm.cash_vault.services.Setup;

import static ru.otus.atm.cash_vault.services.Setup.*;

public class printResult implements IOperation {
    public static int execute(int result, int amount) {
        switch (result) {
            case Setup.CHECK_FAIL_INCORRECT_INPUT_DATA:
                ToConsole.print(Setup.STR_CHECK_FAIL_INCORRECT_INPUT_DATA, RED);
                break;
            case Setup.CHECK_FAIL_NO_SUCH_AMOUNT:
                ToConsole.print(Setup.STR_CHECK_FAIL_NO_SUCH_AMOUNT + amount, RED);
                break;
            case Setup.CHECK_FAIL_NOT_ENOUGH_BILLS:
                ToConsole.print(Setup.STR_CHECK_FAIL_NOT_ENOUGH_BILLS, RED);
                break;
            case Setup.SOMETHING_WRONG:
                ToConsole.print(Setup.STR_SOMETHING_WRONG, RED);
                break;
            case CHECK_PASSED:
                ToConsole.print(Setup.STR_CHECK_PASSED + amount, GREEN);
                break;
        }
        return CHECK_PASSED;
    }
}
