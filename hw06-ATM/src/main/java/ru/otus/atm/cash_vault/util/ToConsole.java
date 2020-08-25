package ru.otus.atm.cash_vault.util;

import static java.lang.System.*;
import static ru.otus.atm.cash_vault.services.Setup.*;

public class ToConsole {

    public static void print(String msg) {
        if (executionControl) {
            out.println(msg);
        }
    }

    public static void print(String msg, int color) {
        if (executionControl) {
            switch (color) {
                case RED:
                    out.println("\u001B[31m" + msg + "\u001B[0m");
                    break;
                case GREEN:
                    out.println("\u001B[32m" + msg + "\u001B[0m");
                    break;
                case BLUE:
                    out.println("\u001B[34m" + msg + "\u001B[0m");
                    break;
                default:
                    out.println(msg);
                    break;
            }
        }
    }
}
