package ru.otus;

import ru.otus.atm.cash_vault.ATM;
import ru.otus.atm.cash_vault.services.Setup;
import ru.otus.atm.cash_vault.util.ToConsole;
import ru.otus.atm.cash_vault.util.printResult;

import java.util.ArrayList;
import java.util.List;

import static ru.otus.atm.cash_vault.services.Setup.*;

public class Main {
    public static void main(String[] args) {
        final boolean executionControl = true;
        int result;
        ATM cd = new ATM();

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
            ToConsole.print(Setup.STR_SEPARATE_LINE);
            ToConsole.print(Setup.STR_REQUESTED_BY_USER + listGetMoney.get(i), BLUE);

            result = cd.getAmount(listGetMoney.get(i));

            printResult.execute(result, listGetMoney.get(i));
        }
    }
}
