package ru.otus;

public class Borders {

    private static final String strStartBorder = "************************************************************";
    private static String strStart;
    private static String strEnd;

    public static String hwStart() {
        strStart = "\n" + strStartBorder +
                "\n*************** hw01-gradle (Rafik Arefulin) ***************\n" +
                strStartBorder + "\n";
        return strStart;
    }

    public static String hwEnd() {
        strEnd = "\n********************* end hw01-gradle **********************\n";
        return strEnd;
    }
}
