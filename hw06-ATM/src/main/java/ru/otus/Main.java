package ru.otus;

import ru.otus.atm.cash_vault.ATM;
import ru.otus.atm.cash_vault.services.Setup;
import ru.otus.atm.cash_vault.util.ToConsole;

import java.util.ArrayList;
import java.util.List;

import static ru.otus.atm.cash_vault.services.Setup.*;

public class Main {
    public static void main(String[] args) {
        final boolean executionControl = true;
        int result;
        ATM cd = new ATM(executionControl);
        List<Integer> listGetMoney = new ArrayList<>();
        listGetMoney.add(9800);
        listGetMoney.add(50000);
        listGetMoney.add(79800);
        listGetMoney.add(29800);
        listGetMoney.add(29800);
        listGetMoney.add(19800);
        listGetMoney.add(9800);
        listGetMoney.add(9800);
        listGetMoney.add(9800);
        for (int i = 0; i < listGetMoney.size(); i++) {
            ToConsole.print(executionControl, Setup.STR_SEPARATE_LINE);
            ToConsole.print(executionControl, Setup.STR_REQUESTED_BY_USER + listGetMoney.get(i), BLUE);

            result = cd.getAmount(listGetMoney.get(i));

            switch (result) {
                case Setup.CHECK_FAIL_INCORRECT_INPUT_DATA:
                    ToConsole.print(executionControl, Setup.STR_CHECK_FAIL_INCORRECT_INPUT_DATA, RED);
                    break;
                case Setup.CHECK_FAIL_NO_SUCH_AMOUNT:
                    ToConsole.print(executionControl, Setup.STR_CHECK_FAIL_NO_SUCH_AMOUNT + listGetMoney.get(i), RED);
                    break;
                case Setup.CHECK_FAIL_NOT_ENOUGH_BILLS:
                    ToConsole.print(executionControl, Setup.STR_CHECK_FAIL_NOT_ENOUGH_BILLS, RED);
                    break;
                case Setup.SOMETHING_WRONG:
                    ToConsole.print(executionControl, Setup.STR_SOMETHING_WRONG, RED);
                    break;
                case Setup.CHECK_PASSED:
                    ToConsole.print(executionControl, Setup.STR_CHECK_PASSED + listGetMoney.get(i), GREEN);
                    break;
            }
        }
    }
}
