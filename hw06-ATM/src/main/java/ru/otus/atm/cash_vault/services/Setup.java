package ru.otus.atm.cash_vault.services;

public class Setup {
    public static final int MAX_NUMBER_IN_CELL = 20;
    public static final int MAX_NUMBER_FOR_OUTPUT = 40;
    public static final int CHECK_PASSED = 1;
    public static final int CHECK_FAIL_INCORRECT_INPUT_DATA = -10;
    public static final int CHECK_FAIL_NO_SUCH_AMOUNT = -11;
    public static final int CHECK_FAIL_NOT_ENOUGH_BILLS = -12;
    public static final int SOMETHING_WRONG = -13;
    public static final String STR_CHECK_PASSED = "Operation completed. Issued amount: ";
    public static final String STR_CHECK_FAIL_INCORRECT_INPUT_DATA = "Operation was canceled. The amount must be positive and divisible by 100 without a remainder.";
    public static final String STR_CHECK_FAIL_NO_SUCH_AMOUNT = "Operation was canceled. Issuance of money is impossible. There is no ";
    public static final String STR_CHECK_FAIL_NOT_ENOUGH_BILLS = "Operation was canceled. Issuance of money is impossible. Not enough bills of the required denomination.";
    public static final String STR_SOMETHING_WRONG = "Operation was canceled. Something wrong.";
    public static final String STR_REQUESTED_BY_USER = "The amount requested by the user: ";
    public static final String STR_SEPARATE_LINE = "----------------------------------";
    public static final String STR_OPERATION_APPROVED = "Operation was approved!";
    public static final String STR_PREPARED_ISSUANCE = "Prepared for issuance:\n";
    public static final String STR_ISSUANCE = "Issuance: ";
    public static final String STR_CASH_DISPENSER_STATE = "\n-CASH DISPENSER STATE-";
    public static final String STR_TABLE_HEAD = "|Denomination| Counter  |";
    public static final int RED = 501;
    public static final int GREEN = 502;
    public static final int BLUE = 503;
}
